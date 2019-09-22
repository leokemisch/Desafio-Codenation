package app.integration;

import app.answer.*;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Integrations {


    public void getJson(String token){
        File file = new File("answer.json");
        try{
            if (file.createNewFile()) {
                URI uri = new URI("https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=" + token);
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(response.body());
                fileWriter.close();
            }

            String line = "";
            String fileContent = "";
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            while ((line = bufferedReader.readLine()) != null) {
                fileContent += line;
            }

            bufferedReader.close();
            fileReader.close();

            Gson gson = new Gson();
            Answer answer = gson.fromJson(fileContent, Answer.class);
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            String decifrado = "";

            for (int x = 0; x < answer.getCifrado().length(); x++) {
                char ch = answer.getCifrado().charAt(x);
                char chAux = ch;

                if (Character.isLetter(ch)) {
                    int y = (alphabet.indexOf(ch) - answer.getNumeroCasas());

                    if (y < 0) {
                        y = ((alphabet.length() - 1) + (y + 1));
                    }

                    chAux = alphabet.charAt(y);
                }

                decifrado += chAux;
            }

            answer.setDecifrado(decifrado);
            System.out.println(decifrado);

              /**
             * https://www.geeksforgeeks.org/sha-1-hash-in-java/
             */
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            byte[] digest = crypt.digest(decifrado.getBytes("UTF-8"));
            BigInteger signum = new BigInteger(1, digest);
            String hash = signum.toString(16);

            while (hash.length() < 32) {
                hash = "0" + hash;
            }

            answer.setResumoCriptografico(hash);
            
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(answer));
            fileWriter.close();

            URI uri = new URI("https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=" + token);
            Map<Object, Object> data = new LinkedHashMap<>();
            data.put("answer", file.toPath());
            String boundary = new BigInteger(256, new Random()).toString();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(uri).header("Content-Type", "multipart/form-data;boundary=" + boundary).POST(ofMimeMultipartData(data, boundary)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) throws IOException {
        ArrayList<byte[]> byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                Path path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName() + "\"\r\nContent-Type: "
                        + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return BodyPublishers.ofByteArrays(byteArrays);
    }
}
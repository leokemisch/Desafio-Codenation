package app.answer;

import com.google.gson.annotations.SerializedName;

public class Answer
{
    @SerializedName(value = "numero_casas")
    private Integer numeroCasas;
    private String token;
    private String cifrado;
    private String decifrado;
    @SerializedName(value = "resumo_criptografico")
    private String resumoCriptografico;

    public Integer getNumeroCasas() {
        return numeroCasas;
    }

    public void setNumeroCasas(Integer numeroCasas) {
        this.numeroCasas = numeroCasas;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCifrado() {
        return cifrado;
    }

    public void setCifrado(String cifrado) {
        this.cifrado = cifrado;
    }

    public String getDecifrado() {
        return decifrado;
    }

    public void setDecifrado(String decifrado) {
        this.decifrado = decifrado;
    }

    public String getResumoCriptografico() {
        return resumoCriptografico;
    }

    public void setResumoCriptografico(String resumoCriptografico) {
        this.resumoCriptografico = resumoCriptografico;
    }
}

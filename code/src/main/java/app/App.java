package app;

import app.integration.*;

public class App 
{
    public static void main( String[] args )
    {
        String token = "b51c10580666df60851a777144d53a57481e6e01";
        Integrations postToken = new Integrations();
        postToken.getJson(token);
    }
}

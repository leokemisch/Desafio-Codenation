package app;

import app.integration.*;

public class App 
{
    public static void main( String[] args )
    {
        String token = "";
        Integrations postToken = new Integrations();
        postToken.getJson(token);
    }
}

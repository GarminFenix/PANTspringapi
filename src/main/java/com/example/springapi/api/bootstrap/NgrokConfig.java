package com.example.springapi.api.bootstrap;

/**
 * Configuration class for storing NGROK URLs used in the application.
 * These URLs are typically temporary and used for tunneling local services
 * to the public internet during development or testing.
 * @author Ross Cochrane
 */
public class NgrokConfig {

    /**
     * NGROK endpoint exposing the web service.
     * This URL allows external clients to access the local web service.
     */
    public static final String NGROK_URL_WEB_SERVICE =
            System.getenv("NGROK_URL_WEB_SERVICE");

    /**
     * NGROK endpoint used by the Spring application to receive pollution data.
     * This is the callback URL that external services will use to push updates.
     */
    public static final String NGROK_URL_SPRING_SUB =
            System.getenv("NGROK_URL_SPRING_SUB");
}
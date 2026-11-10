package com.javengers.central;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class CentralApp {

    private static final int PORT = 8080;
    public static void main(String[] args) throws Exception {


        //THE CODE BELOW IS INCOMPLETE
        try {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT),0);

        server.createContext("/api/v1/central/requests");
        server.createContext("/api/v1/central/status");

        server.setExecutor(null);
        System.out.println("Server running on port: " + PORT );
        }catch(IOException e){
            System.err.println("Error during server creation process \n"+e);
        }
    }
}

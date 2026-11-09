package com.javengers.central;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class CentralApp {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

    }
}

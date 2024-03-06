package com.example.dosl1;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Set up Spark server to listen on port 4567
        port(4567);

        // Define a route that responds with "Hello, world!"
        get("/", (req, res) -> "Hello, world!");
    }
}
package com.example;
import static spark.Spark.*;

public class Frontend {
    public static void main(String[] args) {
        port(4567);

        get("/search/:topic", (request, response) -> {
            String topic = request.params(":topic");

            response.redirect("http://localhost:4575/search/" + topic);
            return null;
        });


        get("/info/:id", (request, response) -> {
            String id = request.params(":id");

            response.redirect("http://localhost:4575/info/" + id);
            return null;
        });


        post("/purchase/:id", (request, response) -> {
            String id = request.params(":id");

            response.redirect("http://localhost:3300/purchase/" + id);
            return null;
        });
    }
}
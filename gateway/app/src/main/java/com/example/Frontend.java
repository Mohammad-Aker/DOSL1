package com.example;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static spark.Spark.*;

public class Frontend {
    public static void main(String[] args) {
        port(4567);

        get("/search/:topic", (request, response) -> {
            String topic = request.params(":topic");
            try {
                HttpResponse<String> forwardResponse = Unirest.get("http://catalog:4575/search/" + topic)
                        .asString();

                if (forwardResponse.getStatus() == 200) {
                    return forwardResponse.getBody();
                } else {
                    response.status(forwardResponse.getStatus());
                    return "Forwarding error: " + forwardResponse.getBody();
                }
            } catch (UnirestException e) {
                response.status(500);
                return "Internal server error";
            }
        });

        get("/info/:id", (request, response) -> {
            String id = request.params(":id");
            try {
                HttpResponse<String> forwardResponse = Unirest.get("http://catalog:4575/info/" + id)
                        .asString();

                if (forwardResponse.getStatus() == 200) {
                    return forwardResponse.getBody();
                } else {
                    response.status(forwardResponse.getStatus());
                    return "Forwarding error: " + forwardResponse.getBody();
                }
            } catch (UnirestException e) {
                response.status(500);
                return "Internal server error";
            }
        });


        post("/purchase/:itemId", (request, response) -> {
            String itemId = request.params(":itemId");
            try {
                HttpResponse<String> forwardResponse = Unirest.post("http://order:3300/purchase/" + itemId)
                        .asString();

                if (forwardResponse.getStatus() == 200) {
                    return forwardResponse.getBody();
                } else {
                    response.status(forwardResponse.getStatus());
                    return "Forwarding error: " + forwardResponse.getBody();
                }
            } catch (UnirestException e) {
                response.status(500);
                return "Internal server error";
            }
        });
    }
}
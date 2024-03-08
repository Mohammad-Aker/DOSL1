package com.example.dosl1.backend.Order;

import static spark.Spark.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OrderServer {

    private static final String CATALOG_SERVER_URL = "http://localhost:4570"; // Adjust this to your catalog server's URL

    public static void main(String[] args) {
        port(3300); // The port this order server runs on

        post("/purchase/:itemId", (request, response) -> {
            String itemId = request.params(":itemId");
            boolean purchaseResult = attemptPurchase(itemId);

            if (purchaseResult) {
                return "Purchase successful for item ID: " + itemId;
            } else {
                response.status(400); // Bad request if purchase failed
                return "Purchase failed for item ID: " + itemId + ". Item may be out of stock or not found.";
            }
        });
    }

    private static boolean attemptPurchase(String itemId) {
        try {
            // Fetch book info to check stock
            HttpResponse<String> infoResponse = Unirest.get(CATALOG_SERVER_URL + "/info/" + itemId).asString();
            if (infoResponse.getStatus() == 200) {
                JsonObject bookInfo = JsonParser.parseString(infoResponse.getBody()).getAsJsonObject();
                int stock = bookInfo.get("stock").getAsInt();

                if (stock > 0) {
                    // If stock is available, proceed with updating stock by decreasing it by 1
                    HttpResponse<String> updateResponse = Unirest.put(CATALOG_SERVER_URL + "/updateStock/" + itemId)
                            .queryString("newStock", stock - 1)
                            .asString();
                    return updateResponse.getStatus() == 200;
                }
            }
        } catch (UnirestException e) {
            System.err.println("Error communicating with catalog server: " + e.getMessage());
        }
        return false;
    }
}

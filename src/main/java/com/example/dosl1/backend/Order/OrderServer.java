import static spark.Spark.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class OrderServer {

    private static final String CATALOG_SERVER_URL = "http://localhost:4570"; // Adjust this to your catalog server's URL

    public static void main(String[] args) {
        port(3300); // The port this order server runs on

        // Endpoint to receive purchase requests from clients
        post("/purchase/:itemId", (request, response) -> {
            String itemId = request.params(":itemId");
            boolean purchaseResult = attemptPurchase(itemId);

            if (purchaseResult) {
                return "Purchase successful for item ID: " + itemId;
            } else {
                response.status(400); // Bad request if purchase failed
                return "Purchase failed for item ID: " + itemId + ". Item may be out of stock.";
            }
        });
    }

    private static boolean attemptPurchase(String itemId) {
        try {
            // Get the current stock for the item
            HttpResponse<String> stockResponse = Unirest.get(CATALOG_SERVER_URL + "/getStock/" + itemId).asString();
            if (stockResponse.getStatus() == 200) {
                int stock = Integer.parseInt(stockResponse.getBody());
                if (stock > 0) {
                    // If stock is available, proceed with purchase and decrease stock by 1
                    HttpResponse<String> updateStockResponse = Unirest.post(CATALOG_SERVER_URL + "/updateStock/" + itemId)
                            .queryString("newStock", stock - 1)
                            .asString();
                    return updateStockResponse.getStatus() == 200;
                }
            }
        } catch (UnirestException e) {
            System.err.println("Error communicating with catalog server: " + e.getMessage());
        }
        return false;
    }
}

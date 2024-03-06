package com.example.dosl1.backend.Catalog;
import com.google.gson.Gson;
import java.util.List;
import java.util.stream.Collectors;
import static spark.Spark.*;

public class CatalogServer {
    public static void main(String[] args) {

        List<Book> books = Book.readBooksFromFile("/Users/mohammadaker/IdeaProjects/DOSL1/src/main/java/com/example/dosl1/backend/Catalog/books.txt");

        port(4570);

        Gson gson = new Gson();


        get("/info/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));

            Book resultBook = books.stream()
                    .filter(book -> book.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (resultBook != null) {

                BookInfoResponse bookInfo = new BookInfoResponse(resultBook.getTitle(), resultBook.getPrice(), resultBook.getStock());
                response.type("application/json");
                return gson.toJson(bookInfo);
            } else {
                response.status(404);
                return gson.toJson("Book not found");
            }
        });

        get("/search/:topic", (request, response) -> {
            String topic = request.params(":topic");

            List<Book> filteredBooks = books.stream()
                    .filter(book -> book.getTopic().equalsIgnoreCase(topic))
                    .collect(Collectors.toList());


            List<BookIdNameResponse> idNameResponses = filteredBooks.stream()
                    .map(book -> new BookIdNameResponse(book.getId(), book.getTitle()))
                    .collect(Collectors.toList());

            response.type("application/json");
            return gson.toJson(idNameResponses);
        });
    }
}


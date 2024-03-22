package com.example.Catalog;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static spark.Spark.*;

public class CatalogServer {
    public static void main(String[] args) {


        String booksFilePath="/usr/app/src/main/java/com/example/Catalog/books.txt";
        List<Book> books = Book.readBooksFromFile(booksFilePath);

        port(4575);

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

        put("/updateStock/:bookID", (request, response) -> {
            String bookID = request.params(":bookID");
            List<String> lines = Files.readAllLines(Paths.get(booksFilePath));
            List<String> updatedLines = new ArrayList<>();

            boolean found = false;
            for (String line : lines) {
                if (line.startsWith(bookID + ",")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        int stock = Integer.parseInt(parts[3]);
                        if (stock > 0) {
                            stock--; // Decrement the stock
                            parts[3] = String.valueOf(stock);
                            line = String.join(",", parts);
                            found = true;
                        }
                    }
                }
                updatedLines.add(line);
            }

            if (found) {
                Files.write(Paths.get(booksFilePath), updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                return "Stock updated successfully.";
            } else {
                response.status(404);
                return "Book not found or stock is 0.";
            }
        });
    }

    private static void updateBooksFile(List<Book> books) {
        try (FileWriter writer = new FileWriter("books.txt")) {
            for (Book book : books) {
                writer.write(book.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


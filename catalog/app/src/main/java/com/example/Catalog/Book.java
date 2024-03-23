package com.example.Catalog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Book {





    private int id;
    private String title;
    private String topic;
    private int stock;
    private double price;

    // Constructors
    public Book() {
    }

    public Book(int id, String title, String topic, int stock, double price) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.stock = stock;
        this.price = price;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }




    public static List<Book> readBooksFromFile(String filePath) {
        List<Book> books = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) { // Ensure correct format
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String topic = parts[2];
                    int stock = Integer.parseInt(parts[3]);
                    double price = Double.parseDouble(parts[4]);

                    Book book = new Book(id, title, topic, stock, price);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }


}



class BookInfoResponse {
    private String title;
    private double price;
    private int stock;

    public BookInfoResponse(String name, double price, int stock) {
        this.title = name;
        this.price = price;
        this.stock = stock;
    }

}

class BookIdNameResponse {
    private int id;
    private String name;

    public BookIdNameResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

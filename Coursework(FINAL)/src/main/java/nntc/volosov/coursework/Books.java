package nntc.volosov.coursework;

import java.math.BigDecimal;

public class Books {
    private int id;
    private String title;
    private String author;
    private int availableCopies;
    private int genreId;
    private BigDecimal price;
    private int quantityInCart;

    public Books(int id, String title, String author, int availableCopies, int genreId, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.availableCopies = availableCopies;
        this.genreId = genreId;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getGenreId() {
        return genreId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }

    public int getStockQuantity() {
        return availableCopies;
    }

    public String getName() {
        return title;
    }

    public int getCategoryId() {
        return genreId;
    }

    public String getDescription() {
        return author;
    }
}
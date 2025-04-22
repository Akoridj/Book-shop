package nntc.volosov.coursework;

import java.math.BigDecimal;

public class OrderItems {
    private int id;
    private int loanId;
    private int bookId;
    private int quantity;
    private BigDecimal price;

    public OrderItems(int id, int loanId, int bookId, int quantity, BigDecimal price) {
        this.id = id;
        this.loanId = loanId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getLoanId() {
        return loanId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
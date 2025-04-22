package nntc.volosov.coursework;

import java.math.BigDecimal;

public class Orders {
    private int id;
    private int userId;
    private String status;
    private BigDecimal price;

    public Orders(int id, int userId, String status, BigDecimal totalPrice) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.price = totalPrice;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
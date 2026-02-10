package reisners.model;


import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long customerId;
    private String description;
    private LocalDateTime orderDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Order() {}

    public Order(Long id, Long customerId, String description, LocalDateTime orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.description = description;
        this.orderDate = orderDate;
    }
}

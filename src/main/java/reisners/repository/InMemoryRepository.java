package reisners.repository;

import org.springframework.stereotype.Repository;
import reisners.model.Customer;
import reisners.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryRepository {
    private final ConcurrentHashMap<Long, Customer> customers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong customerIdGenerator = new AtomicLong(1);
    private final AtomicLong orderIdGenerator = new AtomicLong(1);

    public List<Customer> findAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Optional<Customer> findCustomerById(Long id) {
        return Optional.ofNullable(customers.get(id));
    }

    public Customer saveCustomer(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(customerIdGenerator.getAndIncrement());
        }
        customers.put(customer.getId(), customer);
        return customer;
    }

    public List<Order> findOrdersByCustomerId(Long customerId) {
        return orders.values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public Order saveOrder(Order order) {
        if (order.getId() == null) {
            order.setId(orderIdGenerator.getAndIncrement());
        }
        orders.put(order.getId(), order);
        return order;
    }
}

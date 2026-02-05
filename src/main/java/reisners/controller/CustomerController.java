package reisners.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reisners.assembler.CustomerModelAssembler;
import reisners.assembler.OrderModelAssembler;
import reisners.model.Customer;
import reisners.model.Order;
import reisners.repository.InMemoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final InMemoryRepository repository;
    private final CustomerModelAssembler assembler;
    private final OrderModelAssembler orderAssembler;

    public CustomerController(InMemoryRepository repository, 
                              CustomerModelAssembler assembler,
                              OrderModelAssembler orderAssembler) {
        this.repository = repository;
        this.assembler = assembler;
        this.orderAssembler = orderAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Customer>> all() {
        List<EntityModel<Customer>> customers = repository.findAllCustomers().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(customers, linkTo(methodOn(CustomerController.class).all()).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> newCustomer(@RequestBody Customer newCustomer) {
        EntityModel<Customer> entityModel = assembler.toModel(repository.saveCustomer(newCustomer));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public EntityModel<Customer> one(@PathVariable Long id) {
        Customer customer = repository.findCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Could not find customer " + id));

        return assembler.toModel(customer);
    }

    @GetMapping("/{id}/orders")
    public CollectionModel<EntityModel<Order>> getOrders(@PathVariable Long id) {
        repository.findCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Could not find customer " + id));

        List<EntityModel<Order>> orders = repository.findOrdersByCustomerId(id).stream()
                .map(orderAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(CustomerController.class).getOrders(id)).withSelfRel());
    }

    @PostMapping("/{id}/orders")
    public ResponseEntity<?> newOrder(@PathVariable Long id, @RequestBody Order order) {
        repository.findCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Could not find customer " + id));

        order.setCustomerId(id);
        order.setOrderDate(LocalDateTime.now());
        EntityModel<Order> entityModel = orderAssembler.toModel(repository.saveOrder(order));

        return ResponseEntity
                .created(entityModel.getRequiredLink("orders").toUri()) // Link back to all orders of customer
                .body(entityModel);
    }
}

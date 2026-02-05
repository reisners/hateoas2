package reisners;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateAndGetCustomer() throws Exception {
        String customerJson = "{\"name\": \"John Doe\", \"email\": \"john@example.com\"}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.customers.href").exists())
                .andExpect(jsonPath("$._links.orders.href").exists());

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testCreateOrderForCustomer() throws Exception {
        String customerJson = "{\"name\": \"Jane Doe\", \"email\": \"jane@example.com\"}";
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson));
        
        String orderJson = "{\"description\": \"New Laptop\"}";
        mockMvc.perform(post("/customers/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("New Laptop"))
                .andExpect(jsonPath("$._links.orders.href").exists())
                .andExpect(jsonPath("$._links.customer.href").exists());

        mockMvc.perform(get("/customers/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orderList[0].description").value("New Laptop"));
    }
}

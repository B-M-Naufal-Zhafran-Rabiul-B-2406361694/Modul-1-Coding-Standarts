package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void createProductPage_returnsViewAndModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPost_redirectsToList() throws Exception {
        Product created = new Product();
        created.setProductId("product-1");
        created.setProductName("Sampo Cap Bambang");
        created.setProductQuantity(100);
        when(service.create(any(Product.class))).thenReturn(created);

        mockMvc.perform(post("/product/create")
                        .param("productName", "Sampo Cap Bambang")
                        .param("productQuantity", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service).create(any(Product.class));
    }

    @Test
    void editProductPage_returnsViewAndModel() throws Exception {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Old Name");
        product.setProductQuantity(10);
        when(service.getProductById("product-1")).thenReturn(product);

        mockMvc.perform(get("/product/edit/product-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("product", sameInstance(product)));
    }

    @Test
    void editProductPost_redirectsToList() throws Exception {
        Product updated = new Product();
        updated.setProductId("product-1");
        updated.setProductName("New Name");
        updated.setProductQuantity(25);
        when(service.edit(eq("product-1"), any(Product.class))).thenReturn(updated);

        mockMvc.perform(post("/product/edit/product-1")
                        .param("productName", "New Name")
                        .param("productQuantity", "25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service).edit(eq("product-1"), any(Product.class));
    }

    @Test
    void deleteProduct_callsService() throws Exception {
        mockMvc.perform(delete("/product/delete/product-1"))
                .andExpect(status().isOk());

        verify(service).delete("product-1");
    }

    @Test
    void productListPage_returnsViewAndModel() throws Exception {
        Product product1 = new Product();
        product1.setProductId("product-1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(10);

        Product product2 = new Product();
        product2.setProductId("product-2");
        product2.setProductName("Product 2");
        product2.setProductQuantity(20);

        List<Product> products = Arrays.asList(product1, product2);
        when(service.findAll()).thenReturn(products);

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attribute("products", sameInstance(products)));
    }
}

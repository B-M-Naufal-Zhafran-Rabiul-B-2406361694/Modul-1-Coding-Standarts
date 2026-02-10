package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void create_setsIdAndPersistsProduct() {
        when(productRepository.create(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product created = productService.create(product);

        assertSame(product, created);
        assertNotNull(product.getProductId());
        assertFalse(product.getProductId().isEmpty());
        verify(productRepository).create(product);
    }

    @Test
    void edit_updatesExistingProduct() {
        Product existing = new Product();
        existing.setProductId("product-1");
        existing.setProductName("Old Name");
        existing.setProductQuantity(10);

        Product update = new Product();
        update.setProductName("New Name");
        update.setProductQuantity(25);

        Product updated = new Product();
        updated.setProductId("product-1");
        updated.setProductName("New Name");
        updated.setProductQuantity(25);

        when(productRepository.getProductById("product-1"))
                .thenReturn(existing, updated);

        Product result = productService.edit("product-1", update);

        assertSame(updated, result);
        assertEquals("product-1", update.getProductId());
        verify(productRepository).edit("product-1", update);
    }

    @Test
    void delete_callsRepository() {
        productService.delete("product-1");
        verify(productRepository).delete("product-1");
    }

    @Test
    void delete_missingProductDoesNotThrow() {
        assertDoesNotThrow(() -> productService.delete("missing-id"));
        verify(productRepository).delete("missing-id");
    }

    @Test
    void getProductById_returnsRepositoryResult() {
        Product existing = new Product();
        existing.setProductId("product-1");
        existing.setProductName("Sample Product");
        existing.setProductQuantity(10);

        when(productRepository.getProductById("product-1")).thenReturn(existing);

        Product result = productService.getProductById("product-1");

        assertSame(existing, result);
    }

    @Test
    void findAll_collectsProductsFromRepository() {
        Product product1 = new Product();
        product1.setProductId("product-1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(10);

        Product product2 = new Product();
        product2.setProductId("product-2");
        product2.setProductName("Product 2");
        product2.setProductQuantity(20);

        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products.iterator());

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertSame(product1, result.get(0));
        assertSame(product2, result.get(1));
    }
}

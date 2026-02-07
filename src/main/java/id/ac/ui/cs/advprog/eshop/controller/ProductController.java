package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage (Model model){
        Product product = new Product();
        model.addAttribute("product", product);
        return  "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model){
        service.create(product);
        return "redirect:list";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        Product product = service.getProductById(id);
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit/{id}")
    public String editProductPost(@PathVariable String id, @ModelAttribute Product product){
        service.edit(id, product);
        return "redirect:/product/list";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable String id, HttpServletResponse response){
        boolean deleted = service.delete(id);
        if (!deleted) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Product not found";
        }
        return "Deleted";
    }


    @GetMapping("/list")
    public String productListPage (Model model){
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "productList";
    }
}

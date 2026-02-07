package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product){
        productData.add(product);
        return product;
    }

    public void edit(String Id, Product updateProduct){
        Product product = getProductById(Id);
        product.setProductName(updateProduct.getProductName());
        product.setProductQuantity(updateProduct.getProductQuantity());
        return;
    }

    public boolean delete(String id){
        Iterator<Product> iterator = productData.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }


    public Product getProductById(String Id){
        for(Product product: productData){
            if(product.getProductId().equals(Id)){
                return product;
            }

        }
        return null;
    }






    public Iterator<Product> findAll(){
        return productData.iterator();
    }
}

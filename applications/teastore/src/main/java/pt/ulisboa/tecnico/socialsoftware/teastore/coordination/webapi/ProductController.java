package pt.ulisboa.tecnico.socialsoftware.teastore.coordination.webapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ulisboa.tecnico.socialsoftware.teastore.coordination.functionalities.ProductFunctionalities;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.product.aggregate.ProductDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductFunctionalities productFunctionalities;

    @PostMapping("/products/create")
    public ProductDto createProduct(@RequestBody ProductDto productDto) throws Exception {
        ProductDto result = productFunctionalities.createProduct(productDto);
        return result;
    }

    @GetMapping("/products/{productAggregateId}")
    public ProductDto findByProductId(@PathVariable Integer productAggregateId) {
        ProductDto result = productFunctionalities.findByProductId(productAggregateId);
        return result;
    }

    @GetMapping("/products/category/{categoryName}")
    public List<ProductDto> findByCategory(@PathVariable String categoryName) {
        List<ProductDto> result = productFunctionalities.findByCategory(categoryName);
        return result;
    }

    @DeleteMapping("/products/{productAggregateId}/delete")
    public void deleteProduct(@PathVariable Integer productAggregateId) throws Exception {
        productFunctionalities.deleteProduct(productAggregateId);
    }
}

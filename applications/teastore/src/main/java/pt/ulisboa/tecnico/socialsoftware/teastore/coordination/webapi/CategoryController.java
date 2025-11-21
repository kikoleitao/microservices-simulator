package pt.ulisboa.tecnico.socialsoftware.teastore.coordination.webapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ulisboa.tecnico.socialsoftware.teastore.coordination.functionalities.CategoryFunctionalities;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.category.aggregate.CategoryDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryFunctionalities categoryFunctionalities;

    @PostMapping("/categories/create")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) throws Exception {
        CategoryDto result = categoryFunctionalities.createCategory(categoryDto);
        return result;
    }

    @GetMapping("/categories")
    public List<CategoryDto> findAllCategories() {
        List<CategoryDto> result = categoryFunctionalities.findAllCategories();
        return result;
    }
}

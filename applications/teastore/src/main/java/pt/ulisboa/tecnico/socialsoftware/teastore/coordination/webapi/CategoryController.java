package pt.ulisboa.tecnico.socialsoftware.teastore.coordination.webapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ulisboa.tecnico.socialsoftware.teastore.coordination.functionalities.CategoryFunctionalities;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.*;
import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.CategoryDto;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryFunctionalities categoryFunctionalities;

    @PostMapping("/categories/create")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) throws Exception {
        return categoryFunctionalities.createCategory(categoryDto);
    }
}

package pt.ulisboa.tecnico.socialsoftware.teastore.coordination.webapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ulisboa.tecnico.socialsoftware.teastore.coordination.functionalities.UserFunctionalities;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.aggregate.UserDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserFunctionalities userFunctionalities;

    @PostMapping("/users/create")
    public UserDto createUser(@RequestBody UserDto userDto) throws Exception {
        UserDto result = userFunctionalities.createUser(userDto);
        return result;
    }

    @GetMapping("/users/{userAggregateId}")
    public UserDto findByUserId(@PathVariable Integer userAggregateId) {
        UserDto result = userFunctionalities.findByUserId(userAggregateId);
        return result;
    }

    @DeleteMapping("/users/{userAggregateId}/delete")
    public void deleteUser(@PathVariable Integer userAggregateId) throws Exception {
        userFunctionalities.deleteUser(userAggregateId);
    }
}

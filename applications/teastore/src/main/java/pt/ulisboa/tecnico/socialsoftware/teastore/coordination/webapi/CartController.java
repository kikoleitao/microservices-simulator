package pt.ulisboa.tecnico.socialsoftware.teastore.coordination.webapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ulisboa.tecnico.socialsoftware.teastore.coordination.functionalities.CartFunctionalities;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.cart.aggregate.CartDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartFunctionalities cartFunctionalities;

    @PostMapping("/carts/create")
    public CartDto createCart(@RequestBody CartDto cartDto) throws Exception {
        CartDto result = cartFunctionalities.createCart(cartDto);
        return result;
    }

    @PostMapping("/carts/{cartAggregateId}/add")
    public CartDto addItem(@PathVariable Long cartAggregateId, @RequestParam Long productId, @RequestParam String productName, @RequestParam Double unitPriceInCents, @RequestParam Integer quantity) throws Exception {
        CartDto result = cartFunctionalities.addItem(cartAggregateId, productId, productName, unitPriceInCents, quantity);
        return result;
    }

    @PutMapping("/carts/{cartAggregateId}/update")
    public CartDto updateItem(@PathVariable Long cartAggregateId, @RequestParam Long productId, @RequestParam Integer quantity) throws Exception {
        CartDto result = cartFunctionalities.updateItem(cartAggregateId, productId, quantity);
        return result;
    }

    @DeleteMapping("/carts/{cartAggregateId}/remove")
    public CartDto removeItem(@PathVariable Long cartAggregateId, @RequestParam Long productId) throws Exception {
        CartDto result = cartFunctionalities.removeItem(cartAggregateId, productId);
        return result;
    }

    @PostMapping("/carts/{cartAggregateId}/checkout")
    public CartDto checkoutCart(@PathVariable Long cartAggregateId) throws Exception {
        CartDto result = cartFunctionalities.checkoutCart(cartAggregateId);
        return result;
    }

    @GetMapping("/carts/user/{userId}")
    public CartDto findByUserId(@PathVariable Long userId) {
        CartDto result = cartFunctionalities.findByUserId(userId);
        return result;
    }
}

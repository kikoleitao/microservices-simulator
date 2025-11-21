package pt.ulisboa.tecnico.socialsoftware.teastore.coordination.webapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import pt.ulisboa.tecnico.socialsoftware.teastore.coordination.functionalities.OrderFunctionalities;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.order.aggregate.OrderDto;
import pt.ulisboa.tecnico.socialsoftware.teastore.microservices.exception.*;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderFunctionalities orderFunctionalities;

    @PostMapping("/orders/create")
    public OrderDto createOrder(@RequestBody OrderDto orderDto) throws Exception {
        OrderDto result = orderFunctionalities.createOrder(orderDto);
        return result;
    }

    @GetMapping("/orders/{orderAggregateId}")
    public OrderDto findByOrderId(@PathVariable Integer orderAggregateId) {
        OrderDto result = orderFunctionalities.findByOrderId(orderAggregateId);
        return result;
    }

    @GetMapping("/orders/user/{userAggregateId}")
    public List<OrderDto> findByUserAggregateId(@PathVariable Integer userAggregateId) {
        List<OrderDto> result = orderFunctionalities.findByUserAggregateId(userAggregateId);
        return result;
    }

    @PostMapping("/orders/{orderAggregateId}/cancel")
    public void cancelOrder(@PathVariable Integer orderAggregateId) throws Exception {
        orderFunctionalities.cancelOrder(orderAggregateId);
    }
}

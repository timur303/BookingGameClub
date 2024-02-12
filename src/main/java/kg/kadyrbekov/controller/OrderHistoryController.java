package kg.kadyrbekov.controller;

import io.swagger.annotations.*;
import kg.kadyrbekov.dto.ComputerResponse;
import kg.kadyrbekov.dto.OrderHistoryDTO;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.model.entity.OrderHistory;
import kg.kadyrbekov.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-history")
@ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")})
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @Autowired
    public OrderHistoryController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    @ApiOperation(value = "Get order-history")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully", response = OrderHistoryDTO.class),
    @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class),})
    @GetMapping(value = "/order-history-token",produces = "application/json")
    public ResponseEntity<?> getUserOrderHistory() {
        List<OrderHistory> orderHistory = orderHistoryService.getUserOrderHistory();

        List<OrderHistoryDTO> orderHistoryDTOs = orderHistory.stream()
                .map(order -> new OrderHistoryDTO(order.getId(), order.getName(), order.getPrice(),
                        order.getVipPrice(), order.getDate_Of_Operation(),
                        order.getO_J_Id(), order.getGameTypeForHistory()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderHistoryDTOs);
    }

}
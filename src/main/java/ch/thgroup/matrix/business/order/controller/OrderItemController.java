package ch.thgroup.matrix.business.order.controller;

import ch.thgroup.matrix.business.common.ApplicationPaths;
import ch.thgroup.matrix.business.order.dto.OrderItemDTO;
import ch.thgroup.matrix.business.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApplicationPaths.API_PATH + "/order")
@RequiredArgsConstructor
@Slf4j
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping(path = "/crudorderitemlist")
    public ResponseEntity<List<OrderItemDTO>> crudOrderItemList(@RequestBody List<OrderItemDTO> orderItemListDTO) {
        log.info("Received request to CRUD order item list");
        try {
            var createdOrderItemList = orderItemService.crudOrderItemList(orderItemListDTO);
            log.info("Successfully processed CRUD order item list");
            return new ResponseEntity<>(createdOrderItemList, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error processing CRUD order item list: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error processing CRUD order item list: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getorderitemsbyorderid/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        log.info("Received request to get order items by order ID: {}", orderId);
        try {
            var orderItems = orderItemService.getOrderItemsByOrderId(orderId);
            log.info("Successfully retrieved order items for order ID: {}", orderId);
            return new ResponseEntity<>(orderItems, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Error retrieving order items for order ID {}: {}", orderId, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error retrieving order items for order ID {}: {}", orderId, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/distributionsplit")
    public ResponseEntity<List<OrderItemDTO>> distributionSplit(@RequestBody List<OrderItemDTO> orderItemListDTO) {
        log.info("Received request to make distribution split");
        try {
            var createdOrderItemList = orderItemService.distributionSplit(orderItemListDTO);
            log.info("Successfully processed distribution split");
            return new ResponseEntity<>(createdOrderItemList, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error processing distribution split: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error processing distribution split: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/confirmationsupplier")
    public ResponseEntity<List<OrderItemDTO>> confirmationSupplier(@RequestBody List<OrderItemDTO> orderItemListDTO) {
        log.info("Received request to make confirmation supplier");
        try {
            var createdOrderItemList = orderItemService.confirmationSupplier(orderItemListDTO);
            log.info("Successfully processed confirmation supplier");
            return new ResponseEntity<>(createdOrderItemList, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error processing confirmation supplier: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error processing confirmation supplier: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/validatedeliveryquantity")
    public ResponseEntity<List<OrderItemDTO>> validateDeliveryQuantity(@RequestBody List<OrderItemDTO> orderItemListDTO) {
        log.info("Received request to validate delivery quantity");
        try {
            var createdOrderItemList = orderItemService.validateDeliveryQuantity(orderItemListDTO);
            log.info("Successfully processed validate delivery quantity");
            return new ResponseEntity<>(createdOrderItemList, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error processing validate delivery quantity: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error processing validate delivery quantity: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/publishorder/{orderId}")
    public ResponseEntity<Void> publishOrder(@PathVariable Long orderId) {
        orderItemService.publishOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/crudorderchritemlist")
    public ResponseEntity<List<OrderItemDTO>> crudOrderChrItemList(@RequestBody List<OrderItemDTO> orderItemListDTO) {
        log.info("Received request to CRUD order change request item list");
        try {
            var createdOrderItemList = orderItemService.crudOrderChrItemList(orderItemListDTO);
            log.info("Successfully processed CRUD order change request item list");
            return new ResponseEntity<>(createdOrderItemList, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error processing CRUD order change request item list: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error processing CRUD order change request item list: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/approveorderchr/{orderId}")
    public ResponseEntity<Void> approveOrderChr(@PathVariable Long orderId) {
        log.info("Received request to approve order change request for order ID: {}", orderId);
        try {
            orderItemService.approveOrderChr(orderId);
            log.info("Successfully approved order change request for order ID: {}", orderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Error approving order change request for order ID {}: {}", orderId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error approving order change request for order ID {}: {}", orderId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/rejectorderchr/{orderId}")
    public ResponseEntity<Void> rejectOrderChr(@PathVariable Long orderId) {
        log.info("Received request to reject order change request for order ID: {}", orderId);
        try {
            orderItemService.rejectOrderChr(orderId);
            log.info("Successfully rejected order change request for order ID: {}", orderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Error rejecting order change request for order ID {}: {}", orderId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error rejecting order change request for order ID {}: {}", orderId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
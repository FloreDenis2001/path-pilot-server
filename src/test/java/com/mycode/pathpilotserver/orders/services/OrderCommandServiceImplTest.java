package com.mycode.pathpilotserver.orders.services;

import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.city.models.City;
import com.mycode.pathpilotserver.customers.exceptions.CustomerNotFoundException;
import com.mycode.pathpilotserver.customers.models.Customer;
import com.mycode.pathpilotserver.customers.models.SubscriptionType;
import com.mycode.pathpilotserver.orders.dto.OrderDTO;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.orders.repository.OrderRepo;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import com.mycode.pathpilotserver.shipments.models.StatusType;
import com.mycode.pathpilotserver.shipments.repository.ShipmentRepo;
import com.mycode.pathpilotserver.system.enums.OrderType;
import com.mycode.pathpilotserver.user.models.User;
import com.mycode.pathpilotserver.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class OrderCommandServiceImplTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private UserRepo customerRepo;

    @Mock
    private ShipmentRepo shipmentRepo;

    @InjectMocks
    private OrderCommandServiceImpl orderCommandService;

    private User customer;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(1L)
                .username("customer_user")
                .password("password")
                .email("customer@example.com")
                .firstName("Customer")
                .lastName("User")
                .phone("1234567890")
                .subscriptionType(SubscriptionType.BASIC)
                .build();

        City city = City.builder()
                .country("Romania")
                .city("Bucuresti")
                .lat(44.4268)
                .lng(26.1025)
                .iso2("RO")
                .admin_name("Bucharest")
                .capital("Bucharest")
                .population("1877155")
                .population_proper("1877155")
                .build();

        Address originAddress = Address.builder()
                .street("Damovicioarei")
                .streetNumber("17")
                .postalCode("447065")
                .cityDetails(city)
                .build();

        City city2 = City.builder()
                .country("Romania")
                .city("Brasov")
                .lat(44.4268)
                .lng(26.1025)
                .iso2("RO")
                .admin_name("Bucharest")
                .capital("Bucharest")
                .population("1877155")
                .population_proper("1877155")
                .build();

        Address destinationAddress = Address.builder()
                .street("Strada Lunga")
                .streetNumber("23")
                .postalCode("500123")
                .cityDetails(city2)
                .build();

        orderDTO = new OrderDTO(
                1L,
                100.0,
                1.0,
                1.0,
                1.0,
                "Test Description",
                OrderType.PALLET,
                "Origin Name",
                "Destination Name",
                "000-111-222",
                "111-222-333",
                originAddress,
                destinationAddress
        );
    }

//    @Test
//    void createOrder_ShouldSaveOrder_WhenCustomerExists() {
//        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
//        when(shipmentRepo.saveAndFlush(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(orderRepo.saveAndFlush(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        orderCommandService.createOrder(orderDTO);
//
//        verify(customerRepo).findById(orderDTO.customerId());
//        verify(shipmentRepo).saveAndFlush(any(Shipment.class));
//        verify(orderRepo).saveAndFlush(any(Order.class));
//    }

    @Test
    void createOrder_ShouldThrowCustomerNotFoundException_WhenCustomerDoesNotExist() {
        lenient().when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            orderCommandService.createOrder(orderDTO);
        });

        assertEquals("Customer with id: " + orderDTO.customerId() + " not found", exception.getMessage());
    }

//    @Test
//    void createOrder_ShouldCreateOrderAndShipment_WhenValidOrderDTOIsProvided() {
//        when(customerRepo.findById(orderDTO.customerId())).thenReturn(Optional.of(customer));
//        when(shipmentRepo.saveAndFlush(any(Shipment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(orderRepo.saveAndFlush(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        orderCommandService.createOrder(orderDTO);
//
//        verify(customerRepo).findById(orderDTO.customerId());
//        verify(shipmentRepo).saveAndFlush(any(Shipment.class));
//        verify(orderRepo).saveAndFlush(any(Order.class));
//    }

    @Test
    void getShipments_ShouldReturnShipmentWithCorrectDetails() {
        Optional<Shipment> shipment = OrderCommandServiceImpl.getShipments(orderDTO);

        assertTrue(shipment.isPresent());
        Shipment s = shipment.get();
        assertEquals(orderDTO.originName(), s.getOriginName());
        assertEquals(orderDTO.destinationName(), s.getDestinationName());
        assertEquals(orderDTO.originPhone(), s.getOriginPhone());
        assertEquals(orderDTO.destinationPhone(), s.getDestinationPhone());
        assertEquals(orderDTO.origin(), s.getOriginAddress());
        assertEquals(orderDTO.destination(), s.getDestinationAddress());
        assertEquals(StatusType.PICKED, s.getStatus());
        assertTrue(s.getEstimatedDeliveryDate().isAfter(LocalDateTime.now()));
    }

    @Test
    void getOrder_ShouldReturnOrderWithCorrectDetails() {
        Order order = OrderCommandServiceImpl.getOrder(orderDTO, Optional.of(customer), Optional.of(new Shipment()));

        assertEquals(customer, order.getCustomer());
        assertNull(order.getRoute());
        assertNotNull(order.getShipment());
        assertEquals(orderDTO.height(), order.getHeight());
        assertEquals(orderDTO.weight(), order.getWeight());
        assertEquals(orderDTO.width(), order.getWidth());
        assertEquals(orderDTO.deliveryDescription(), order.getDeliveryDescription());
        assertTrue(order.getOrderDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertEquals(orderDTO.totalAmount(), order.getTotalAmount());
    }
}

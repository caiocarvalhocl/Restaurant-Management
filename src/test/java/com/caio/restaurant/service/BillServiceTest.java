package com.caio.restaurant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.caio.restaurant.dto.request.AddBillItemRequest;
import com.caio.restaurant.dto.request.CloseBillRequest;
import com.caio.restaurant.dto.request.OpenBillRequest;
import com.caio.restaurant.dto.response.BillResponse;
import com.caio.restaurant.entity.Bill;
import com.caio.restaurant.entity.BillItem;
import com.caio.restaurant.entity.Product;
import com.caio.restaurant.entity.RestaurantTable;
import com.caio.restaurant.entity.User;
import com.caio.restaurant.enums.BillStatus;
import com.caio.restaurant.enums.PaymentMethod;
import com.caio.restaurant.enums.TableStatus;
import com.caio.restaurant.enums.UserRole;
import com.caio.restaurant.repository.BillItemRepository;
import com.caio.restaurant.repository.BillRepository;
import com.caio.restaurant.repository.ProductRepository;
import com.caio.restaurant.repository.RestaurantTableRepository;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private BillItemRepository billItemRepository;

    @Mock
    private RestaurantTableRepository tableRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private BillService billService;

    private User waiter;

    @BeforeEach
    void setUp() {
        waiter = new User();
        waiter.setId(1L);
        waiter.setName("Waiter");
        waiter.setEmail("waiter@restaurant.com");
        waiter.setPassword("hashed");
        waiter.setRole(UserRole.EMPLOYEE);
    }

    @Test
    void openBill_withAvailableTable_occupiesTableAndOpensBill() {
        RestaurantTable table = new RestaurantTable();
        table.setId(10L);
        table.setTableNumber(5);
        table.setStatus(TableStatus.AVAILABLE);

        OpenBillRequest request = new OpenBillRequest(10L, "John");

        when(tableRepository.findById(10L)).thenReturn(Optional.of(table));
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BillResponse response = billService.openBill(request, waiter);

        assertThat(response.status()).isEqualTo(BillStatus.OPEN);
        assertThat(response.tableNumber()).isEqualTo(5);
        assertThat(table.getStatus()).isEqualTo(TableStatus.OCCUPIED);
        verify(tableRepository).save(table);
    }

    @Test
    void openBill_withoutTable_opensTakeawayBillWithoutTouchingTables() {
        OpenBillRequest request = new OpenBillRequest(null, "Takeaway Client");

        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BillResponse response = billService.openBill(request, waiter);

        assertThat(response.status()).isEqualTo(BillStatus.OPEN);
        assertThat(response.tableNumber()).isNull();
        verify(tableRepository, never()).save(any(RestaurantTable.class));
    }

    @Test
    void addItem_appendsItemAndRecalculatesBillTotals() {
        Bill bill = new Bill();
        bill.setId(20L);
        bill.setOpenedBy(waiter);
        bill.setStatus(BillStatus.OPEN);

        Product product = new Product();
        product.setId(30L);
        product.setName("Burger");
        product.setPrice(new BigDecimal("25.00"));
        product.setAvailable(true);

        AddBillItemRequest request = new AddBillItemRequest(30L, new BigDecimal("2"), "no onions");

        when(billRepository.findById(20L)).thenReturn(Optional.of(bill));
        when(productRepository.findById(30L)).thenReturn(Optional.of(product));

        BillResponse response = billService.addItem(20L, request, waiter);

        assertThat(response.items()).hasSize(1);
        assertThat(response.subtotal()).isEqualByComparingTo("50.00");
        assertThat(response.total()).isEqualByComparingTo("50.00");
    }

    @Test
    void addItem_onClosedBill_throwsAndDoesNotSaveItem() {
        Bill bill = new Bill();
        bill.setId(21L);
        bill.setOpenedBy(waiter);
        bill.setStatus(BillStatus.CLOSED);

        AddBillItemRequest request = new AddBillItemRequest(30L, BigDecimal.ONE, null);

        when(billRepository.findById(21L)).thenReturn(Optional.of(bill));

        assertThrows(RuntimeException.class, () -> billService.addItem(21L, request, waiter));
        verify(billItemRepository, never()).save(any());
    }

    @Test
    void closeBill_withItems_closesBillAndFreesTable() {
        RestaurantTable table = new RestaurantTable();
        table.setId(11L);
        table.setTableNumber(7);
        table.setStatus(TableStatus.OCCUPIED);

        Bill bill = new Bill();
        bill.setId(22L);
        bill.setOpenedBy(waiter);
        bill.setStatus(BillStatus.OPEN);
        bill.setTable(table);
        bill.setSubtotal(new BigDecimal("40.00"));

        Product product = new Product();
        product.setId(31L);
        product.setPrice(new BigDecimal("40.00"));
        product.setAvailable(true);

        BillItem item = new BillItem();
        item.setBill(bill);
        item.setProduct(product);
        item.setQuantity(BigDecimal.ONE);
        item.setUnitPrice(new BigDecimal("40.00"));
        item.setTotalPrice(new BigDecimal("40.00"));
        item.setAddedBy(waiter);
        bill.getItems().add(item);

        CloseBillRequest request = new CloseBillRequest(PaymentMethod.CASH, null);

        when(billRepository.findById(22L)).thenReturn(Optional.of(bill));

        BillResponse response = billService.closeBill(22L, request, waiter);

        assertThat(response.status()).isEqualTo(BillStatus.CLOSED);
        assertThat(response.paymentMethod()).isEqualTo(PaymentMethod.CASH);
        assertThat(response.total()).isEqualByComparingTo("40.00");
        assertThat(table.getStatus()).isEqualTo(TableStatus.AVAILABLE);
        verify(tableRepository).save(table);
    }

    @Test
    void closeBill_withNoItems_throwsAndLeavesTableUntouched() {
        RestaurantTable table = new RestaurantTable();
        table.setId(12L);
        table.setStatus(TableStatus.OCCUPIED);

        Bill bill = new Bill();
        bill.setId(23L);
        bill.setOpenedBy(waiter);
        bill.setStatus(BillStatus.OPEN);
        bill.setTable(table);

        CloseBillRequest request = new CloseBillRequest(PaymentMethod.CASH, null);

        when(billRepository.findById(23L)).thenReturn(Optional.of(bill));

        assertThrows(RuntimeException.class, () -> billService.closeBill(23L, request, waiter));
        assertThat(table.getStatus()).isEqualTo(TableStatus.OCCUPIED);
        verify(tableRepository, never()).save(any(RestaurantTable.class));
    }

    @Test
    void cancelBill_freesTableWithoutRequiringPayment() {
        RestaurantTable table = new RestaurantTable();
        table.setId(13L);
        table.setTableNumber(9);
        table.setStatus(TableStatus.OCCUPIED);

        Bill bill = new Bill();
        bill.setId(24L);
        bill.setOpenedBy(waiter);
        bill.setStatus(BillStatus.OPEN);
        bill.setTable(table);

        when(billRepository.findById(24L)).thenReturn(Optional.of(bill));

        BillResponse response = billService.cancelBill(24L);

        assertThat(response.status()).isEqualTo(BillStatus.CANCELLED);
        assertThat(response.paymentMethod()).isNull();
        assertThat(table.getStatus()).isEqualTo(TableStatus.AVAILABLE);
        verify(tableRepository).save(table);
    }
}

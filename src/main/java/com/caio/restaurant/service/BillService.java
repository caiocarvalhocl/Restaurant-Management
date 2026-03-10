package com.caio.restaurant.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.caio.restaurant.enums.TableStatus;
import com.caio.restaurant.exception.ResourceNotFoundException;
import com.caio.restaurant.repository.BillItemRepository;
import com.caio.restaurant.repository.BillRepository;
import com.caio.restaurant.repository.ProductRepository;
import com.caio.restaurant.repository.RestaurantTableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillService {

        private final BillRepository billRepository;
        private final BillItemRepository billItemRepository;
        private final RestaurantTableRepository tableRepository;
        private final ProductRepository productRepository;

        // === QUERIES ===

        @Transactional(readOnly = true)
        public List<BillResponse> findAll() {
                return billRepository.findAll().stream()
                                .map(BillResponse::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<BillResponse> findOpen() {
                return billRepository.findByStatus(BillStatus.OPEN).stream()
                                .map(BillResponse::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public BillResponse findById(Long id) {
                return BillResponse.toResponse(findBillById(id));
        }

        // === OPEN BILL ===

        @Transactional
        public BillResponse openBill(OpenBillRequest request, User currentUser) {
                Bill bill = new Bill();
                bill.setOpenedBy(currentUser);
                bill.setClientName(request.clientName());

                // If table specified, mark it as occupied
                if (request.tableId() != null) {
                        RestaurantTable table = tableRepository.findById(request.tableId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Table not found with id: " + request.tableId()));

                        if (table.getStatus() != TableStatus.AVAILABLE) {
                                throw new RuntimeException("Table is not available");
                        }

                        table.setStatus(TableStatus.OCCUPIED);
                        tableRepository.save(table);
                        bill.setTable(table);
                }

                Bill savedBill = billRepository.save(bill);
                return BillResponse.toResponse(savedBill);
        }

        // === ADD ITEM ===

        @Transactional
        public BillResponse addItem(Long billId, AddBillItemRequest request, User currentUser) {
                Bill bill = findBillById(billId);

                if (bill.getStatus() != BillStatus.OPEN) {
                        throw new RuntimeException("Cannot add items to a closed bill");
                }

                Product product = productRepository.findById(request.productId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Product not found with id: " + request.productId()));

                if (!product.getAvailable()) {
                        throw new RuntimeException("Product is not available: " + product.getName());
                }

                // Create bill item
                BillItem item = new BillItem();
                item.setBill(bill);
                item.setProduct(product);
                item.setQuantity(request.quantity());
                item.setUnitPrice(product.getPrice());
                item.setTotalPrice(product.getPrice().multiply(request.quantity()));
                item.setNotes(request.notes());
                item.setAddedBy(currentUser);

                billItemRepository.save(item);

                // Recalculate bill totals
                recalculateTotals(bill);

                return BillResponse.toResponse(bill);
        }

        // === REMOVE ITEM ===

        @Transactional
        public BillResponse removeItem(Long billId, Long itemId) {
                Bill bill = findBillById(billId);

                if (bill.getStatus() != BillStatus.OPEN) {
                        throw new RuntimeException("Cannot remove items from a closed bill");
                }

                BillItem item = billItemRepository.findById(itemId)
                                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

                if (!item.getBill().getId().equals(billId)) {
                        throw new RuntimeException("Item does not belong to this bill");
                }

                billItemRepository.delete(item);
                bill.getItems().remove(item);

                // Recalculate bill totals
                recalculateTotals(bill);

                return BillResponse.toResponse(bill);
        }

        // === CLOSE BILL ===

        @Transactional
        public BillResponse closeBill(Long billId, CloseBillRequest request, User currentUser) {
                Bill bill = findBillById(billId);

                if (bill.getStatus() != BillStatus.OPEN) {
                        throw new RuntimeException("Bill is already closed");
                }

                if (bill.getItems().isEmpty()) {
                        throw new RuntimeException("Cannot close a bill with no items");
                }

                // Apply discount if provided
                if (request.discount() != null && request.discount().compareTo(BigDecimal.ZERO) > 0) {
                        bill.setDiscount(request.discount());
                }

                // Calculate final total
                bill.setTotal(bill.getSubtotal().subtract(bill.getDiscount()));
                bill.setPaymentMethod(request.paymentMethod());
                bill.setStatus(BillStatus.CLOSED);
                bill.setClosedBy(currentUser);
                bill.setClosedAt(LocalDateTime.now());

                // Free up the table
                if (bill.getTable() != null) {
                        bill.getTable().setStatus(TableStatus.AVAILABLE);
                        tableRepository.save(bill.getTable());
                }

                billRepository.save(bill);
                return BillResponse.toResponse(bill);
        }

        // === CANCEL BILL ===

        @Transactional
        public BillResponse cancelBill(Long billId) {
                Bill bill = findBillById(billId);

                if (bill.getStatus() != BillStatus.OPEN) {
                        throw new RuntimeException("Cannot cancel a closed bill");
                }

                bill.setStatus(BillStatus.CANCELLED);

                // Free up the table
                if (bill.getTable() != null) {
                        bill.getTable().setStatus(TableStatus.AVAILABLE);
                        tableRepository.save(bill.getTable());
                }

                billRepository.save(bill);
                return BillResponse.toResponse(bill);
        }

        // === PRIVATE HELPERS ===

        private Bill findBillById(Long id) {
                return billRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        }

        private void recalculateTotals(Bill bill) {
                BigDecimal subtotal = bill.getItems().stream()
                                .map(BillItem::getTotalPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                bill.setSubtotal(subtotal);
                bill.setTotal(subtotal.subtract(bill.getDiscount()));
                billRepository.save(bill);
        }
}

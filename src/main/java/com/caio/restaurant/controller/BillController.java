package com.caio.restaurant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.caio.restaurant.dto.request.AddBillItemRequest;
import com.caio.restaurant.dto.request.CloseBillRequest;
import com.caio.restaurant.dto.request.OpenBillRequest;
import com.caio.restaurant.dto.response.BillResponse;
import com.caio.restaurant.entity.User;
import com.caio.restaurant.service.BillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    // Get all bills
    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'CASHIER')")
    public List<BillResponse> getAllBills() {
        return billService.findAll();
    }

    // Get open bills only
    @GetMapping("/open")
    @PreAuthorize("isAuthenticated()")
    public List<BillResponse> getOpenBills() {
        return billService.findOpen();
    }

    // Get bill by ID
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public BillResponse getBillById(@PathVariable Long id) {
        return billService.findById(id);
    }

    // Open a new bill
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public BillResponse openBill(
            @Valid @RequestBody OpenBillRequest request,
            @AuthenticationPrincipal User currentUser) {
        return billService.openBill(request, currentUser);
    }

    // Add item to bill
    @PostMapping("/{id}/items")
    @PreAuthorize("isAuthenticated()")
    public BillResponse addItem(
            @PathVariable Long id,
            @Valid @RequestBody AddBillItemRequest request,
            @AuthenticationPrincipal User currentUser) {
        return billService.addItem(id, request, currentUser);
    }

    // Remove item from bill
    @DeleteMapping("/{billId}/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public BillResponse removeItem(
            @PathVariable Long billId,
            @PathVariable Long itemId) {
        return billService.removeItem(billId, itemId);
    }

    // Close bill (payment)
    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('OWNER', 'CASHIER')")
    public BillResponse closeBill(
            @PathVariable Long id,
            @Valid @RequestBody CloseBillRequest request,
            @AuthenticationPrincipal User currentUser) {
        return billService.closeBill(id, request, currentUser);
    }

    // Cancel bill
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('OWNER', 'CASHIER')")
    public BillResponse cancelBill(@PathVariable Long id) {
        return billService.cancelBill(id);
    }
}

package com.timadeshola.vendingmachine.resource;

import com.timadeshola.vendingmachine.core.AppConstant;
import com.timadeshola.vendingmachine.model.request.UserRequest;
import com.timadeshola.vendingmachine.model.response.AppResponse;
import com.timadeshola.vendingmachine.model.response.PaginateResponse;
import com.timadeshola.vendingmachine.model.response.UserResponse;
import com.timadeshola.vendingmachine.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 9:56 PM
 */
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Api(tags = "User Management API")
public class UserResource {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AppResponse<UserResponse>> createUser(@RequestBody @Valid UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok().body(AppResponse.<UserResponse>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.CREATED.value())
                .data(response)
                .build());

    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppResponse<UserResponse>> updateUser(@RequestBody @Valid UserRequest request, @RequestParam Long id) {
        UserResponse response = userService.updateUser(request, id);
        return ResponseEntity.ok().body(AppResponse.<UserResponse>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());

    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppResponse<Boolean>> deleteUser(@RequestParam Long id) {
        Boolean response = userService.deleteUser(id);
        return ResponseEntity.ok().body(AppResponse.<Boolean>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());

    }

    @GetMapping("viewUser/{id:[\\d]}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppResponse<UserResponse>> fetchUser(@PathVariable Long id) {
        UserResponse response = userService.fetchUser(id);
        return ResponseEntity.ok().body(AppResponse.<UserResponse>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());

    }

    @GetMapping("viewUsers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppResponse<PaginateResponse<UserResponse>>> fetchUsers(@RequestParam(defaultValue = "0") int start,
                                                                                  @RequestParam(defaultValue = "10") int limit,
                                                                                  @RequestParam(required = false) String startDate,
                                                                                  @RequestParam(required = false) String endDate,
                                                                                  @RequestParam(required = false) String username,
                                                                                  @RequestParam(required = false) String role) {
        PaginateResponse<UserResponse> response = userService.fetchUser(start, limit, startDate, endDate, username, role);
        return ResponseEntity.ok().body(AppResponse.<PaginateResponse<UserResponse>>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());

    }

    @PostMapping("deposit")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<AppResponse<BigDecimal>> purchaseProduct(@RequestParam BigDecimal amount) {
        BigDecimal response = userService.deposit(amount);
        return ResponseEntity.ok().body(AppResponse.<BigDecimal>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());
    }

    @PostMapping("reset")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<AppResponse<BigDecimal>> resetDeposit() {
        BigDecimal response = userService.resetDeposit();
        return ResponseEntity.ok().body(AppResponse.<BigDecimal>builder()
                .message(AppConstant.RestMessage.success)
                .status(HttpStatus.OK.value())
                .data(response)
                .build());
    }

}
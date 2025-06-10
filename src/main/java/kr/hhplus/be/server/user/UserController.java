package kr.hhplus.be.server.user;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public record ChargeRequest(
    BigDecimal price
  ) {}

  public record ChargeResponse(
    Long userId,
    BigDecimal charge
  ) {}

  @PostMapping("/{id}/chargeBalance")
  public ResponseEntity<ChargeResponse> chargeBalance(@PathVariable Long id, @RequestBody ChargeRequest request) {
    BigDecimal updatedBalance = userService.chargeBalance(id, request.price());
    return ResponseEntity.ok(new ChargeResponse(id, updatedBalance));
  }
}

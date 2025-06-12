package kr.hhplus.be.server.balance;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
public class BalanceController {
  private final BalanceService balanceService;

  public BalanceController(BalanceService balanceService) {
    this.balanceService = balanceService;
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
    BigDecimal updatedBalance = balanceService.chargeBalance(id, request.price());
    return ResponseEntity.ok(new ChargeResponse(id, updatedBalance));
  }
}

package kr.hhplus.be.server.balance;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BalanceControllerTest {
  private MockMvc mockMvc;
  private BalanceService balanceService;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    balanceService = Mockito.mock(BalanceService.class);
    BalanceController userController = new BalanceController(balanceService);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void testChargeBalance() throws Exception {
    Long userId = 1L;
    BigDecimal chargeAmount = new BigDecimal("2000");
    BigDecimal updatedBalance = new BigDecimal("3000");

    given(balanceService.chargeBalance(eq(userId), eq(chargeAmount))).willReturn(updatedBalance);

    BalanceController.ChargeRequest request = new BalanceController.ChargeRequest(chargeAmount);

    mockMvc.perform(post("/balance/{id}/chargeBalance", userId)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.userId").value(userId))
    .andExpect(jsonPath("$.charge").value(updatedBalance));
  }
}

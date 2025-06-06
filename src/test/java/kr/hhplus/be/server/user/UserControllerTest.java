package kr.hhplus.be.server.user;

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

public class UserControllerTest {
  private MockMvc mockMvc;
  private UserService userService;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    userService = Mockito.mock(UserService.class);
    UserController userController = new UserController(userService);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void testChargeBalance() throws Exception {
    Long userId = 1L;
    BigDecimal chargeAmount = new BigDecimal("2000");
    BigDecimal updatedBalance = new BigDecimal("3000");

    given(userService.chargeBalance(eq(userId), eq(chargeAmount))).willReturn(updatedBalance);

    UserController.ChargeRequest request = new UserController.ChargeRequest(chargeAmount);

    mockMvc.perform(post("/users/{id}/chargeBalance", userId)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(request)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.userId").value(userId))
    .andExpect(jsonPath("$.charge").value(updatedBalance));
  }
}

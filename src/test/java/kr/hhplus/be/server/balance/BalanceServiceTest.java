package kr.hhplus.be.server.balance;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import kr.hhplus.be.server.user.User;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BalanceServiceTest {
  @Mock
  private BalanceRepository balanceRepository;

  @InjectMocks
  private BalanceService balanceService;

  @Test
  public void chargeBalance() throws Exception {
    User mockUser = new User("Johb", new BigDecimal("1000"));
    Field idField = User.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(mockUser, 1L);

    when(balanceRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(mockUser));
    BigDecimal result = balanceService.chargeBalance(1L, new BigDecimal("2000"));
    assertThat(result).isEqualTo(new BigDecimal("3000"));    
  }
}
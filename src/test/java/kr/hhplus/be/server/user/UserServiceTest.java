package kr.hhplus.be.server.user;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  public void chargeBalance() throws Exception {
    User mockUser = new User("Johb", new BigDecimal("1000"));
    Field idField = User.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(mockUser, 1L);

    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
    BigDecimal result = userService.chargeBalance(1L, new BigDecimal("2000"));
    assertThat(result).isEqualTo(new BigDecimal("3000"));    
  }
}
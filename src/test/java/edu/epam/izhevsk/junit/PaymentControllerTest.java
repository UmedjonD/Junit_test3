package edu.epam.izhevsk.junit;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.Mockito.*;

public class PaymentControllerTest {
    private long userId;
    private long amount;
    @Mock
    private AccountService accountService;
    @Mock
    private DepositService depositService;
    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    @Test
    public void testPaymentController() throws InsufficientFundsException {
        MockitoAnnotations.initMocks(this);
        userId = 100L;
        amount = 50L;
        when(accountService.isUserAuthenticated(userId)).thenReturn(true);
        when(depositService.deposit(lt(amount), anyLong())).thenReturn("success");
        when(depositService.deposit(gt(userId), anyLong())).thenThrow(InsufficientFundsException.class);

    }
    @Test
    public void testSuccessDeposit() {
        assertDoesNotThrow(() ->  paymentController.deposit(amount, userId));
    }
    @Test
    public void testFailedDepozitWhenUserIsNot100L() {
        assertThrows(SecurityException.class, () -> paymentController.deposit(amount, 150L));
    }
    @Test
    public  void testFiledDepositIfAmountBig() {
        assertThrows(SecurityException.class, () -> paymentController.deposit(200L, 150L));
    }
}
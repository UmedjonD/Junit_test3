package edu.epam.izhevsk.junit;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.*;
import org.junit.gen5.api.BeforeEach;
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
        //verify(accountService, times(1)).isUserAuthenticated(userId);
    }
    @Test
    public void testFailedDepozitWhenUserIsNot100L() throws InsufficientFundsException {
        assertThrows(InsufficientFundsException.class, () -> paymentController.deposit(110L, 200L));
    }
    @Test
    public  void testFiledDepositIfAmountBig() throws InsufficientFundsException {
        paymentController.deposit(200L, userId);
    }
}
package Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import BankAccount.BankAccountDAO;
import BankAccount.BankAccountDTO;
import BankAccount.TransactionDTO;
import BankAccountService.BankAccount;

public class BankAccountTest {
	private String accountNumber = "1234567890";
	@Mock
	BankAccountDAO mockBankAccountDAO = mock(BankAccountDAO.class);

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		reset(mockBankAccountDAO);
		BankAccount.bankAccountDAO = mockBankAccountDAO;
	}

	// 1
	@Test
	public void testOpenAccount() {
		BankAccountDTO bankAccountDTO = BankAccount.openAccount(accountNumber);
		ArgumentCaptor<BankAccountDTO> argumentCaptor = ArgumentCaptor
				.forClass(BankAccountDTO.class);
		verify(mockBankAccountDAO, times(1)).save(argumentCaptor.capture());

		assertEquals(accountNumber, argumentCaptor.getValue()
				.getAccountNumber());
		assertTrue(0 == argumentCaptor.getValue().getBalance());
	}

	// 2
	@Test
	public void testPersistant() {
		ArgumentCaptor<String> accountNumberCaptor = ArgumentCaptor
				.forClass(String.class);
		BankAccountDTO bankAccountDTO = BankAccount.getAccount(accountNumber);
		verify(mockBankAccountDAO, times(1)).getAccount(
				accountNumberCaptor.capture());

		assertEquals(accountNumber, accountNumberCaptor.getValue());
	}

	// 3
	@Test
	public void testDeposit() {
		double amount = 100, DELTA = 1e-2;
		String description = "deposit 100";

		BankAccountDTO bankAccountDTO = new BankAccountDTO(accountNumber, 50);
		when(mockBankAccountDAO.getAccount(bankAccountDTO.getAccountNumber()))
				.thenReturn(bankAccountDTO);
		BankAccount.deposit(accountNumber, amount, description);

		ArgumentCaptor<BankAccountDTO> argument = ArgumentCaptor
				.forClass(BankAccountDTO.class);
		verify(mockBankAccountDAO, times(1)).save(argument.capture());
		assertEquals(150, argument.getValue().getBalance(), DELTA);
		assertEquals(accountNumber, argument.getValue().getAccountNumber());
	}
	
	// 4
	@Test
	public void testTimeStampDeposit() {
		String accountNumber = "1234567890";
		double amount = 100;
		long timestamp = 1000;
		String description = "deposit 100";

		BankAccountDTO bankAccount = new BankAccountDTO(accountNumber, 50);
		when(mockBankAccountDAO.getAccount(bankAccount.getAccountNumber()))
				.thenReturn(bankAccount);
		when(mockCalendar.getTimeInMillis()).thenReturn(timestamp);
		TransactionDTO transactionDTO = new TransactionDTO(accountNumber,
				timestamp, amount, description);
		
		BankAccount.deposit(accountNumber, amount, description);
		ArgumentCaptor<TransactionDTO> argumentCaptor = ArgumentCaptor
				.forClass(TransactionDTO.class);
		
		verify(mockTransactionDAO).createTransaction(argumentCaptor.capture());
		assertEquals(timestamp, argumentCaptor.getValue().getTimestamp());
	}

}

package Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import BankAccount.BankAccountDAO;
import BankAccount.BankAccountDTO;
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

		assertEquals(accountNumber, argumentCaptor.getValue().getAccountNumber());
		assertTrue(0 == argumentCaptor.getValue().getBalance());
	}
	
	// 2
	@Test
	public void testPersistant() {
		ArgumentCaptor<String> accountNumberCaptor = ArgumentCaptor.forClass(String.class);
		BankAccountDTO bankAccountDTO = BankAccount.getAccount(accountNumber);
		verify(mockBankAccountDAO, times(1)).getAccount(accountNumberCaptor.capture());
		
		assertEquals(accountNumber, accountNumberCaptor.getValue());
	}

}

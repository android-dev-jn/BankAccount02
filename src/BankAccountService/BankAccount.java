package BankAccountService;

import BankAccount.BankAccountDAO;
import BankAccount.BankAccountDTO;

public class BankAccount {

	public static BankAccountDAO bankAccountDAO;

	public static BankAccountDTO openAccount(String string) {
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDAO.save(bankAccountDTO);
		return null;
	}

	public static BankAccountDTO getAccount(String accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

}

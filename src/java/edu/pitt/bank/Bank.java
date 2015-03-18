package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import edu.pitt.utilities.*;
/**
 * Provides the foundation for the bank account application
 * @author bconner
 *
 */
public class Bank {
	private ArrayList<Account> accountList = new ArrayList<Account>();
	private ArrayList<Customer>customerList = new ArrayList<Customer>();
	
	/**
	 * Constructor. Loads all accounts and sets the owners to each account.
	 */
	public Bank(){
		loadAccounts();
		setAccountOwners();
	}
	
	/**
	 * Finds an account with the given account ID
	 * returns null if none exists.
	 * @param accountID
	 * @return Account, or <code>null</code> if not found 
	 */
	public Account findAccount(String accountID){
		Account acct = null;
		
		for(Account a: accountList){
			if(a.getAccountID().equals(accountID)){
				acct = new Account(accountID);
				break;
			}
		}		
		return acct;
	}
	
	/**
	 * Finds a customer with the given customer ID
	 * @param customerID
	 * @return Customer, or <code> null</code> if not found
	 */
	public Customer findCustomer(String customerID){
		Customer cust = null;
		
		for(Customer c: customerList){
			if(c.getCustomerID().equals(customerID)){
				cust = new Customer(customerID);
				break;
			}
		}		
		return cust;
	}
	
	/**
	 * Retrieves a list of a customers accounts.
	 * @param customerID
	 * @return ArrayList<Account>
	 */
	public ArrayList<Account> getCustomerAccounts(String customerID) {
		ArrayList<Account> foundAccounts = new ArrayList<Account>();
			
		for(Account a: accountList){
			for(Customer c: a.getAccountOwners()){
				if(c.getCustomerID().equals(customerID)){
					foundAccounts.add(a);
				}
			}
		}
		
		return foundAccounts;
	}
	
	
	public ArrayList<Account> getAccountList() {
		return accountList;
	}
	
	
	public ArrayList<Customer> getCustomerList() {
		return customerList;
	}
	
/////////////////////////////////////////////////
//				PRIVATE
/////////////////////////////////////////////////

	/**
	 * Retrieves all accounts from the database
	 */
	private void loadAccounts(){
		DbUtilities db = new MySqlUtilities();
		ResultSet rs = null;
		String sql = "SELECT * FROM account;";
			
		try{
			rs = db.getResultSet(sql);
			while (rs.next()){
				Account acct = new Account(rs.getString("accountID"));
				accountList.add(acct);
			}
		}catch(SQLException e){
			ErrorLogger.log("Error loading accounts records.");
			ErrorLogger.log(e.getMessage());
		}finally{
			db.silentClose(rs);
			db.silentClose();
		}
	}
	
	/**
	 * populates every account with the appropriate customer owners
	 */
	private void setAccountOwners(){
		DbUtilities db = new MySqlUtilities();
		ResultSet rs = null, custAcctRs = null;
		String customerSql = "SELECT customerID FROM customer;";
		String customerAccountSql = "SELECT * FROM customer_account ";
		
		try{
			//populate customerList
			rs = db.getResultSet(customerSql);
			while(rs.next()){
				Customer customer = new Customer(rs.getString("customerID"));
				customerList.add(customer);
			}
			
			//for each account find the customer
			for(Customer c: customerList){
				//complete SQL string for each customerID				
				custAcctRs = db.getResultSet(customerAccountSql + "WHERE fk_customerID = '" + c.getCustomerID() + "';");		
				while(custAcctRs.next()){
					
					// Search through the accountList for the matching account.
					// Note: multiple customers can belong to a single account if needed
					for(Account a: accountList){
						if(a.getAccountID().equals(custAcctRs.getString("fk_accountID"))){
							a.addAccountOwner(c);
						}
					}
				}
			}
			
		}catch(SQLException e){
			ErrorLogger.log("Error while adding account owners to accountList.");
			ErrorLogger.log(e.getMessage());
		}finally{
			db.silentClose(rs);
			db.silentClose(custAcctRs);
			db.silentClose();
		}
		
	}
}

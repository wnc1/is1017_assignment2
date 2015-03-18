package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.Vector;

import edu.pitt.utilities.*;
/**
 * Provides a data model for an account as well as additional methods.
 * @author bconner
 *
 */
public class Account {
	public static final String ACTIVE_ACCOUNT_STATUS = "active";
	private String accountID;
	private String type;
	private double balance;
	private double interestRate;
	private double penalty;
	private String status;
	private Date dateOpen;
	private ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	private ArrayList<Customer> accountOwners = new ArrayList<Customer>();
	private NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Constructs an instance of an existing account
	 * @param accountID
	 */
	public Account(String accountID){
		DbUtilities db = new MySqlUtilities();
		ResultSet rs = null, ts = null;
		
		String sql = "SELECT * FROM account "; 
		sql += "WHERE accountID = '" + accountID + "';";
		
		String transactionSql = "SELECT transactionID FROM transaction";
		transactionSql += " WHERE accountId = '" + accountID + "' order by transactionDate desc;";
		
		try {
			//retrieve account information
			rs = db.getResultSet(sql);
			while(rs.next()){
				this.accountID = rs.getString("accountID");
				this.type = rs.getString("type");
				this.balance = rs.getDouble("balance");
				this.interestRate = rs.getDouble("interestRate");
				this.penalty = rs.getDouble("penalty");
				this.status = rs.getString("status");
				this.dateOpen = rs.getDate("dateOpen");					
			}
			
			//retrieve transactions for the specified account
			ts = db.getResultSet(transactionSql);
			while(ts.next()){
				createTransaction(ts.getString("transactionID"));
			}
			
		} catch (SQLException e) {
			ErrorLogger.log("Unable to retrieve account.");
			ErrorLogger.log(e.getMessage());
		}finally{
			db.silentClose(rs);
			db.silentClose(ts);
			db.silentClose();
		}
		
	}
	
	/**
	 * Constructs a new account
	 * @param accountType
	 * @param initialBalance
	 */
	public Account(String accountType, double initialBalance){
		this.accountID = UUID.randomUUID().toString();
		this.type = accountType;
		this.balance = initialBalance;
		this.interestRate = 0;
		this.penalty = 0;
		this.status = "active";
		this.dateOpen = new Date();
		
		//build the SQL Insert statement
		String sql = "INSERT INTO account ";
		sql += "(accountID,type,balance,interestRate,penalty,status,dateOpen) ";
		sql += " VALUES ";
		sql += "('" + this.accountID + "', ";
		sql += "'" + this.type + "', ";
		sql += this.balance + ", ";
		sql += this.interestRate + ", ";
		sql += this.penalty + ", ";
		sql += "'" + this.status + "', ";
		sql += "CURDATE());";
		
		//insert into database
		DbUtilities db = new MySqlUtilities();	
		try{		
			db.executeQuery(sql);
		}catch(Exception e){
			ErrorLogger.log("Unable to create account.");
			ErrorLogger.log(e.getMessage());			
		}finally{
			db.silentClose();
		}
	}
	
	/**
	 * Creates a transaction and performs a withdrawal from the account.
	 * Transaction is time stamped and remaining balance updated.
	 * @param amount
	 */
	public void withdraw(double amount){
		if(status.equals(ACTIVE_ACCOUNT_STATUS)){
			this.balance -= amount;
			createTransaction(this.accountID, this.type, amount, this.balance);
			updateDatabaseAccountBalance();	
		}
	}
	
	/**
	 * Creates a transactions and performs a deposit.
	 * Transaction is time stamped and remaining balanced updated.
	 * @param amount
	 */
	public void deposit(double amount){
		if(status.equals(ACTIVE_ACCOUNT_STATUS)){
			this.balance += amount;
			createTransaction(this.accountID, this.type, amount, this.balance);
			updateDatabaseAccountBalance();			
		}
	}
	
	/**
	 * Retrieves the account ID of the account.
	 * @return String
	 */
	public String getAccountID(){
		return accountID;
	}
	
	/**
	 * Retrieves the remaining account balance.
	 * @return double
	 */
	public double getBalance(){
		return balance;
	}
	
	/**
	 * Retrieves the type of the account (Savings, Checking, etc.).
	 * @return String
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Retrieves the current interest rate on the account.
	 * @return double
	 */
	public double getInterestRate(){
		return interestRate;
	}
	
	/**
	 * Retrieves the penalty amount.
	 * @return double
	 */
	public double getPenalty(){
		return penalty;
	}

	/**
	 * Retrieves the status of the account (active, closed, frozen, etc.).
	 * @return String
	 */
	public String getStatus(){
		return status;
	}
	
	/**
	 * Retrieves the date the account was opened.
	 * @return Date
	 */
	public Date getDateOpen(){
		return dateOpen;
	}
	
	/**
	 * Retrieves a list of all transactions for the account.
	 * @return <code>ArrayList</code> of <code>Transaction</code> objects.
	 */
	public ArrayList<Transaction> getTransactions(){		
		return transactionList;
	}
	
	/**
	 * Adds a customer to be associated with the account.
	 * @param accountOwner
	 */
	public void addAccountOwner(Customer accountOwner){
		accountOwners.add(accountOwner);
	}
	
	/**
	 * Retrieves a list of all customers associated with the account.
	 * @return <code>ArrayList</code> of <code>Customer</code> objects.
	 */
	public ArrayList<Customer> getAccountOwners(){
		return accountOwners;
	}
	
	/**
	 * Retrieves a list of account transaction values in a vector format
	 * suitable for a <code>DefaultTableModel</code> object in a Swing application.
	 * Values returned include transaction type, date and time of the transaction
	 * and the dollar amount of the transaction.
	 * @return <code>Vector</code> of <code>Vector</code> objects.
	 */
	public Vector<Vector<Object>> renderTransactionVector(){
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		
		for(Transaction t:transactionList){
			Vector<Object> rows = new Vector<Object>();
			rows.add(t.getType());
			rows.add(dateFormat.format(t.getTransactionDate()));
			rows.add(moneyFormat.format(t.getAmount()));
			
			data.add(rows);
		}	
		
		return data;
	}
	
	@Override
	public String toString(){
		return getAccountID();
	}
	
	//////////////////////////////////////////////////////////////////////
	//  			PRIVATE
	//////////////////////////////////////////////////////////////////////
	private void updateDatabaseAccountBalance(){
		String sql = "UPDATE account SET balance = " + this.balance + " ";
		sql += "WHERE accountID = '" + this.accountID + "';";
		DbUtilities db = new MySqlUtilities();
		
		try{
			db.executeQuery(sql);		
		}catch(Exception e){
			ErrorLogger.log("Unable to update account balance.");
			ErrorLogger.log(e.getMessage());
		}finally{
			db.silentClose();
		}
	}
	
	/**
	 * Adds an existing transaction to the transactionList with the provided transactionID.
	 * @param transactionID
	 * @return Transaction
	 */
	private Transaction createTransaction(String transactionID){
		Transaction t = new Transaction(transactionID);
		transactionList.add(t);
		return t;
	}
	
	/**
	 * Creates a new transaction and adds it to the transaction list.
	 * @param accountID
	 * @param type
	 * @param amount
	 * @param balance
	 * @return <code>Transaction</code>
	 */
	private Transaction createTransaction(String accountID, String type, double amount, double balance){
		Transaction t = new Transaction(accountID, type, amount, balance);
		transactionList.add(t);
		return t;
	}
	
}

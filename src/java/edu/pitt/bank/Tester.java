package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.Date;

import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

public class Tester{
	public static void main(String[] args) {
		final NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		//test account
//		Account mine = new Account("00ae9c2a-5d43-11e3-94ef-97beef767f1d");
		//System.out.println(mine.getType());
		
		//test customer
//		Customer me = new Customer("01b9f986-5d41-11e3-94ef-97beef767f1d");
//		System.out.println(me.getFirstName());
//		System.out.println(me.getLastName());
		//Customer me = new Customer("Barnes", "Bucky", "555-34-5222", "bbarnes", 1234, "555 American Lane", "New York", "NY", 55555);
		
		//test security
//		Security session = new Security();
//		
//		for(String s:session.listUserGroups("9636abdc-5d40-11e3-94ef-97beef767f1d")){
//			System.out.println(s);
//		}
		
		//ERROR LOGGING: this is how to log errors

                Security security = new Security();
                Customer cust = security.validateLogin("bbarnes", 1234);
                System.out.println("Welcome " + cust.getFirstName());
//                ErrorLogger.log("Error log test: test for echo");
		
		
		//test bank
//		Bank b = new Bank();
//		Customer cust = new Customer("-29d82b0a8cca");
//		
//		for(Account a: b.getCustomerAccounts(cust.getCustomerID())){
//			System.out.println("add account : " + a.getAccountID() + " for " + cust.getFirstName());
//			
//		}
		
		//test no sql version
//		System.out.println(b.findCustomer("9636abdc-5d40-11e3-94ef-97beef767f1d").getLoginName());
//		try{
//			
//			for(Account a: b.getAccountList()){
//				
//				for(Customer c: a.getAccountOwners()){
//					if(c.getCustomerID().equals(cust.getCustomerID())){
//						System.out.println("add account : " + a.getAccountID() + " for " + cust.getFirstName());
//					}
//				}
//			}
//			
//		}catch(NullPointerException e){
//			ErrorLogger.log("Account missing or not found for customerID " + cust.getCustomerID());
//			ErrorLogger.log(e.getMessage());
//		}
//		
		
		
//		try {
//			
//			for(Account a: b.getCustomerAccounts("ebcbe67a-5d40-11e3-94ef-97beef767f1d")){
//				System.out.println(a.getType());
//			}
//			
//		} catch (NullPointerException e) {
//			System.out.println("no accounts found.");
////			e.printStackTrace();
//		}
		
		//test all bank methods!!!!!!!!!!!!!!!!!!!!!!!!
		//testing accountsList and customerList
//		System.out.println("List of accounts:");
//		System.out.println("-------------------------");
//		
//		for(Account a: b.getAccountList()){
//			System.out.print("Owner(s) : ");
//			for(Customer c: a.getAccountOwners()){
//				System.out.print(c.getFirstName() + " ");
//				System.out.print(c.getLastName() + ", ");
//			}
//
//			System.out.print(moneyFormat.format(a.getBalance()));
//			System.out.println("");
//		}
//		System.out.println("-------------------------");
//
//		System.out.println("List of customers:");
//		System.out.println("-------------------------");
//		for(Customer c: b.getCustomerList()){
//			System.out.println(c.getLoginName());
//		}
//		System.out.println("-------------------------");

		
	}
}
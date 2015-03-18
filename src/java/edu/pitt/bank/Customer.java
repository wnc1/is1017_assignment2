package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import edu.pitt.utilities.*;

/**
 * Provides the data model for a customer as well as additional methods.
 * @author bconner
 *
 */
public class Customer {

	private String customerID;
	private String firstName;
	private String lastName;
	private String ssn;
	private String streetAddress;
	private String city;
	private String state;
	private int zip;
	private String loginName;
	private int pin;
	
	/**
	 * Constructor
	 * Retrieves an existing customer object with the given customerID.
	 * @param customerID
	 */
	public Customer(String customerID){
		DbUtilities db = new MySqlUtilities();
		ResultSet rs = null;
		
		String sql = "SELECT * FROM customer "; 
		sql += "WHERE customerID = '" + customerID + "';";

		try{
			 rs = db.getResultSet(sql);
			while(rs.next()){
				this.customerID = customerID;
				this.firstName = rs.getString("firstName");
				this.lastName = rs.getString("lastName");
				this.ssn = rs.getString("ssn");
				this.streetAddress = rs.getString("streetAddress");
				this.city = rs.getString("city");
				this.state = rs.getString("state");
				this.zip = rs.getInt("zip");
				this.loginName = rs.getString("loginName");
				this.pin = rs.getInt("pin");
			}	
		} catch (SQLException e) {
			ErrorLogger.log("Unable to retrieve customer object.");
			ErrorLogger.log(e.getMessage());
		}finally{
			db.silentClose(rs);
			db.silentClose();
		}
	}
	
	/**
	 * Constructor
	 * Creates a new customer object with given parameters.
	 * @param lastName
	 * @param firstName
	 * @param ssn
	 * @param loginName
	 * @param pin
	 * @param streetAddress
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Customer(String lastName, String firstName, String ssn, String loginName, int pin, String streetAddress, String city, String state, int zip){
		this.customerID = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.lastName = lastName;
		this.ssn = ssn;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.loginName = loginName;
		this.pin = pin;
		
		String sql = "INSERT INTO customer ";
		sql += "(customerID, firstName, lastName, ssn, streetAddress, city, state, zip, loginName, pin) ";
		sql += " VALUES ";
		sql += "('" + this.customerID + "', ";
		sql += "'" + this.firstName + "', ";
		sql += "'" + this.lastName + "', ";
		sql += "'" + this.ssn + "', ";
		sql += "'" + this.streetAddress + "', ";
		sql += "'" + this.city + "', ";
		sql += "'" + this.state + "', ";
		sql += this.zip + ", ";
		sql += "'" + this.loginName + "', ";
		sql += this.pin + ");";
				
		DbUtilities db = new MySqlUtilities();
		
		try{
			db.executeQuery(sql);			
		}catch(Exception e){
			ErrorLogger.log("Unable create new customer.");
			ErrorLogger.log(e.getMessage());
		}finally{
			db.silentClose();			
		}
	}
	
	/**
	 * Retrieves the customerID
	 * @return String
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * Sets the customerID
	 * @param customerID
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * Retrieves the first name of the customer.
	 * @return String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the customer.
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Retrieves the last name of the customer.
	 * @return String
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the customer.
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Retrieves the social security number of the customer.
	 * @return String
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * Sets the social security number of the customer.
	 * @param ssn
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * Retrieves the street address of the customer.
	 * @return String
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * Sets the street address of the customer.
	 * @param streetAddress
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * Retrieves the city of the customer.
	 * @return String
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city of the customer.
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Retrieves the state of the customer
	 * @return String
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state of the customer.
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Retrieves the zip code of the customer.
	 * @return int
	 */
	public int getZip() {
		return zip;
	}

	/**
	 * Sets the zip code of the customer.
	 * @param zip
	 */
	public void setZip(int zip) {
		this.zip = zip;
	}

	/**
	 * Retrieves the login name of the customer.
	 * @return String
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * Sets the login name of the customer.
	 * @param loginName
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * Retrieves the numeric pin of the customer
	 * @return int
	 */
	public int getPin() {
		return pin;
	}

	/**
	 * Sets a numeric pin number of the customer.
	 * @param pin
	 */
	public void setPin(int pin) {
		this.pin = pin;
	}
}

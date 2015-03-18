package edu.pitt.bank;

import edu.pitt.utilities.DbUtilities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

/**
 * Provides login functionality and a model for user group permissions.
 * @author bconner
 *
 */
public class Security {
	private String userID;
	private ArrayList<String> userGroups = new ArrayList<String>();
	
	/**
	 * Constructor
	 */
	public Security(){	
		
	}
	
	/**
	 * Validates customer login name and pin.
	 * @param loginName
	 * @param pin
	 * @return Customer or <code>null</code> if not found.
	 */
	public Customer validateLogin(String loginName, int pin){
		String sql = "SELECT * FROM customer WHERE loginName= '" + loginName + "' AND pin=" + pin +";";
		Customer cust = null;
		ResultSet rs  = null;	
		DbUtilities db = new MySqlUtilities();
		
		try{
                    rs = db.getResultSet(sql);
                    while (rs.next()) {
                        cust = new Customer(rs.getString("customerID"));
                    }
		}catch (SQLException e){
			ErrorLogger.log("Failed login attempt.");
			ErrorLogger.log(e.getMessage());
		}finally{
			db.silentClose(rs);
			db.silentClose();
		}

		return cust;
	}
	
	/**
	 * Retrieves the group permissions for a given user.
	 * @param userID
	 * @return <code>ArrayList</code> of <code>Strings<code>
	 */
	public ArrayList<String> listUserGroups(String userID){
		ResultSet rs = null;
		DbUtilities db = new MySqlUtilities();
		String sql = "SELECT groupName FROM groups g ";
			sql+= "JOIN user_permissions up ON g.groupID=up.groupID ";
			sql+="JOIN customer ON customerID=up.groupOrUserID ";
			sql+= "WHERE customerid='" + userID + "';";
			
		try{
			rs = db.getResultSet(sql);
			while(rs.next()){
				userGroups.add(rs.getString("groupName"));
			}
		}catch (SQLException e){	
			ErrorLogger.log("Unable to obtain user group permissions.");
			ErrorLogger.log(e.getMessage());
		}finally{
			db.silentClose(rs);
			db.silentClose();
		}
			
		return userGroups;
	}
}

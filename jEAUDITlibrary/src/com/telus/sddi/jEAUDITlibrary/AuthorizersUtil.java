package com.telus.sddi.jEAUDITlibrary;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;

/**
 * A Utility class related to the Authorizers class. Contains useful public static methods
 * @author fbegin1
 *
 */
public class AuthorizersUtil {


	private static String mySelectStatement = "SELECT idAuthorizers,AuthorizerType,AuthorizerEmpID,AuthorizerFirstName,AuthorizerLastName,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Audit_idAudi FROM Authorizers";

	/**
	* A method that creates a list of all records
	* @return ArrayList of all records
	*/
	public static ArrayList<Authorizers> list(String databasePropertiesFile) {
		ArrayList<Authorizers> myRecords = new ArrayList<Authorizers>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement;
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Authorizers myCurrentRecord = new Authorizers(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecords;
	}

	
	/**
	 * A method that mass creates an ArrayList of object
	 * @return the idAudit of the newly created audit
	 */
	public static void massLoad(ArrayList<Authorizers> recordsToMassLoad, String databasePropertiesFile) {
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			
			myDB.query = "";		
			myDB.query 			= "INSERT INTO Authorizers (AuthorizerType,AuthorizerEmpID,AuthorizerFirstName,AuthorizerLastName,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Audit_idAudit) VALUES (?,?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query);
			
			int i = 1;
			
			/*
			 * Set parameters for the massive query
			 */
			for (Authorizers myCurrRecord : recordsToMassLoad ) {
				myDB.stmt.setString(1, myCurrRecord.getAuthorizerType());
				myDB.stmt.setInt(2, myCurrRecord.getAuthorizerEmpID());
				myDB.stmt.setString(3, myCurrRecord.getAuthorizerFirstName());
				myDB.stmt.setString(4, myCurrRecord.getAuthorizerLastName());
				myDB.stmt.setDate(5, myCurrRecord.getCreatedDateTime());
				myDB.stmt.setString(6, myCurrRecord.getCreatedBy());
				myDB.stmt.setDate(7, myCurrRecord.getLastUpdatedDateTime());
				myDB.stmt.setString(8, myCurrRecord.getLastUpdatedBy());
				myDB.stmt.setInt(9, myCurrRecord.getAudit_idAudit());
				myDB.stmt.addBatch();
				
				if ( i % 2500 == 0) {
					myDB.stmt.executeBatch(); // Execute every 2500 items.
	            }
				i++;
				
			}

			myDB.stmt.executeBatch();


			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	
	}

	
	
}

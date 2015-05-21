package com.telus.sddi.jEAUDITlibrary;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;

/**
 * A Utility class related to the Entities class. Contains useful public static methods
 * @author fbegin1
 *
 */
public class EntitiesUtil {


	private static String mySelectStatement = "SELECT idEntities,PrimaryKey,UDEAprimaryFieldsValues,UDEAsecondaryFieldsValues,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Audit_idAudi FROM Entities";

	/**
	* A method that creates a list of all records
	* @return ArrayList of all records
	*/
	public static ArrayList<Entities> list(String databasePropertiesFile) {
		ArrayList<Entities> myRecords = new ArrayList<Entities>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement;
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Entities myCurrentRecord = new Entities(myDB.rs);
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
	public static void massLoad(ArrayList<Entities> recordsToMassLoad, String databasePropertiesFile) {
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			
			myDB.query = "";		
			myDB.query 			= "INSERT INTO Entities (PrimaryKey,UDEAprimaryFieldsValues,UDEAsecondaryFieldsValues,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Audit_idAudit) VALUES (?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query);
			
			int i = 1;
			
			/*
			 * Set parameters for the massive query
			 */
			for (Entities myCurrRecord : recordsToMassLoad ) {
				myDB.stmt.setString(1, myCurrRecord.getPrimaryKey());
				myDB.stmt.setString(2, myCurrRecord.getUDEAprimaryFieldsValues());
				myDB.stmt.setString(3, myCurrRecord.getUDEAsecondaryFieldsValues());
				myDB.stmt.setDate(4, myCurrRecord.getCreatedDateTime());
				myDB.stmt.setString(5, myCurrRecord.getCreatedBy());
				myDB.stmt.setDate(6, myCurrRecord.getLastUpdatedDateTime());
				myDB.stmt.setString(7, myCurrRecord.getLastUpdatedBy());
				myDB.stmt.setInt(8, myCurrRecord.getAudit_idAudit());
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

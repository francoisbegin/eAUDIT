package com.telus.sddi.jEAUDITlibraryUtil;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;
import com.telus.sddi.jEAUDITlibrary.Entitlement;

public class EntitlementUtil {

	private static String mySelectStatement = "SELECT idEntitlement,TotalNumberOfEntities,Entitlement, CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,AuditTypeReference_idAuditTypeReference FROM Entitlement";


	
	/**
	 * A method that bulks create an ArrayList of object
	 * This methods bulk creates up to 2500 records at a time - hard-coded in the method but easily modified 
	 * @param recordsToMassLoad
	 */
	public static void bulkCreate(ArrayList<Entitlement> recordsToBulkCreate, String databasePropertiesFile) {
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			
			myDB.query = "";		
			myDB.query 			=  "INSERT INTO Entitlement (idEntitlement, TotalNumberOfEntities,Entitlement,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,AuditTypeReference_idAuditTypeReference) VALUES (?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query);
			
			int i = 1;
			
			/*
			 * Set parameters for the massive query
			 */
			for (Entitlement myCurrRecord : recordsToBulkCreate ) {			
				myDB.stmt.setInt(1, myCurrRecord.getidEntitlement());
				myDB.stmt.setInt(2, myCurrRecord.getTotalNumberOfEntities());
				myDB.stmt.setString(3, myCurrRecord.getEntitlement());
				myDB.stmt.setDate(4, myCurrRecord.getCreatedDateTime());
				myDB.stmt.setString(5, myCurrRecord.getCreatedBy());
				myDB.stmt.setDate(6, myCurrRecord.getLastUpdatedDateTime());
				myDB.stmt.setString(7, myCurrRecord.getLastUpdatedBy());
				myDB.stmt.setInt(8, myCurrRecord.getAuditTypeReference_idAuditTypeReference());
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
	

	/**
	* A method that creates a list of all records
	* @return ArrayList of all records
	*/
	public static ArrayList<Entitlement> list(String databasePropertiesFile) {
		ArrayList<Entitlement> myRecords = new ArrayList<Entitlement>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement; 
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Entitlement myCurrentRecord = new Entitlement(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecords;
	}
	
	
	/**
	* A method that retrieves a specific Entitlement record
	* @return 
	*/
	public static Entitlement get(int idEntitlement, String databasePropertiesFile) {
		Entitlement myRecord = new Entitlement();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement + " WHERE idEntitlement = ?";
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.setInt(1, idEntitlement);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			if (myDB.rs.next()) {
				myRecord = new Entitlement(myDB.rs);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecord;
	}
	
	
	
  	/**
     * A method that drops all entitlement of a certain idAuditTypeReference from the Entitlements table
  	 */
        public static void deleteFromEntitlementByIDauditTypeReference(int idAuditTypeReference, String databasePropertiesFile) {

        	DBConnector myDB = new DBConnector(databasePropertiesFile);
        	myDB.connect();
        	try {
        		myDB.query = "DELETE FROM Entitlement WHERE AuditTypeReference_idAuditTypeReference = ? ";
        		myDB.stmt = myDB.conn.prepareStatement(myDB.query);
        		myDB.stmt.setInt(1, idAuditTypeReference);
        		
        		myDB.stmt.executeUpdate();
        		myDB.close();
        	} catch (Exception e) {
        		System.out.println(e.toString());
        	}

       	}
	
	/**
	 * A method that creates a list of all records associated with a sepcific AuditTypeReference
	 * @param auditTypeReference_idAuditTypeReference
	 * @return
	 */
	public static ArrayList<Entitlement> listByAuditTypeReference(int auditTypeReference_idAuditTypeReference, String databasePropertiesFile) {
		ArrayList<Entitlement> myRecords = new ArrayList<Entitlement>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement + " WHERE AuditTypeReference_idAuditTypeReference=?";
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.setInt(1, auditTypeReference_idAuditTypeReference );
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Entitlement myCurrentRecord = new Entitlement(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecords;
	}


	/**
	 * A method that returns the highest idEntitlement value in table Entitlement. Useful to find top value so we can pre-assign new values at load time
	 * @param auditTypeReference_idAuditTypeReference
	 * @return
	 */
	public static int findMaxIDentitlementValue(String databasePropertiesFile) {
		int maxValue = 0;
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = "select MAX(idEntitlement) as maxIDentitlement from Entitlement";
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			if (myDB.rs.next()) {
				maxValue = myDB.rs.getInt(1);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return maxValue;
	}

}
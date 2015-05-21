package com.telus.sddi.jEAUDITlibrary;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;

/**
 * A Utility class related to the Audit class. Contains useful public static methods
 * @author fbegin1
 *
 */
public class AuditUtil {
	
	private static String mySelectStatement = "SELECT idAudit,TotalNumberOfEntities,Entitlement,SystemLoaderAuth, CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,AuditTypeReference_idAuditTypeReference FROM Audit";

	
	/**
	 * A method that mass creates an ArrayList of object
	 * @return the idAudit of the newly created audit
	 */
	public static void massLoad(ArrayList<Audit> recordsToMassLoad,String databasePropertiesFile) {
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			
			myDB.query = "";		
			myDB.query 			=  "INSERT INTO Audit (idAudit, TotalNumberOfEntities,Entitlement,SystemLoaderAuth,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,AuditTypeReference_idAuditTypeReference) VALUES (?,?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query);
			
			int i = 1;
			
			/*
			 * Set parameters for the massive query
			 */
			for (Audit myCurrRecord : recordsToMassLoad ) {
				myDB.stmt.setInt(1, myCurrRecord.getidAudit());
				myDB.stmt.setInt(2, myCurrRecord.getTotalNumberOfEntities());
				myDB.stmt.setString(3, myCurrRecord.getEntitlement());
				myDB.stmt.setString(4, myCurrRecord.getSystemLoaderAuth());
				myDB.stmt.setDate(5, myCurrRecord.getCreatedDateTime());
				myDB.stmt.setString(6, myCurrRecord.getCreatedBy());
				myDB.stmt.setDate(7, myCurrRecord.getLastUpdatedDateTime());
				myDB.stmt.setString(8, myCurrRecord.getLastUpdatedBy());
				myDB.stmt.setInt(9, myCurrRecord.getAuditTypeReference_idAuditTypeReference());
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
	public static ArrayList<Audit> list(String databasePropertiesFile) {
		ArrayList<Audit> myRecords = new ArrayList<Audit>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement; 
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Audit myCurrentRecord = new Audit(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecords;
	}
	
	
	/**
	 * A method that creates a list of all records associated with a specific AuditTypeReference
	 * @param auditTypeReference_idAuditTypeReference
	 * @return
	 */
	public static ArrayList<Audit> listByAuditTypeReference(int auditTypeReference_idAuditTypeReference, String databasePropertiesFile) {
		ArrayList<Audit> myRecords = new ArrayList<Audit>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement + " WHERE AuditTypeReference_idAuditTypeReference=?";
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.setInt(1, auditTypeReference_idAuditTypeReference );
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Audit myCurrentRecord = new Audit(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecords;
	}


	/**
	 * A method that returns the highest idAudit value in table Audit. Useful to find top value so we can pre-assign new values at load time
	 * @param auditTypeReference_idAuditTypeReference
	 * @return
	 */
	public static int findMaxIDauditValue(String databasePropertiesFile) {
		int maxValue = 0;
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = "select MAX(idAudit) as maxIDaudit from Audit";
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

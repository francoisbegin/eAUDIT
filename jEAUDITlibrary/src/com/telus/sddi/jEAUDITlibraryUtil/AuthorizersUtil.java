package com.telus.sddi.jEAUDITlibraryUtil;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;
import com.telus.sddi.jEAUDITlibrary.Authorizers;


public class AuthorizersUtil {



	private static String mySelectStatement = "SELECT idAuthorizers,AuthorizerType,AuthorizerEmpID,AuthorizerFirstName,AuthorizerLastName,AuthorizerManagerEmpID,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Entitlement_idEntitlement FROM Authorizers";



	/**
	 * A method that bulks create records
	 * This methods bulk creates up to 2500 records at a time - hard-coded in the method but easily modified 
	 * @param recordsToBulkCreate
	 */
	public static void bulkCreate(ArrayList<Authorizers> recordsToBulkCreate, String databasePropertiesFile) {
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		try {
			myDB.connect();
			
			myDB.query = "";		
			myDB.query 			= "INSERT INTO Authorizers (AuthorizerType,AuthorizerEmpID,AuthorizerFirstName,AuthorizerLastName,AuthorizerManagerEmpID,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Entitlement_idEntitlement) VALUES (?,?,?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query);
			
			int i = 1;
			
			/*
			 * Set parameters for the massive query
			 */
			for (Authorizers myCurrRecord : recordsToBulkCreate ) {
				myDB.stmt.setString(1, myCurrRecord.getAuthorizerType());
				myDB.stmt.setInt(2, myCurrRecord.getAuthorizerEmpID());
				myDB.stmt.setString(3, myCurrRecord.getAuthorizerFirstName());
				myDB.stmt.setString(4, myCurrRecord.getAuthorizerLastName());
				myDB.stmt.setInt(5, myCurrRecord.getAuthorizerManagerEmpID());
				myDB.stmt.setDate(6, myCurrRecord.getCreatedDateTime());
				myDB.stmt.setString(7, myCurrRecord.getCreatedBy());
				myDB.stmt.setDate(8, myCurrRecord.getLastUpdatedDateTime());
				myDB.stmt.setString(9, myCurrRecord.getLastUpdatedBy());
				myDB.stmt.setInt(10, myCurrRecord.getEntitlement_idEntitlement());
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
	 * A method that bulks update records. 
	 * Note that fields CreatedDateTime and CreatedBy are omitted from the update for obvious reasons, as well as LastUpdatedDateTime which is handled by a TRIGGER in the table
	 * This methods bulk updates up to 2500 records at a time - hard-coded in the method but easily modified 
	 * @param recordsToBulkUpdate
	 */
	public static void bulkUpdate(ArrayList<Authorizers> recordsToBulkUpdate, String databasePropertiesFile) {
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		try {
			myDB.connect();
			
			myDB.query = "";		
			myDB.query 			= "UPDATE Authorizers SET AuthorizerType=?, AuthorizerEmpID=?, AuthorizerFirstName=?, AuthorizerLastName=?, AuthorizerManagerEmpID=?,LastUpdatedBy=?,Entitlement_idEntitlement=? WHERE idAuthorizers = ?;";
						
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query);
			
			int i = 1;
			
			/*
			 * Set parameters for the massive query
			 */
			for (Authorizers myCurrRecord : recordsToBulkUpdate ) {
				myDB.stmt.setString(1, myCurrRecord.getAuthorizerType());
				myDB.stmt.setInt(2, myCurrRecord.getAuthorizerEmpID());
				myDB.stmt.setString(3, myCurrRecord.getAuthorizerFirstName());
				myDB.stmt.setString(4, myCurrRecord.getAuthorizerLastName());
				myDB.stmt.setInt(5, myCurrRecord.getAuthorizerManagerEmpID());
				myDB.stmt.setString(6, myCurrRecord.getLastUpdatedBy());
				myDB.stmt.setInt(7, myCurrRecord.getEntitlement_idEntitlement());
				myDB.stmt.setInt(8, myCurrRecord.getidAuthorizers());
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
     * A method that drops all Super authorizers from the Authorizers table
  	 */
        public static void deleteFromAuthorizersByIDauditTypeReference(int idAuditTypeReference, String databasePropertiesFile) {

        	DBConnector myDB = new DBConnector(databasePropertiesFile);
        	myDB.connect();
        	try {
        		myDB.query = "DELETE FROM Authorizers WHERE Entitlement_idEntitlement IN ( "+
        						"SELECT idEntitlement FROM Entitlement AS e "+
        						"LEFT JOIN AuditTypeReference AS r ON (e.AuditTypeReference_idAuditTypeReference = r.idAuditTypeReference) "+
        							"WHERE r.idAuditTypeReference = ? ) "; 
        		myDB.stmt = myDB.conn.prepareStatement(myDB.query);
        		myDB.stmt.setInt(1, idAuditTypeReference);
        		
        		myDB.stmt.executeUpdate();
        		myDB.close();
        	} catch (Exception e) {
        		System.out.println(e.toString());
        	}

       	}
        
        /**
         * A method that drops all authorizers from the Authorizers table based on a sepcific idAuditTypeReference
         * @param idAuditTypeReference
         * @return
         */
        public static ArrayList<Authorizers> listByIDauditTypeReference(int idAuditTypeReference, String databasePropertiesFile) {

        	ArrayList<Authorizers> myRecords = new ArrayList<Authorizers>();
        	DBConnector myDB = new DBConnector(databasePropertiesFile);
        	myDB.connect();
        	try {
        		myDB.query = mySelectStatement + " WHERE Entitlement_idEntitlement IN ( "+
        				"SELECT idEntitlement FROM Entitlement AS e "+
        				"LEFT JOIN AuditTypeReference AS r ON (e.AuditTypeReference_idAuditTypeReference = r.idAuditTypeReference) "+
        				"WHERE r.idAuditTypeReference = ? ) "; 
        		myDB.stmt = myDB.conn.prepareStatement(myDB.query);
        		myDB.stmt.setInt(1, idAuditTypeReference);
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
	* A method that drops all Super authorizers from the Authorizers table
	*/
	public static void deleteFromAuthorizersByTypeSuper(String databasePropertiesFile) {

		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = "DELETE FROM Authorizers WHERE AuthorizerType = 'Super' "; 
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeUpdate();
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}
	
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


}
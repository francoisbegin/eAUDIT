package com.telus.sddi.jEAUDITlibrary;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


public class EntitiesAuditResponsesUtil {



	private static String mySelectStatement = "SELECT Entities_idEntities,RecordedResponse,AuditHistoryKey,LastUpdatedDateTime,LastUpdatedB FROM EntitiesAuditResponses";


	/**
	* A method that creates a list of all records
	* @return ArrayList of all records
	*/
	public static ArrayList<EntitiesAuditResponses> list(String databasePropertiesFile) {
		ArrayList<EntitiesAuditResponses> myRecords = new ArrayList<EntitiesAuditResponses>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement;
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				EntitiesAuditResponses myCurrentRecord = new EntitiesAuditResponses(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecords;
	}

}

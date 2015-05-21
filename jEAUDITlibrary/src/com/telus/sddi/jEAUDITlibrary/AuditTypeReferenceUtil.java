package com.telus.sddi.jEAUDITlibrary;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;

/**
 * A Utility class related to the AuditTypeReference class. Contains useful public static methods
 * @author fbegin1
 *
 */
public class AuditTypeReferenceUtil {
	
	private static String mySelectStatement = "SELECT idAuditTypeReference,AuditName,AuditDescription,AuditStart,AuditEnd,AuditInstructionsEN,AuditInstructionsFR,UDEAprimaryFieldsEN,UDEAprimaryFieldsFR,UDEAsecondaryFieldsEN,UDEAsecondaryFieldsFR,AuditByManager,DataLoadType,eMACyclesTimelineID,UseEmac,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedB FROM AuditTypeReference";

	/**
	* A method that creates a list of all records
	* @return ArrayList of all records
	*/
	public static ArrayList<AuditTypeReference> list(String databasePropertiesFile) {
		ArrayList<AuditTypeReference> myRecords = new ArrayList<AuditTypeReference>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement;
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				AuditTypeReference myCurrentRecord = new AuditTypeReference(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			// Do something
		}
		return myRecords;
	}
}

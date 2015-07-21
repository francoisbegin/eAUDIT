package com.telus.sddi.jEAUDITlibraryUtil;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;
import com.telus.sddi.jEAUDITlibrary.AuditTypeReference;


public class AuditTypeReferenceUtil {



	private static String mySelectStatement = "SELECT idAuditTypeReference,AuditName,AuditDescription,AuditStart,AuditEnd,AuditInstructionsEN,AuditInstructionsFR,UDEAprimaryFieldsEN,UDEAprimaryFieldsFR,UDEAsecondaryFieldsEN,UDEAsecondaryFieldsFR,AuditByManager,DataLoadType,eMACyclesTimelineID,EntityTransferRule,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy FROM AuditTypeReference";


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
			System.out.println(e.toString());
		}
		return myRecords;
	}
	
	/**
	* A method that retrieves a specific AuditTypeReference record
	* @return 
	*/
	public static AuditTypeReference get(int idAuditTypeReference, String databasePropertiesFile) {
		AuditTypeReference myRecord = new AuditTypeReference();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement + " WHERE idAuditTypeReference = ?";
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.setInt(1, idAuditTypeReference);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			if (myDB.rs.next()) {
				myRecord = new AuditTypeReference(myDB.rs);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecord;
	}

}
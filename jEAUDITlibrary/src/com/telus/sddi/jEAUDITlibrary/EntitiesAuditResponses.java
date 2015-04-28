package com.telus.sddi.jEAUDITlibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


public class EntitiesAuditResponses {

	private int 		entities_idEntities;
	private String 		recordedResponse;
	private String 		auditHistoryKey;
	private Date 		lastUpdatedDateTime;
	private String 		lastUpdatedBy;


	private static String mySelectStatement = "SELECT Entities_idEntities,RecordedResponse,AuditHistoryKey,LastUpdatedDateTime,LastUpdatedB FROM EntitiesAuditResponses";


	/**
	 * Default contructor
	 * @param entities_idEntities
	 * @param recordedResponse
	 * @param auditHistoryKey
	 * @param lastUpdatedDateTime
	 * @param lastUpdatedBy
	 */
	public EntitiesAuditResponses(
			int entities_idEntities,
			String recordedResponse,
			String auditHistoryKey,
			Date lastUpdatedDateTime,
			String lastUpdatedBy) { 

		super();

		this.entities_idEntities = entities_idEntities;
		this.recordedResponse = recordedResponse;
		this.auditHistoryKey = auditHistoryKey;
		this.lastUpdatedDateTime = lastUpdatedDateTime;
		this.lastUpdatedBy = lastUpdatedBy;

	}


	/**
	* A constructor that uses a Result Set to build a basic object
	* @param myResultSet
	*/
	public EntitiesAuditResponses(ResultSet myResultSet) {
		buildBasicObjectFromResultSet(myResultSet);
	}


	/**
	* A method that takes a ResultSet and uses it to populate a basic object.
	* @param rs The result set from which to build the object
	*/
	public void buildBasicObjectFromResultSet(ResultSet rs) {
		try {
			setEntities_idEntities(rs.getInt("Entities_idEntities"));
			setRecordedResponse(rs.getString("RecordedResponse"));
			setAuditHistoryKey(rs.getString("AuditHistoryKey"));
			setLastUpdatedDateTime(rs.getDate("LastUpdatedDateTime"));
			setLastUpdatedBy(rs.getString("LastUpdatedBy"));
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}




	/**
	* A method that creates a new record
	* @return the id key of the newly created record
	*/
	public int create(String databasePropertiesFile) {
		int key = -1;
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query 			= "INSERT INTO EntitiesAuditResponses (Entities_idEntities,RecordedResponse,AuditHistoryKey,LastUpdatedDateTime,LastUpdatedBy) VALUES (?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query, Statement.RETURN_GENERATED_KEYS );	
			myDB.stmt.setInt(1, getEntities_idEntities());
			myDB.stmt.setString(2, getRecordedResponse());
			myDB.stmt.setString(3, getAuditHistoryKey());
			myDB.stmt.setDate(4, getLastUpdatedDateTime());
			myDB.stmt.setString(5, getLastUpdatedBy());
			myDB.stmt.executeUpdate();
			ResultSet keys = myDB.stmt.getGeneratedKeys();
			keys.next(); 
			key = keys.getInt(1);
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return key;
	}


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



	public int getEntities_idEntities() {
		return entities_idEntities;
	}
	public void setEntities_idEntities(int entities_idEntities) {
		this.entities_idEntities = entities_idEntities;
	}
	public String getRecordedResponse() {
		return recordedResponse;
	}
	public void setRecordedResponse(String recordedResponse) {
		this.recordedResponse = recordedResponse;
	}
	public String getAuditHistoryKey() {
		return auditHistoryKey;
	}
	public void setAuditHistoryKey(String auditHistoryKey) {
		this.auditHistoryKey = auditHistoryKey;
	}
	public Date getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}
	public void setLastUpdatedDateTime(Date lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}

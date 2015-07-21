package com.telus.sddi.jEAUDITlibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


public class Entitlement {

	private int 		idEntitlement;
	private int 		totalNumberOfEntities;
	private String 		entitlement;
	private Date 		createdDateTime;
	private String 		createdBy;
	private Date 		lastUpdatedDateTime;
	private String 		lastUpdatedBy;
	private int 		auditTypeReference_idAuditTypeReference;


	/**
	 * Default contructor
	 * @param idEntitlement
	 * @param totalNumberOfEntities
	 * @param entitlement
	 * @param createdDateTime
	 * @param createdBy
	 * @param lastUpdatedDateTime
	 * @param lastUpdatedBy
	 * @param auditTypeReference_idAuditTypeReference
	 */
	public Entitlement(
			int idEntitlement,
			int totalNumberOfEntities,
			String entitlement,
			Date createdDateTime,
			String createdBy,
			Date lastUpdatedDateTime,
			String lastUpdatedBy,
			int auditTypeReference_idAuditTypeReference) { 

		super();

		this.idEntitlement 								= idEntitlement;
		this.totalNumberOfEntities 						= totalNumberOfEntities;
		this.entitlement 								= entitlement;
		this.createdDateTime 							= createdDateTime;
		this.createdBy 									= createdBy;
		this.lastUpdatedDateTime 						= lastUpdatedDateTime;
		this.lastUpdatedBy 								= lastUpdatedBy;
		this.auditTypeReference_idAuditTypeReference 	= auditTypeReference_idAuditTypeReference;

	}


	/**
	* A constructor that uses a Result Set to build a basic object
	* @param myResultSet
	*/
	public Entitlement(ResultSet myResultSet) {
		buildBasicObjectFromResultSet(myResultSet);
	}

	/**
	* A blank constructor 
	*/
	public Entitlement() {
	
	}

	/**
	* A method that takes a ResultSet and uses it to populate a basic object.
	* @param rs The result set from which to build the object
	*/
	public void buildBasicObjectFromResultSet(ResultSet rs) {
		try {
			setidAudit(rs.getInt("idEntitlement"));
			setTotalNumberOfEntities(rs.getInt("TotalNumberOfEntities"));
			setEntitlement(rs.getString("Entitlement"));
			setCreatedDateTime(rs.getDate("CreatedDateTime"));
			setCreatedBy(rs.getString("CreatedBy"));
			setLastUpdatedDateTime(rs.getDate("LastUpdatedDateTime"));
			setLastUpdatedBy(rs.getString("LastUpdatedBy"));
			setAuditTypeReference_idAuditTypeReference(rs.getInt("AuditTypeReference_idAuditTypeReference"));
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}


	


	/**
	* A method that creates a new record
	* @return the id key of the newly created record
	*/
	public Boolean create(String databasePropertiesFile) {
		Boolean success = false;
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query 			= "INSERT INTO Entitlement (idEntitlement,TotalNumberOfEntities,Entitlement,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,AuditTypeReference_idAuditTypeReference) VALUES (?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query, Statement.RETURN_GENERATED_KEYS );	
			myDB.stmt.setInt(1, getidEntitlement());
			myDB.stmt.setInt(2, getTotalNumberOfEntities());
			myDB.stmt.setString(3, getEntitlement());
			myDB.stmt.setDate(4, getCreatedDateTime());
			myDB.stmt.setString(5, getCreatedBy());
			myDB.stmt.setDate(6, getLastUpdatedDateTime());
			myDB.stmt.setString(7, getLastUpdatedBy());
			myDB.stmt.setInt(8, getAuditTypeReference_idAuditTypeReference());
			myDB.stmt.executeUpdate();
			//ResultSet keys = myDB.stmt.getGeneratedKeys();
			//keys.next(); 
			//key = keys.getInt(1);
			myDB.close();
			success = true;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return success;
	}
	
	/**
	* A method that decrements field TotalNumberOfEntities by 1
	* @return 
	*/
	public Boolean  decrementTotalNumberOfEntitiesCount(String databasePropertiesFile) {
		Boolean success = false;
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query 			= "UPDATE Entitlement SET TotalNumberOfEntities = (TotalNumberOfEntities - 1), LastUpdatedBy = ?  WHERE idEntitlement = ? ;";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query );	
			myDB.stmt.setString(1, getLastUpdatedBy());
			myDB.stmt.setInt(2, getidEntitlement());
			myDB.stmt.executeUpdate();
			myDB.close();
			success = true;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return success;
	}
	
	/**
	* A method that increments field TotalNumberOfEntities by 1
	* @return the id key of the newly created record
	*/
	public Boolean  incrementTotalNumberOfEntitiesCount(String databasePropertiesFile) {
		Boolean success = false;
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query 			= "UPDATE Entitlement SET TotalNumberOfEntities = (TotalNumberOfEntities + 1), LastUpdatedBy = ? WHERE idEntitlement = ? ;";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query );	
			myDB.stmt.setString(1, getLastUpdatedBy());
			myDB.stmt.setInt(2, getidEntitlement());
			myDB.stmt.executeUpdate();
			myDB.close();
			success = true;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return success;
	}
	


	public int getidEntitlement() {
		return idEntitlement;
	}
	public void setidAudit(int idEntitlement) {
		this.idEntitlement = idEntitlement;
	}
	public int getTotalNumberOfEntities() {
		return totalNumberOfEntities;
	}
	public void setTotalNumberOfEntities(int totalNumberOfEntities) {
		this.totalNumberOfEntities = totalNumberOfEntities;
	}
	public String getEntitlement() {
		return entitlement;
	}
	public void setEntitlement(String entitlement) {
		this.entitlement = entitlement;
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	public int getAuditTypeReference_idAuditTypeReference() {
		return auditTypeReference_idAuditTypeReference;
	}
	public void setAuditTypeReference_idAuditTypeReference(int auditTypeReference_idAuditTypeReference) {
		this.auditTypeReference_idAuditTypeReference = auditTypeReference_idAuditTypeReference;
	}
}
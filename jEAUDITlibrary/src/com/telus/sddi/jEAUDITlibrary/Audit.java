package com.telus.sddi.jEAUDITlibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


/**
 * The Audit class, which maps to the Audit table of the eAUDIT database
 * @author fbegin1
 *
 */
public class Audit {

	private int 		idAudit;
	private int 		totalNumberOfEntities;
	private String 		entitlement;
	private String 		systemLoaderAuth;
	private Date 		createdDateTime;
	private String 		createdBy;
	private Date 		lastUpdatedDateTime;
	private String 		lastUpdatedBy;
	private int 		auditTypeReference_idAuditTypeReference;


	/**
	 * Default constructor
	 * @param idAudit
	 * @param totalNumberOfEntities
	 * @param entitlement
	 * @param systemLoaderAuth
	 * @param createdDateTime
	 * @param createdBy
	 * @param lastUpdatedDateTime
	 * @param lastUpdatedBy
	 * @param auditTypeReference_idAuditTypeReference
	 */
	public Audit(
			int idAudit,
			int totalNumberOfEntities,
			String entitlement,
			String systemLoaderAuth,
			Date createdDateTime,
			String createdBy,
			Date lastUpdatedDateTime,
			String lastUpdatedBy,
			int auditTypeReference_idAuditTypeReference) { 

		super();

		this.idAudit 									= idAudit;
		this.totalNumberOfEntities 						= totalNumberOfEntities;
		this.entitlement 								= entitlement;
		this.systemLoaderAuth 							= systemLoaderAuth;
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
	public Audit(ResultSet myResultSet) {
		buildBasicObjectFromResultSet(myResultSet);
	}


	/**
	* A method that takes a ResultSet and uses it to populate a basic object.
	* @param rs The result set from which to build the object
	*/
	public void buildBasicObjectFromResultSet(ResultSet rs) {
		try {
			setidAudit(rs.getInt("idAudit"));
			setTotalNumberOfEntities(rs.getInt("TotalNumberOfEntities"));
			setEntitlement(rs.getString("Entitlement"));
			setSystemLoaderAuth(rs.getString("SystemLoaderAuth"));
			setCreatedDateTime(rs.getDate("CreatedDateTime"));
			setCreatedBy(rs.getString("CreatedBy"));
			setLastUpdatedDateTime(rs.getDate("LastUpdatedDateTime"));
			setLastUpdatedBy(rs.getString("LastUpdatedBy"));
			setAuditTypeReference_idAuditTypeReference(rs.getInt("AuditTypeReference_idAuditTypeReference"));
		} catch (SQLException e) {
			System.out.println(e.toString());;
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
			myDB.query 			= "INSERT INTO Audit (idAudit,TotalNumberOfEntities,Entitlement,SystemLoaderAuth,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,AuditTypeReference_idAuditTypeReference) VALUES (?,?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query, Statement.RETURN_GENERATED_KEYS );	
			myDB.stmt.setInt(1, getidAudit());
			myDB.stmt.setInt(2, getTotalNumberOfEntities());
			myDB.stmt.setString(3, getEntitlement());
			myDB.stmt.setString(4, getSystemLoaderAuth());
			myDB.stmt.setDate(5, getCreatedDateTime());
			myDB.stmt.setString(6, getCreatedBy());
			myDB.stmt.setDate(7, getLastUpdatedDateTime());
			myDB.stmt.setString(8, getLastUpdatedBy());
			myDB.stmt.setInt(9, getAuditTypeReference_idAuditTypeReference());
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
	
	

	public int getidAudit() {
		return idAudit;
	}
	public void setidAudit(int idAudit) {
		this.idAudit = idAudit;
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


	public String getSystemLoaderAuth() {
		return systemLoaderAuth;
	}


	public void setSystemLoaderAuth(String systemLoaderAuth) {
		this.systemLoaderAuth = systemLoaderAuth;
	}
}

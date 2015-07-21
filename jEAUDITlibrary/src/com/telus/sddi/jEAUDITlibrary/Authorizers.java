package com.telus.sddi.jEAUDITlibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


public class Authorizers {

	private int 		idAuthorizers;
	private String 		authorizerType;
	private int 		authorizerEmpID;
	private String 		authorizerFirstName;
	private String 		authorizerLastName;
	private int 		authorizerManagerEmpID;
	private Date 		createdDateTime;
	private String 		createdBy;
	private Date 		lastUpdatedDateTime;
	private String 		lastUpdatedBy;
	private int 		entitlement_idEntitlement;


	/**
	 * Default contructor
	 * @param idAuthorizers
	 * @param authorizerType
	 * @param authorizerEmpID
	 * @param authorizerFirstName
	 * @param authorizerLastName
	 * @param authorizerManagerEmpID
	 * @param createdDateTime
	 * @param createdBy
	 * @param lastUpdatedDateTime
	 * @param lastUpdatedBy
	 * @param authorizersTypes_idAuthorizersTypes
	 * @param entitlement_idEntitlement
	 */
	public Authorizers(
			int idAuthorizers,
			String authorizerType,
			int authorizerEmpID,
			String authorizerFirstName,
			String authorizerLastName,
			int authorizerManagerEmpID,
			Date createdDateTime,
			String createdBy,
			Date lastUpdatedDateTime,
			String lastUpdatedBy,
			int audit_idAudit) { 

		super();

		this.idAuthorizers = idAuthorizers;
		this.authorizerType = authorizerType;
		this.authorizerEmpID = authorizerEmpID;
		this.authorizerFirstName = authorizerFirstName;
		this.authorizerLastName = authorizerLastName;
		this.authorizerManagerEmpID = authorizerManagerEmpID;
		this.createdDateTime = createdDateTime;
		this.createdBy = createdBy;
		this.lastUpdatedDateTime = lastUpdatedDateTime;
		this.lastUpdatedBy = lastUpdatedBy;
		this.entitlement_idEntitlement = audit_idAudit;

	}


	/**
	* A constructor that uses a Result Set to build a basic object
	* @param myResultSet
	*/
	public Authorizers(ResultSet myResultSet) {
		buildBasicObjectFromResultSet(myResultSet);
	}


	/**
	* A method that takes a ResultSet and uses it to populate a basic object.
	* @param rs The result set from which to build the object
	*/
	public void buildBasicObjectFromResultSet(ResultSet rs) {
		try {
			setidAuthorizers(rs.getInt("idAuthorizers"));
			setAuthorizerType(rs.getString("AuthorizerType"));
			setAuthorizerEmpID(rs.getInt("AuthorizerEmpID"));
			setAuthorizerFirstName(rs.getString("AuthorizerFirstName"));
			setAuthorizerLastName(rs.getString("AuthorizerLastName"));
			setAuthorizerManagerEmpID(rs.getInt("AuthorizerManagerEmpID"));
			setCreatedDateTime(rs.getDate("CreatedDateTime"));
			setCreatedBy(rs.getString("CreatedBy"));
			setLastUpdatedDateTime(rs.getDate("LastUpdatedDateTime"));
			setLastUpdatedBy(rs.getString("LastUpdatedBy"));
			setEntitlement_idEntitlement(rs.getInt("Entitlement_idEntitlement"));
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
			myDB.query 			= "INSERT INTO Authorizers (AuthorizerType,AuthorizerEmpID,AuthorizerFirstName,AuthorizerLastName,AuthorizerManagerEmpID,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Entitlement_idEntitlement) VALUES (?,?,?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query, Statement.RETURN_GENERATED_KEYS );	
			myDB.stmt.setString(1, getAuthorizerType());
			myDB.stmt.setInt(2, getAuthorizerEmpID());
			myDB.stmt.setString(3, getAuthorizerFirstName());
			myDB.stmt.setString(4, getAuthorizerLastName());
			myDB.stmt.setInt(5, getAuthorizerManagerEmpID());
			myDB.stmt.setDate(6, getCreatedDateTime());
			myDB.stmt.setString(7, getCreatedBy());
			myDB.stmt.setDate(8, getLastUpdatedDateTime());
			myDB.stmt.setString(9, getLastUpdatedBy());
			myDB.stmt.setInt(10, getEntitlement_idEntitlement());
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


	public int getidAuthorizers() {
		return idAuthorizers;
	}
	public void setidAuthorizers(int idAuthorizers) {
		this.idAuthorizers = idAuthorizers;
	}
	public String getAuthorizerType() {
		return authorizerType;
	}
	public void setAuthorizerType(String authorizerType) {
		this.authorizerType = authorizerType;
	}
	public int getAuthorizerEmpID() {
		return authorizerEmpID;
	}
	public void setAuthorizerEmpID(int authorizerEmpID) {
		this.authorizerEmpID = authorizerEmpID;
	}
	public String getAuthorizerFirstName() {
		return authorizerFirstName;
	}
	public void setAuthorizerFirstName(String authorizerFirstName) {
		this.authorizerFirstName = authorizerFirstName;
	}
	public String getAuthorizerLastName() {
		return authorizerLastName;
	}
	public void setAuthorizerLastName(String authorizerLastName) {
		this.authorizerLastName = authorizerLastName;
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

	public int getEntitlement_idEntitlement() {
		return entitlement_idEntitlement;
	}
	public void setEntitlement_idEntitlement(int audit_idAudit) {
		this.entitlement_idEntitlement = audit_idAudit;
	}

	public void setIdAuthorizers(int idAuthorizers) {
		this.idAuthorizers = idAuthorizers;
	}

	public int getAuthorizerManagerEmpID() {
		return authorizerManagerEmpID;
	}

	public void setAuthorizerManagerEmpID(int authorizerManagerEmpID) {
		this.authorizerManagerEmpID = authorizerManagerEmpID;
	}
	
	public String getAuthorizerFullName() {
		return authorizerFirstName + " " + authorizerLastName;
	}
	
	public String getAuthorizerDetails() {
		return "[" + authorizerEmpID + "] " + authorizerFirstName + " " + authorizerLastName;
	}
	
	public String getAuthorizerFullDetails() {
		return getAuthorizerDetails() + " (t:" + authorizerType + ", mgr:" + authorizerManagerEmpID + ")";
	}
	
	
}
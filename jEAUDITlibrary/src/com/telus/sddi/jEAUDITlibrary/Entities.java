package com.telus.sddi.jEAUDITlibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


/**
 * The Audit class, which maps to the Entities table of the eAUDIT database
 * @author fbegin1
 *
 */
public class Entities {

	private int 		idEntities;
	private String 		primaryKey;
	private String 		uDEAprimaryFieldsValues;
	private String 		uDEAsecondaryFieldsValues;
	private Date 		createdDateTime;
	private String 		createdBy;
	private Date 		lastUpdatedDateTime;
	private String 		lastUpdatedBy;
	private int 		audit_idAudit;


	/**
	 * Default contructor
	 * @param idEntities
	 * @param primaryKey
	 * @param uDEAprimaryFieldsValues
	 * @param uDEAsecondaryFieldsValues
	 * @param createdDateTime
	 * @param createdBy
	 * @param lastUpdatedDateTime
	 * @param lastUpdatedBy
	 * @param audit_idAudit
	 */
	public Entities(
			int idEntities,
			String primaryKey,
			String uDEAprimaryFieldsValues,
			String uDEAsecondaryFieldsValues,
			Date createdDateTime,
			String createdBy,
			Date lastUpdatedDateTime,
			String lastUpdatedBy,
			int audit_idAudit) { 

		super();

		this.idEntities = idEntities;
		this.primaryKey = primaryKey;
		this.uDEAprimaryFieldsValues = uDEAprimaryFieldsValues;
		this.uDEAsecondaryFieldsValues = uDEAsecondaryFieldsValues;
		this.createdDateTime = createdDateTime;
		this.createdBy = createdBy;
		this.lastUpdatedDateTime = lastUpdatedDateTime;
		this.lastUpdatedBy = lastUpdatedBy;
		this.audit_idAudit = audit_idAudit;

	}


	/**
	* A constructor that uses a Result Set to build a basic object
	* @param myResultSet
	*/
	public Entities(ResultSet myResultSet) {
		buildBasicObjectFromResultSet(myResultSet);
	}


	/**
	* A method that takes a ResultSet and uses it to populate a basic object.
	* @param rs The result set from which to build the object
	*/
	public void buildBasicObjectFromResultSet(ResultSet rs) {
		try {
			setidEntities(rs.getInt("idEntities"));
			setPrimaryKey(rs.getString("PrimaryKey"));
			setUDEAprimaryFieldsValues(rs.getString("UDEAprimaryFieldsValues"));
			setUDEAsecondaryFieldsValues(rs.getString("UDEAsecondaryFieldsValues"));
			setCreatedDateTime(rs.getDate("CreatedDateTime"));
			setCreatedBy(rs.getString("CreatedBy"));
			setLastUpdatedDateTime(rs.getDate("LastUpdatedDateTime"));
			setLastUpdatedBy(rs.getString("LastUpdatedBy"));
			setAudit_idAudit(rs.getInt("Audit_idAudit"));
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
			myDB.query 			= "INSERT INTO Entities (PrimaryKey,UDEAprimaryFieldsValues,UDEAsecondaryFieldsValues,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Audit_idAudit) VALUES (?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query, Statement.RETURN_GENERATED_KEYS );	
			myDB.stmt.setString(1, getPrimaryKey());
			myDB.stmt.setString(2, getUDEAprimaryFieldsValues());
			myDB.stmt.setString(3, getUDEAsecondaryFieldsValues());
			myDB.stmt.setDate(4, getCreatedDateTime());
			myDB.stmt.setString(5, getCreatedBy());
			myDB.stmt.setDate(6, getLastUpdatedDateTime());
			myDB.stmt.setString(7, getLastUpdatedBy());
			myDB.stmt.setInt(8, getAudit_idAudit());
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



	public int getidEntities() {
		return idEntities;
	}
	public void setidEntities(int idEntities) {
		this.idEntities = idEntities;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getUDEAprimaryFieldsValues() {
		return uDEAprimaryFieldsValues;
	}
	public void setUDEAprimaryFieldsValues(String uDEAprimaryFieldsValues) {
		this.uDEAprimaryFieldsValues = uDEAprimaryFieldsValues;
	}
	public String getUDEAsecondaryFieldsValues() {
		return uDEAsecondaryFieldsValues;
	}
	public void setUDEAsecondaryFieldsValues(String uDEAsecondaryFieldsValues) {
		this.uDEAsecondaryFieldsValues = uDEAsecondaryFieldsValues;
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
	public int getAudit_idAudit() {
		return audit_idAudit;
	}
	public void setAudit_idAudit(int audit_idAudit) {
		this.audit_idAudit = audit_idAudit;
	}
}

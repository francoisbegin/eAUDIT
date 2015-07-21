package com.telus.sddi.jEAUDITlibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


public class Entities {

	private int 		idEntities;
	private String 		primaryKey;
	private String 		uDEAprimaryFieldsValues;
	private String 		uDEAsecondaryFieldsValues;
	private Date 		createdDateTime;
	private String 		createdBy;
	private Date 		lastUpdatedDateTime;
	private String 		lastUpdatedBy;
	private int 		entitlement_idEntitlement;



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
	 * @param entitlement_idEntitlement
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
			int entitlement_idEntitlement) { 

		super();

		this.idEntities = idEntities;
		this.primaryKey = primaryKey;
		this.uDEAprimaryFieldsValues = uDEAprimaryFieldsValues;
		this.uDEAsecondaryFieldsValues = uDEAsecondaryFieldsValues;
		this.createdDateTime = createdDateTime;
		this.createdBy = createdBy;
		this.lastUpdatedDateTime = lastUpdatedDateTime;
		this.lastUpdatedBy = lastUpdatedBy;
		this.entitlement_idEntitlement = entitlement_idEntitlement;

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
			myDB.query 			= "INSERT INTO Entities (PrimaryKey,UDEAprimaryFieldsValues,UDEAsecondaryFieldsValues,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,Entitlement_idEntitlement) VALUES (?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query, Statement.RETURN_GENERATED_KEYS );	
			myDB.stmt.setString(1, getPrimaryKey());
			myDB.stmt.setString(2, getUDEAprimaryFieldsValues());
			myDB.stmt.setString(3, getUDEAsecondaryFieldsValues());
			myDB.stmt.setDate(4, getCreatedDateTime());
			myDB.stmt.setString(5, getCreatedBy());
			myDB.stmt.setDate(6, getLastUpdatedDateTime());
			myDB.stmt.setString(7, getLastUpdatedBy());
			myDB.stmt.setInt(8, getEntitlement_idEntitlement());
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
	 * A method that updates a record. 
	 */
	public Boolean update(String databasePropertiesFile) {
		
		Boolean success = false;
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		try {
			myDB.connect();
			
			myDB.query 		= "";		
			myDB.query 		= "UPDATE Entities SET PrimaryKey=?, UDEAprimaryFieldsValues=?, UDEAsecondaryFieldsValues=?, LastUpdatedBy=?,Entitlement_idEntitlement=? WHERE idEntities = ?;";
			
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query);
			
			myDB.stmt.setString(1, getPrimaryKey() );
			myDB.stmt.setString(2, getUDEAprimaryFieldsValues() );
			myDB.stmt.setString(3, getUDEAsecondaryFieldsValues() );
			myDB.stmt.setString(4, getLastUpdatedBy() );
			myDB.stmt.setInt(5, getEntitlement_idEntitlement() );
			myDB.stmt.setInt(6, getidEntities() );
			myDB.stmt.executeUpdate();
			myDB.close();
			success = true;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	
		return success;
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
	public int getEntitlement_idEntitlement() {
		return entitlement_idEntitlement;
	}
	public void setEntitlement_idEntitlement(int entitlement_idEntitlement) {
		this.entitlement_idEntitlement = entitlement_idEntitlement;
	}
}
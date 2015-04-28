package com.telus.sddi.jEAUDITlibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


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


	private static String mySelectStatement = "SELECT idAudit,TotalNumberOfEntities,Entitlement,SystemLoaderAuth, CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,AuditTypeReference_idAuditTypeReference FROM Audit";


	/**
	 * Default contructor
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
	
	
	
	/**
	 * A method that mass creates an ArrayList of object
	 * @return the idAudit of the newly created audit
	 */
	public static void create(ArrayList<Audit> recordsToMassLoad,String databasePropertiesFile) {
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			
			myDB.query = "";		
			myDB.query 			=  "INSERT INTO Audit (idAudit, TotalNumberOfEntities,Entitlement,SystemLoaderAuth,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy,AuditTypeReference_idAuditTypeReference) VALUES (?,?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query);
			
			int i = 1;
			
			/*
			 * Set parameters for the massive query
			 */
			for (Audit myCurrRecord : recordsToMassLoad ) {
				myDB.stmt.setInt(1, myCurrRecord.getidAudit());
				myDB.stmt.setInt(2, myCurrRecord.getTotalNumberOfEntities());
				myDB.stmt.setString(3, myCurrRecord.getEntitlement());
				myDB.stmt.setString(4, myCurrRecord.getSystemLoaderAuth());
				myDB.stmt.setDate(5, myCurrRecord.getCreatedDateTime());
				myDB.stmt.setString(6, myCurrRecord.getCreatedBy());
				myDB.stmt.setDate(7, myCurrRecord.getLastUpdatedDateTime());
				myDB.stmt.setString(8, myCurrRecord.getLastUpdatedBy());
				myDB.stmt.setInt(9, myCurrRecord.getAuditTypeReference_idAuditTypeReference());
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
	* A method that creates a list of all records
	* @return ArrayList of all records
	*/
	public static ArrayList<Audit> list(String databasePropertiesFile) {
		ArrayList<Audit> myRecords = new ArrayList<Audit>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement; 
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Audit myCurrentRecord = new Audit(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecords;
	}
	
	
	/**
	 * A method that creates a list of all records associated with a sepcific AuditTypeReference
	 * @param auditTypeReference_idAuditTypeReference
	 * @return
	 */
	public static ArrayList<Audit> listByAuditTypeReference(int auditTypeReference_idAuditTypeReference, String databasePropertiesFile) {
		ArrayList<Audit> myRecords = new ArrayList<Audit>();
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement + " WHERE AuditTypeReference_idAuditTypeReference=?";
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.setInt(1, auditTypeReference_idAuditTypeReference );
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Audit myCurrentRecord = new Audit(myDB.rs);
				myRecords.add(myCurrentRecord);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return myRecords;
	}


	/**
	 * A method that returns the higest idAudit value in table Audit. Useful to find top value so we can pre-assign new values at load time
	 * @param auditTypeReference_idAuditTypeReference
	 * @return
	 */
	public static int findMaxIDauditValue(String databasePropertiesFile) {
		int maxValue = 0;
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = "select MAX(idAudit) as maxIDaudit from Audit";
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			if (myDB.rs.next()) {
				maxValue = myDB.rs.getInt(1);
			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return maxValue;
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

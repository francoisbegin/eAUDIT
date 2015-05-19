package com.telus.sddi.jEAUDITlibrary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;


/**
 * The Audit class, which maps to the AuditTypeReference table of the eAUDIT database
 * @author fbegin1
 *
 */
public class AuditTypeReference {

	private int 		idAuditTypeReference;
	private String 		auditName;
	private String 		auditDescription;
	private Date 		auditStart;
	private Date 		auditEnd;
	private String 		auditInstructionsEN;
	private String 		auditInstructionsFR;
	private String 		uDEAprimaryFieldsEN;
	private String 		uDEAprimaryFieldsFR;
	private String 		uDEAsecondaryFieldsEN;
	private String 		uDEAsecondaryFieldsFR;
	private Boolean		auditByManager;
	private String 		dataLoadType;
	private int 		eMACyclesTimelineID;
	private Boolean		useEmac;
	private Date 		createdDateTime;
	private String 		createdBy;
	private Date 		lastUpdatedDateTime;
	private String 		lastUpdatedBy;


	/**
	 * Full constructor
	 * @param idAuditTypeReference
	 * @param auditName
	 * @param auditDescription
	 * @param auditStart
	 * @param auditEnd
	 * @param auditInstructionsEN
	 * @param auditInstructionsFR
	 * @param uDEAprimaryFieldsEN
	 * @param uDEAprimaryFieldsFR
	 * @param uDEAsecondaryFieldsEN
	 * @param uDEAsecondaryFieldsFR
	 * @param auditByManager
	 * @param dataLoadType
	 * @param eMACyclesTimelineID
	 * @param useEmac
	 * @param createdDateTime
	 * @param createdBy
	 * @param lastUpdatedDateTime
	 * @param lastUpdatedBy
	 */
	public AuditTypeReference(
			int idAuditTypeReference,
			String auditName,
			String auditDescription,
			Date auditStart,
			Date auditEnd,
			String auditInstructionsEN,
			String auditInstructionsFR,
			String uDEAprimaryFieldsEN,
			String uDEAprimaryFieldsFR,
			String uDEAsecondaryFieldsEN,
			String uDEAsecondaryFieldsFR,
			Boolean auditByManager,
			String dataLoadType,
			int eMACyclesTimelineID,
			Boolean useEmac,
			Date createdDateTime,
			String createdBy,
			Date lastUpdatedDateTime,
			String lastUpdatedBy) { 

		super();

		this.idAuditTypeReference = idAuditTypeReference;
		this.auditName = auditName;
		this.auditDescription = auditDescription;
		this.auditStart = auditStart;
		this.auditEnd = auditEnd;
		this.auditInstructionsEN = auditInstructionsEN;
		this.auditInstructionsFR = auditInstructionsFR;
		this.uDEAprimaryFieldsEN = uDEAprimaryFieldsEN;
		this.uDEAprimaryFieldsFR = uDEAprimaryFieldsFR;
		this.uDEAsecondaryFieldsEN = uDEAsecondaryFieldsEN;
		this.uDEAsecondaryFieldsFR = uDEAsecondaryFieldsFR;
		this.auditByManager = auditByManager;
		this.dataLoadType = dataLoadType;
		this.eMACyclesTimelineID = eMACyclesTimelineID;
		this.useEmac = useEmac;
		this.createdDateTime = createdDateTime;
		this.createdBy = createdBy;
		this.lastUpdatedDateTime = lastUpdatedDateTime;
		this.lastUpdatedBy = lastUpdatedBy;

	}


	

	/**
	* A constructor that uses a Result Set to build a basic object
	* @param myResultSet
	*/
	public AuditTypeReference(ResultSet myResultSet) {
		buildBasicObjectFromResultSet(myResultSet);
	}
	

	/**
	* A blankconstructor 
	* @param myResultSet
	*/
	public AuditTypeReference() {
		
	}


	/**
	* A method that takes a ResultSet and uses it to populate a basic object.
	* @param rs The result set from which to build the object
	*/
	public void buildBasicObjectFromResultSet(ResultSet rs) {
		try {
			setidAuditTypeReference(rs.getInt("idAuditTypeReference"));
			setAuditName(rs.getString("AuditName"));
			setAuditDescription(rs.getString("AuditDescription"));
			setAuditStart(rs.getDate("AuditStart"));
			setAuditEnd(rs.getDate("AuditEnd"));
			setAuditInstructionsEN(rs.getString("AuditInstructionsEN"));
			setAuditInstructionsFR(rs.getString("AuditInstructionsFR"));
			setUDEAprimaryFieldsEN(rs.getString("UDEAprimaryFieldsEN"));
			setUDEAprimaryFieldsFR(rs.getString("UDEAprimaryFieldsFR"));
			setUDEAsecondaryFieldsEN(rs.getString("UDEAsecondaryFieldsEN"));
			setUDEAsecondaryFieldsFR(rs.getString("UDEAsecondaryFieldsFR"));
			setAuditByManager(rs.getBoolean("AuditByManager"));
			setDataLoadType(rs.getString("DataLoadType"));
			seteMACyclesTimelineID(rs.getInt("eMACyclesTimelineID"));
			setUseEmac(rs.getBoolean("UseEmac"));
			setCreatedDateTime(rs.getDate("CreatedDateTime"));
			setCreatedBy(rs.getString("CreatedBy"));
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
			myDB.query 			= "INSERT INTO AuditTypeReference (AuditName,AuditDescription,AuditStart,AuditEnd,AuditInstructionsEN,AuditInstructionsFR,UDEAprimaryFieldsEN,UDEAprimaryFieldsFR,UDEAsecondaryFieldsEN,UDEAsecondaryFieldsFR,AuditByManager,DataLoadType,eMACyclesTimelineID,UseEmac,CreatedDateTime,CreatedBy,LastUpdatedDateTime,LastUpdatedBy) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			myDB.stmt 			= myDB.conn.prepareStatement(myDB.query, Statement.RETURN_GENERATED_KEYS );	
			myDB.stmt.setString(1, getAuditName());
			myDB.stmt.setString(2, getAuditDescription());
			myDB.stmt.setDate(3, getAuditStart());
			myDB.stmt.setDate(4, getAuditEnd());
			myDB.stmt.setString(5, getAuditInstructionsEN());
			myDB.stmt.setString(6, getAuditInstructionsFR());
			myDB.stmt.setString(7, getUDEAprimaryFieldsEN());
			myDB.stmt.setString(8, getUDEAprimaryFieldsFR());
			myDB.stmt.setString(9, getUDEAsecondaryFieldsEN());
			myDB.stmt.setString(10, getUDEAsecondaryFieldsFR());
			myDB.stmt.setBoolean(11, getAuditByManager());
			myDB.stmt.setString(12, getDataLoadType());
			myDB.stmt.setInt(13, geteMACyclesTimelineID());
			myDB.stmt.setBoolean(14, getUseEmac());
			myDB.stmt.setDate(15, getCreatedDateTime());
			myDB.stmt.setString(16, getCreatedBy());
			myDB.stmt.setDate(17, getLastUpdatedDateTime());
			myDB.stmt.setString(18, getLastUpdatedBy());
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


	public int getidAuditTypeReference() {
		return idAuditTypeReference;
	}
	public void setidAuditTypeReference(int idAuditTypeReference) {
		this.idAuditTypeReference = idAuditTypeReference;
	}
	public String getAuditName() {
		return auditName;
	}
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	public String getAuditDescription() {
		return auditDescription;
	}
	public void setAuditDescription(String auditDescription) {
		this.auditDescription = auditDescription;
	}
	public Date getAuditStart() {
		return auditStart;
	}
	public void setAuditStart(Date auditStart) {
		this.auditStart = auditStart;
	}
	public Date getAuditEnd() {
		return auditEnd;
	}
	public void setAuditEnd(Date auditEnd) {
		this.auditEnd = auditEnd;
	}
	public String getAuditInstructionsEN() {
		return auditInstructionsEN;
	}
	public void setAuditInstructionsEN(String auditInstructionsEN) {
		this.auditInstructionsEN = auditInstructionsEN;
	}
	public String getAuditInstructionsFR() {
		return auditInstructionsFR;
	}
	public void setAuditInstructionsFR(String auditInstructionsFR) {
		this.auditInstructionsFR = auditInstructionsFR;
	}
	public String getUDEAprimaryFieldsEN() {
		return uDEAprimaryFieldsEN;
	}
	public void setUDEAprimaryFieldsEN(String uDEAprimaryFieldsEN) {
		this.uDEAprimaryFieldsEN = uDEAprimaryFieldsEN;
	}
	public String getUDEAprimaryFieldsFR() {
		return uDEAprimaryFieldsFR;
	}
	public void setUDEAprimaryFieldsFR(String uDEAprimaryFieldsFR) {
		this.uDEAprimaryFieldsFR = uDEAprimaryFieldsFR;
	}
	public String getUDEAsecondaryFieldsEN() {
		return uDEAsecondaryFieldsEN;
	}
	public void setUDEAsecondaryFieldsEN(String uDEAsecondaryFieldsEN) {
		this.uDEAsecondaryFieldsEN = uDEAsecondaryFieldsEN;
	}
	public String getUDEAsecondaryFieldsFR() {
		return uDEAsecondaryFieldsFR;
	}
	public void setUDEAsecondaryFieldsFR(String uDEAsecondaryFieldsFR) {
		this.uDEAsecondaryFieldsFR = uDEAsecondaryFieldsFR;
	}
	public Boolean getAuditByManager() {
		return auditByManager;
	}
	public void setAuditByManager(Boolean auditByManager) {
		this.auditByManager = auditByManager;
	}
	public String getDataLoadType() {
		return dataLoadType;
	}
	public void setDataLoadType(String dataLoadType) {
		this.dataLoadType = dataLoadType;
	}
	public int geteMACyclesTimelineID() {
		return eMACyclesTimelineID;
	}
	public void seteMACyclesTimelineID(int eMACyclesTimelineID) {
		this.eMACyclesTimelineID = eMACyclesTimelineID;
	}
	public Boolean getUseEmac() {
		return useEmac;
	}
	public void setUseEmac(Boolean useEmac) {
		this.useEmac = useEmac;
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
}

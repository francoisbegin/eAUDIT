package com.telus.sddi.jEAUDITlibraryUtil;

import java.util.ArrayList;

import com.telus.sddi.UnifiedToolBoxV2.DBConnector;
import com.telus.sddi.jEAUDITlibrary.AuditDataReaderObject;
import com.telus.sddi.jEAUDITlibrary.Authorizers;
import com.telus.sddi.jEAUDITlibrary.Entities;
import com.telus.sddi.jEAUDITlibrary.Entitlement;


/**
 * This class is a meta-object that is used to read and organize audit data back from the database. This class is used when making adjustments to manager-driven audits 
 * (which explains why authorizersObject is a single object and not an ArrayList
 * 
 * @author T805959
 *
 */
public class AuditDataReaderObjectUtil {

	private static String mySelectStatement = "SELECT et.idEntities,et.PrimaryKey,et.UDEAprimaryFieldsValues,et.UDEAsecondaryFieldsValues,et.CreatedDateTime,et.CreatedBy,et.LastUpdatedDateTime,et.LastUpdatedBy,et.Entitlement_idEntitlement,"+
													"e.idEntitlement,e.TotalNumberOfEntities,e.Entitlement,e.CreatedDateTime,e.CreatedBy,e.LastUpdatedDateTime,e.LastUpdatedBy,e.AuditTypeReference_idAuditTypeReference,"+
													"a.idAuthorizers,a.AuthorizerType,a.AuthorizerEmpID,a.AuthorizerFirstName,a.AuthorizerLastName,a.AuthorizerManagerEmpID,a.CreatedDateTime,a.CreatedBy,a.LastUpdatedDateTime,a.LastUpdatedBy,a.Entitlement_idEntitlement "+ 
														"FROM Entitlement as e "+
															"LEFT JOIN Entities AS et ON (et.Entitlement_idEntitlement=e.idEntitlement) "+
															"LEFT JOIN Authorizers as a ON (a.Entitlement_idEntitlement=e.idEntitlement) ";
															
	/**
	 * Gets a list of AuditDataReaderObject objects from the database. Note that this list is purely flat : 1 entity = 1 record (assuming a single authorizer) 
	 * @param idAuditTypeReference
	 * @param primeAuthorizersOnly
	 * @return
	 */
	public static ArrayList<AuditDataReaderObject> listByIDauditTypeReference(int idAuditTypeReference, Boolean primeAuthorizersOnly, String databasePropertiesFile ) {
		ArrayList<AuditDataReaderObject> myRecords = new ArrayList<AuditDataReaderObject>();
		
		DBConnector myDB = new DBConnector(databasePropertiesFile);
		myDB.connect();
		try {
			myDB.query = mySelectStatement + "WHERE e.AuditTypeReference_idAuditTypeReference = ? AND et.PrimaryKey IS NOT NULL ";
			
			if ( primeAuthorizersOnly) {
				myDB.query += " AND a.AuthorizerType='Prime'";
			}
			myDB.stmt = myDB.conn.prepareStatement(myDB.query);
			myDB.stmt.setInt(1, idAuditTypeReference);
			
			myDB.stmt.executeQuery();
			myDB.rs = myDB.stmt.getResultSet();
			while (myDB.rs.next()) {
				Entities myCurrentEntity = new Entities(
						myDB.rs.getInt("et.idEntities"), 
						myDB.rs.getString("et.PrimaryKey"), 
						myDB.rs.getString("et.UDEAprimaryFieldsValues"), 
						myDB.rs.getString("et.UDEAsecondaryFieldsValues"), 
						myDB.rs.getDate("et.CreatedDateTime"), 
						myDB.rs.getString("et.CreatedBy"), 
						myDB.rs.getDate("et.LastUpdatedDateTime"), 
						myDB.rs.getString("et.LastUpdatedBy"), 
						myDB.rs.getInt("et.Entitlement_idEntitlement"));
	
				Entitlement myCurrentEntitlement = new Entitlement(
						myDB.rs.getInt("e.idEntitlement"), 
						myDB.rs.getInt("e.TotalNumberOfEntities"), 
						myDB.rs.getString("e.Entitlement"), 
						myDB.rs.getDate("e.CreatedDateTime"), 
						myDB.rs.getString("e.CreatedBy"), 
						myDB.rs.getDate("e.LastUpdatedDateTime"), 
						myDB.rs.getString("e.LastUpdatedBy"), 
						myDB.rs.getInt("e.AuditTypeReference_idAuditTypeReference"));
				
				Authorizers myCurrentAuthorizer = new Authorizers(
						myDB.rs.getInt("a.idAuthorizers"),
						myDB.rs.getString("a.AuthorizerType"), 
						myDB.rs.getInt("a.AuthorizerEmpID"), 
						myDB.rs.getString("a.AuthorizerFirstName"), 
						myDB.rs.getString("a.AuthorizerLastName"), 
						myDB.rs.getInt("a.AuthorizerManagerEmpID"), 
						myDB.rs.getDate("a.CreatedDateTime"), 
						myDB.rs.getString("a.CreatedBy"), 
						myDB.rs.getDate("a.LastUpdatedDateTime"), 
						myDB.rs.getString("a.LastUpdatedBy"), 
						myDB.rs.getInt("a.Entitlement_idEntitlement"));
				
				
				myRecords.add(new AuditDataReaderObject(myCurrentEntity, myCurrentEntitlement, myCurrentAuthorizer) );


			}
			myDB.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		
		
		
		
		return myRecords;
	}

}
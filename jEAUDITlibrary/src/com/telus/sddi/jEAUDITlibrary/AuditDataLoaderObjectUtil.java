package com.telus.sddi.jEAUDITlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.telus.sddi.jEAUDITlibrary.Audit;
import com.telus.sddi.jEAUDITlibrary.Authorizers;
import com.telus.sddi.jEAUDITlibrary.Entities;

/**
 * This class is a meta-object that is used to organize audit data prior to a data load. Using this class, you can funnel all of the
 * audit data into a generic class from where the data load can be done quickly and efficiently.
 * 
 * The Audit table is one of the key tables in the data model of eAUDIT since it is the table that links 
 * 		Authorizers
 * 		AuditTypeReference 
 * 		Entities. 
 * 
 * One key field in class AuditDataLoaderObject is therefore idAuditID
 * 
 * @author T805959
 *
 */
public class AuditDataLoaderObjectUtil {


	
	/**
	 * A method that mass loads audit data
	 * @param auditDataLoaderObjectMap A HashMap that contains all the audit data, organized for easy loading
	 * @param verbose whether or not to show progress as the Audit, Entitites and Authorzers records are loaded
	 */
	public static void massLoad(HashMap<Integer, AuditDataLoaderObject> auditDataLoaderObjectMap, Boolean verbose, String databasePropertiesFile) {
  		  		
  		ArrayList<Audit> allAuditRecords 				= new ArrayList<Audit>();
  		ArrayList<Authorizers> allAuthorizersRecords 	= new ArrayList<Authorizers>();
  		ArrayList<Entities> allEntititesRecords 		= new ArrayList<Entities>();
  		
  		for (Map.Entry<Integer, AuditDataLoaderObject> entry : auditDataLoaderObjectMap.entrySet()) {
  			AuditDataLoaderObject myLoaderObject 		= entry.getValue();	
  			Audit auditRecord							= myLoaderObject.getAudit();
  			allAuditRecords.add(auditRecord);
  			
  			for (Entities myAuditEntities : myLoaderObject.getAuditEntities()) {
  				allEntititesRecords.add(myAuditEntities);
  			}
  			
  			for (Authorizers myAuditAuthorizers : myLoaderObject.getAuditAuthorizers()) {
  				allAuthorizersRecords.add(myAuditAuthorizers);
  			}
  		
  		}
  		
  		if (verbose) System.out.println("Mass loading " + allAuditRecords.size() + " Audit records");
  		AuditUtil.massLoad(allAuditRecords, databasePropertiesFile);
  		
  		if (verbose) System.out.println("Mass loading " + allEntititesRecords.size() + " Entitites records");
  		EntitiesUtil.massLoad(allEntititesRecords, databasePropertiesFile);
  		
  		if (verbose) System.out.println("Mass loading " + allAuthorizersRecords.size() + " Authorizers records");
  		AuthorizersUtil.massLoad(allAuthorizersRecords, databasePropertiesFile);

	}
	
	
	/**
	 * A method that dump (to the console) the contents of an auditDataLoaderObjectMap. 
	 * Used for testing
	 * @param auditDataLoaderObjectMap
	 */
	public static void dump(HashMap<Integer, AuditDataLoaderObject> auditDataLoaderObjectMap) {
		
		for (Map.Entry<Integer, AuditDataLoaderObject> entry : auditDataLoaderObjectMap.entrySet()) {
			Integer key 							= entry.getKey();
			AuditDataLoaderObject myLoaderObject 	= entry.getValue();
			System.out.println("Looking at audit "+key);
			System.out.println("   idAudit inside Loader Object           = "+ myLoaderObject.getIdAuditID());
			System.out.println("   idTypeReference inside Loader Object   = "+ myLoaderObject.getIdAuditTypeReference());
			System.out.println("   Audit record: idAudit                  = "+ myLoaderObject.getAudit().getidAudit());
			System.out.println("   Audit record: total number of entities = "+ myLoaderObject.getAudit().getTotalNumberOfEntities());
			System.out.println("   Audit record: entitlement              = "+ myLoaderObject.getAudit().getEntitlement());
			System.out.println("   Audit record: sysLoaderAuth            = "+ myLoaderObject.getAudit().getSystemLoaderAuth());
			System.out.println("   Authorizers are: ");
			for ( Authorizers myAuth : myLoaderObject.getAuditAuthorizers()) {
				System.out.println("     " 
							+ myAuth.getAuthorizerEmpID() + " | " 
							+ myAuth.getAuthorizerFirstName() + " | " 
							+ myAuth.getAuthorizerLastName() + " | "
							+ myAuth.getAuthorizerType() + " | "
							+ myAuth.getAudit_idAudit());
			}
			System.out.println("   Entitites are: ");
			for ( Entities myEntitites : myLoaderObject.getAuditEntities()) {
				System.out.println("     " 
							+ myEntitites.getPrimaryKey() + " | " 
							+ myEntitites.getUDEAprimaryFieldsValues() + " | " 
							+ myEntitites.getUDEAsecondaryFieldsValues() + " | "
							+ myEntitites.getAudit_idAudit());
			}
		}
	}
	
	
}

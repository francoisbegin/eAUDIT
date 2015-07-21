package com.telus.sddi.jEAUDITlibraryUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.telus.sddi.jEAUDITlibrary.Entitlement;
import com.telus.sddi.jEAUDITlibrary.AuditDataLoaderObject;
import com.telus.sddi.jEAUDITlibrary.Authorizers;
import com.telus.sddi.jEAUDITlibrary.Entities;

/**
 * This class is a meta-object that is used to organize audit data prior to a data load. Using this class, you can funnel all of the
 * audit data into a generic class from where the data load can be done quickly and efficiently.
 * 
 * The Entitlement table is one of the key tables in the data model of eAUDIT since it is the table that links 
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
	 * A method that bulks create records
	 * This methods bulk creates up to 2500 records at a time - hard-coded in the method but easily modified 
	 * @param auditDataLoaderObjectMap A HashMap that contains all the audit data, organized for easy loading
	 * @param verbose whether or not to show progress as the Entitlement, Entitites and Authorizers records are loaded
	 */
	public static void bulkCreate(HashMap<Integer, AuditDataLoaderObject> auditDataLoaderObjectMap, Boolean verbose, String databasePropertiesFile) {
  		  		
  		ArrayList<Entitlement> allEntitlementRecords 	= new ArrayList<Entitlement>();
  		ArrayList<Authorizers> allAuthorizersRecords 	= new ArrayList<Authorizers>();
  		ArrayList<Entities> allEntititesRecords 		= new ArrayList<Entities>();
  		
  		for (Map.Entry<Integer, AuditDataLoaderObject> entry : auditDataLoaderObjectMap.entrySet()) {
  			AuditDataLoaderObject myLoaderObject 		= entry.getValue();	
  			Entitlement auditRecord						= myLoaderObject.getEntitlement();
  			allEntitlementRecords.add(auditRecord);
  			
  			for (Entities myAuditEntities : myLoaderObject.getAuditEntities()) {
  				allEntititesRecords.add(myAuditEntities);
  			}
  			
  			for (Authorizers myAuditAuthorizers : myLoaderObject.getAuditAuthorizers()) {
  				allAuthorizersRecords.add(myAuditAuthorizers);
  			}
  		
  		}
  		
  		if (verbose) System.out.println("Bulk creating " + allEntitlementRecords.size() + " Entitlement records");
  		EntitlementUtil.bulkCreate(allEntitlementRecords, databasePropertiesFile);
  		
  		if (verbose) System.out.println("Bulk creating " + allEntititesRecords.size() + " Entitites records");
  		EntitiesUtil.bulkCreate(allEntititesRecords, databasePropertiesFile);
  		
  		if (verbose) System.out.println("Bulk creating " + allAuthorizersRecords.size() + " Authorizers records");
  		AuthorizersUtil.bulkCreate(allAuthorizersRecords, databasePropertiesFile);

	}
	
	
	/**
	 * A method that dump (to the console) the contents of an auditDataLoaderObjectMap. 
	 * Used for testing
	 * @param auditDataLoaderObjectMap
	 */
	public static void dumpDetailsToConsole(HashMap<Integer, AuditDataLoaderObject> auditDataLoaderObjectMap) {
		
		for (Map.Entry<Integer, AuditDataLoaderObject> entry : auditDataLoaderObjectMap.entrySet()) {
			Integer key 							= entry.getKey();
			AuditDataLoaderObject myLoaderObject 	= entry.getValue();
			System.out.println("Looking at audit "+key);
			System.out.println("   idAudit inside Loader Object                 = "+ myLoaderObject.getIdEntitlementID());
			System.out.println("   idTypeReference inside Loader Object         = "+ myLoaderObject.getIdAuditTypeReference());
			System.out.println("   Entitlement record: idEntitlement            = "+ myLoaderObject.getEntitlement().getidEntitlement());
			System.out.println("   Entitlement record: total number of entities = "+ myLoaderObject.getEntitlement().getTotalNumberOfEntities());
			System.out.println("   Entitlement record: entitlement              = "+ myLoaderObject.getEntitlement().getEntitlement());
			//System.out.println("   Entitlement record: sysLoaderAuth            = "+ myLoaderObject.getEntitlement().getSystemLoaderAuth());
			System.out.println("   Authorizers are: ");
			for ( Authorizers myAuth : myLoaderObject.getAuditAuthorizers()) {
				System.out.println("     " 
							+ myAuth.getAuthorizerEmpID() + " | " 
							+ myAuth.getAuthorizerFirstName() + " | " 
							+ myAuth.getAuthorizerLastName() + " | "
							+ myAuth.getAuthorizerType() + " | "
							+ myAuth.getEntitlement_idEntitlement());
			}
			System.out.println("   Entitites are: ");
			for ( Entities myEntitites : myLoaderObject.getAuditEntities()) {
				System.out.println("     " 
							+ myEntitites.getPrimaryKey() + " | " 
							+ myEntitites.getUDEAprimaryFieldsValues() + " | " 
							+ myEntitites.getUDEAsecondaryFieldsValues() + " | "
							+ myEntitites.getEntitlement_idEntitlement());
			}
		}
	}
	
	
}
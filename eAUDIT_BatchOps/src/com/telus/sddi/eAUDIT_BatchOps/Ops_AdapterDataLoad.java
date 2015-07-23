package com.telus.sddi.eAUDIT_BatchOps;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.telus.sddi.jEAUDITlibrary.AuditDataLoaderObject;
import com.telus.sddi.jEAUDITlibrary.AuditTypeReference;
import com.telus.sddi.jEAUDITlibrary.Authorizers;
import com.telus.sddi.jEAUDITlibrary.Entities;
import com.telus.sddi.jEAUDITlibrary.Entitlement;
import com.telus.sddi.jEAUDITlibraryUtil.AuditDataLoaderObjectUtil;
import com.telus.sddi.jEAUDITlibraryUtil.EntitlementUtil;

public class Ops_AdapterDataLoad {
	
	/**
	 * A method that takes a String, parses it and returns a Date
	 * @param dateString
	 * @return
	 */
	private static Date turnStringToDate(String dateString) {
		
		DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
		Date returnedDate = null;
		
		try {
			returnedDate 	= format.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnedDate;
		
	}
	
	/**
	 * A simple method to demonstrate how data could be loaded from an adapter into eAUDIT
	 */
	public static void loadFromAdapter() {
		
		/*
		 *  For this demo, we consider a badging system. 
		 *  	The entities to audit are badges.
		 *  		PrimaryKey is the badge number
		 *  		Primary UDEA fields are badge number, badge label and activation data
		 *  		Secondary UDEA fields are Badge type, Badge deactivation data
		 *  	The entitlements are specific access profiles attached to the badges, with specific authorizers for each profile
		 *  		General access			Authorized by Alice Fiorito empID 801234 | Arlen Morey empID 825432
		 * 			Secure access			Authorized by Reina Malpass empID 830987 | Michaela Iniguez empID 805959
		 * 			Restricted access 		Authorized by Michaela Iniguez empID 805959
		 */
		
		
		/*
		 *  Define some badges/entities  for demonstration purposes
		 *  	Typically, this is where the code interacts with the badging system to obtain the data. 
		 *  	This could be done using a service account and SQL query, a call to a vendor API, loading a csv file, etc.
		 *  In many data sources, it should be fairly simple to get a list of entities and their entitlement 
		 */
		
		ArrayList<DemoAdapterData> myEntities		 	= new ArrayList<DemoAdapterData>();
		
		// Badges with General Access
		myEntities.add(new DemoAdapterData("123456", "Guard station #1", "2014-03-04", "Generic card", "2032-03-10", "General Access"));
		myEntities.add(new DemoAdapterData("123457", "Guard station #2", "2015-03-04", "Generic card", "2032-03-10", "General Access"));
		myEntities.add(new DemoAdapterData("123458", "Pop Maching maintenance", "2013-12-11", "Generic card", "2032-03-10", "General Access"));
		myEntities.add(new DemoAdapterData("123459", "Kellee Mullet", "2014-06-21", "Contractor card", "2015-12-21", "General Access"));
		myEntities.add(new DemoAdapterData("123460", "Rayna Wight", "2014-03-04", "Employee card", "2032-03-10", "General Access"));
		myEntities.add(new DemoAdapterData("123461", "Lucio Wiles", "2015-02-02", "Employee card", "2032-03-10", "General Access"));
		myEntities.add(new DemoAdapterData("123462", "Nickolas Linke", "2015-01-10", "Contractor Card", "2015-12-21", "General Access"));
		myEntities.add(new DemoAdapterData("123463", "Lu Desantiago", "2010-03-01", "Contractor card", "2015-12-21", "General Access"));
		myEntities.add(new DemoAdapterData("123464", "Beulah Shoup", "2011-12-01", "Employee card", "2032-03-10", "General Access"));
		myEntities.add(new DemoAdapterData("123465", "Karole Harford", "2012-05-11", "Employee card", "2032-03-10", "General Access"));
		// Badges with Secure Access
		myEntities.add(new DemoAdapterData("123456", "Guard station #1", "2014-03-04", "Generic card", "2032-03-10", "Secure Access"));
		myEntities.add(new DemoAdapterData("123457", "Guard station #2", "2015-03-04", "Generic card", "2032-03-10", "Secure Access"));
		myEntities.add(new DemoAdapterData("123464", "Beulah Shoup", "2011-12-01", "Employee card", "2032-03-10", "Secure Access"));
		myEntities.add(new DemoAdapterData("123465", "Karole Harford", "2012-05-11", "Employee card", "2032-03-10", "Secure Access"));
		//Badges with Restricted Access
		myEntities.add(new DemoAdapterData("123456", "Guard station #1", "2014-03-04", "Generic card", "2032-03-10", "Restricted Access"));
		myEntities.add(new DemoAdapterData("123457", "Guard station #2", "2015-03-04", "Generic card", "2032-03-10", "Restricted Access"));
		myEntities.add(new DemoAdapterData("123460", "Rayna Wight", "2014-03-04", "Employee card", "2032-03-10", "Restricted Access"));
		myEntities.add(new DemoAdapterData("123461", "Lucio Wiles", "2015-02-02", "Employee card", "2032-03-10", "Restricted Access"));
		
		// Before we can create any Audit, Entities or Authorizers records, we need a new AuditTypeReference record
				
		AuditTypeReference myNewAuditReferenceRecord = new AuditTypeReference(
				-1,
				"Badges access levels at Main HQ", 
				"Audit the 3 main access levels assigned to badges - Main HQ", 
				castJavaUtilDateToJavaSQLdate(turnStringToDate("2015-07-01")), 
				castJavaUtilDateToJavaSQLdate(turnStringToDate("2016-05-01")), 
				"Audit instructions in English", 
				"Audit instructions in French", 
				"{\"badgeNumber\"},{\"badgeLabel\"},{\"badgeActivation\"}", 
				"UDEA primary FR", 
				"{\"badgeType\"},{\"badgeDeactivation\"}", 
				"UDEA secondary FR", 
				false, 
				"Adapter-driven", 
				0,
				"no transfer",
				null, 
				"eAUDIT_BatchOps",
				null, 
				"eAUDIT_BatchOps");

		int newAuditTypeReferenceRecordID = myNewAuditReferenceRecord.create(Main.mainDB);
		
		System.out.println("Created new AuditTypeReference record with id = " + newAuditTypeReferenceRecordID);
		
		/*
		 * Each entitlement corresponds to a specific Audit record.  It is important to note that the  i
		 */
		
		// Find the next idEntitlement
		int entitlementRecordPointer = EntitlementUtil.findMaxIDentitlementValue(Main.mainDB);
		System.out.println("We will insert new records inside the Audit table, starting at idAudit = " + ( entitlementRecordPointer + 1 ) );
		
		/*
		 *  We could load Audit records then Entities, then Authorizers, but we can also use the jEAUDITlibray AuditDataLoaderObject.
		 *  This object is used to organize all the data inside a HashMap which can then be loaded easily
		 */
		
		// This will hold all the data. The index is the idAudit for each entitlement
		HashMap<Integer, AuditDataLoaderObject> auditDataLoaderObjectMap = new HashMap<Integer, AuditDataLoaderObject>();

		//This will hold the entitlement-to-idAudit mapping
		HashMap<String, Integer> entitlementToIDmap = new HashMap<String, Integer>();
		
		/*
		 * We loop through the entitites and organize them by entitlement, generating new idAudit as required
		 */
		for ( DemoAdapterData myCurrRecord: myEntities) {
						
			// Is this a new entitlement i.e. do we need the next available idAudit?
			if ( ! entitlementToIDmap.containsKey(myCurrRecord.getEntitlement()) ) {
				// Yes, we need a new idAudit
				entitlementRecordPointer++;
				entitlementToIDmap.put(myCurrRecord.getEntitlement(), entitlementRecordPointer);
				
				// We turn this record into an Entity record and it is the first element of a new ArrayList of Entities records
				Entities myNewEntity = new Entities(
						-1, 
						myCurrRecord.getCardNumber(), 
						"{\""+myCurrRecord.getCardNumber()+"\",\""+myCurrRecord.getCardLabel()+"\","+myCurrRecord.getCardActivation()+"\"}", 
						"{\""+myCurrRecord.getCardType()+"\",\""+myCurrRecord.getCardDeactivation()+"\"}",  
						null, 
						"eAUDIT_BatchOps", 
						null, 
						"eAUDIT_BatchOps", 
						entitlementRecordPointer);
				ArrayList<Entities> entitlememtEntitites = new ArrayList<Entities> ();
				entitlememtEntitites.add(myNewEntity);
				
				/*
				 *  We build the ArrayList of Authorizers.
				 *  Note that authorizers that are provided with the audit data are not always accurate: they may be employees that have left the company.
				 *  It is recommended to leverage this particular section of the code to verify authorizers against the authoritative identity data source
				 *  for the company. This would allow to flag authorizers that are not able to perform the authorization in a generic way.
				 *  
				 *   For this demo, we simply load up the authorizers are they were defined in the data provided.
				 */
				ArrayList<Authorizers> entitlementAuthorizers = new ArrayList<Authorizers>(); 
				if ( myCurrRecord.getEntitlement().equals("General Access") ) {
					Authorizers authAlice = new Authorizers(-1, "Base", 801234, "Alice", "Fiorito", -1, null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", entitlementRecordPointer) ;
					Authorizers authArlen = new Authorizers(-1, "Base", 825432, "Arlen", "Morey", -1, null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", entitlementRecordPointer) ;
					entitlementAuthorizers.add(authAlice);
					entitlementAuthorizers.add(authArlen);
				} else if ( myCurrRecord.getEntitlement().equals("Secure Access") ) {
					Authorizers authReina = new Authorizers(-1, "Base", 830987, "Reina", "Malpass", -1, null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", entitlementRecordPointer) ;
					Authorizers authMichaela = new Authorizers(-1, "Base", 805959, "Michaela", "Iniguez", -1, null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", entitlementRecordPointer) ;
					entitlementAuthorizers.add(authReina);
					entitlementAuthorizers.add(authMichaela);
				/*
				 *  Note here that Michaela is an authorizer for two separate audits. In the current eAUDIT design, the Authorizers table is not fully normalised.
				 *  Two separate records end up being created: the Michaela who is an authorizer for Secure access and the one for Restricted access. 
				 */
				} else if ( myCurrRecord.getEntitlement().equals("Restricted Access") ) {
					Authorizers authMichaela = new Authorizers(-1, "Base", 805959, "Michaela", "Iniguez", -1, null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", entitlementRecordPointer) ;
					entitlementAuthorizers.add(authMichaela);
				}  
				
				// We build the Entitlement object
				Entitlement newAuditRecord = new Entitlement(
						entitlementRecordPointer, 
						1, 
						myCurrRecord.getEntitlement(),
						null, 
						Main.toolName, 
						null, 
						Main.toolName, 
						newAuditTypeReferenceRecordID);
				
				// We create the first AuditDataLoaderObject for this new entitlement/audit				
				AuditDataLoaderObject myNewAuditLoaderObject = new AuditDataLoaderObject(
						entitlementRecordPointer, 
						newAuditTypeReferenceRecordID, 
						newAuditRecord, 
						entitlementAuthorizers, 
						entitlememtEntitites);
				
				//We add this to the HashMap
				auditDataLoaderObjectMap.put(entitlementRecordPointer, myNewAuditLoaderObject);
				
				System.out.println(" Created [ " + myCurrRecord.getEntitlement() + " ] and inserted first record: " + myNewEntity.getUDEAprimaryFieldsValues()); 
				
			// This is not the first time we see this entitlement. We only need to add this entity to the existing entry in the HashMap
			} else {
				// We have seen this entitlement before. We only need to add the entity to an the ArrayList<Entitites> inside the HashMap
            	int idAudit = entitlementToIDmap.get(myCurrRecord.getEntitlement());
            	AuditDataLoaderObject existingAuditDataLoaderObject = auditDataLoaderObjectMap.get(idAudit);
            	
				// The records gets appended to entities
				ArrayList<Entities> existingAuditEntities = existingAuditDataLoaderObject.getAuditEntities();
				
				// We turn this record into an Entity record and add it to the ArrayList already in the HashMap
				Entities myNewEntity = new Entities(
						-1, 
						myCurrRecord.getCardNumber(), 
						"{\""+myCurrRecord.getCardNumber()+"\",\""+myCurrRecord.getCardLabel()+"\","+myCurrRecord.getCardActivation()+"\"}", 
						"{\""+myCurrRecord.getCardType()+"\",\""+myCurrRecord.getCardDeactivation()+"\"}",  
						null, 
						"eAUDIT_BatchOps", 
						null, 
						"eAUDIT_BatchOps", 
						entitlementRecordPointer);

				existingAuditEntities.add(myNewEntity);
				
				// We increment Audit.TotalNumberOfEntities by 1
				Entitlement entitlementRecord = existingAuditDataLoaderObject.getEntitlement();
				entitlementRecord.setTotalNumberOfEntities(entitlementRecord.getTotalNumberOfEntities() + 1);
				
				// And put it back into the HashMap 
				auditDataLoaderObjectMap.put(idAudit, existingAuditDataLoaderObject);
				
				System.out.println("  Added to [ " + myCurrRecord.getEntitlement() + " ] : " + myNewEntity.getUDEAprimaryFieldsValues()); 

				
			}
		}
		
		// Finally, we make use of the AuditDataLoaderObject class and call the mass loader method
		AuditDataLoaderObjectUtil.bulkCreate(auditDataLoaderObjectMap, true, Main.mainDB);
		
		
	}
	
	/**
	 * Method to translate a java.util.Date to a java.sql.Date 
	 * @param myDate
	 * @return
	 */
	private static java.sql.Date castJavaUtilDateToJavaSQLdate(java.util.Date myDate){
			long utilStartDate = myDate.getTime();
			java.sql.Date convertedDate = new java.sql.Date(utilStartDate);
			return convertedDate;
	}

}

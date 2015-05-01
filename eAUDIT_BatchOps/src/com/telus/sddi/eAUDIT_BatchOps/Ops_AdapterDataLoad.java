package com.telus.sddi.eAUDIT_BatchOps;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.telus.sddi.jEAUDITlibrary.Audit;
import com.telus.sddi.jEAUDITlibrary.AuditDataLoaderObject;
import com.telus.sddi.jEAUDITlibrary.AuditTypeReference;
import com.telus.sddi.jEAUDITlibrary.Authorizers;
import com.telus.sddi.jEAUDITlibrary.Entities;

public class Ops_AdapterDataLoad {
	
	/**
	 * A simple method to demonstrate how data could be loaded from an adapter into eAUDIT
	 */
	public static void loadFromAdapter() {
		
		/*
		 *  For this demo, we consider a badging system. 
		 *  	The entities to audit are badges.
		 *  		Primary UDEA fields are badge number, badge label and activation data
		 *  		Secondary UDEA fields are Badge type, Badge deactivation data
		 *  	The entitlements are specific access profiles attached to the badges, with specific authorizers for each profile
		 *  		General access			Authorized by Alice Fiorito empID 801234 | Arlen Morey empID 825432
		 * 			Secure access			Authorized by Reina Malpass empID 830987 | Michaela Iniguez empID 805959
		 * 			Restricted access 		Authorized by Michaela Iniguez empID 805959
		 */
		
		
		/*
		 *  We define some badges/entities 
		 *  	Typically, this is where the code interacts with the badging system to obtain the data. 
		 *  	This could be done using a service account and SQL query or an API call, or by loading a csv file, etc.
		 *  In many data source, it should be fairly simple to get the entity and its entitlement 
		 */
		
		ArrayList<DemoAdapterData> myEntities		 	= new ArrayList<DemoAdapterData>();
		
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
		
		myEntities.add(new DemoAdapterData("123456", "Guard station #1", "2014-03-04", "Generic card", "2032-03-10", "Secure Access"));
		myEntities.add(new DemoAdapterData("123457", "Guard station #2", "2015-03-04", "Generic card", "2032-03-10", "Secure Access"));
		myEntities.add(new DemoAdapterData("123464", "Beulah Shoup", "2011-12-01", "Employee card", "2032-03-10", "Secure Access"));
		myEntities.add(new DemoAdapterData("123465", "Karole Harford", "2012-05-11", "Employee card", "2032-03-10", "Secure Access"));
		
		myEntities.add(new DemoAdapterData("123456", "Guard station #1", "2014-03-04", "Generic card", "2032-03-10", "Restricted Access"));
		myEntities.add(new DemoAdapterData("123457", "Guard station #2", "2015-03-04", "Generic card", "2032-03-10", "Restricted Access"));
		myEntities.add(new DemoAdapterData("123460", "Rayna Wight", "2014-03-04", "Employee card", "2032-03-10", "Restricted Access"));
		myEntities.add(new DemoAdapterData("123461", "Lucio Wiles", "2015-02-02", "Employee card", "2032-03-10", "Restricted Access"));
		
		// Before we can create any Audit, Entities or Authorizers records, we need a new AuditTypeReference record
		DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate 	= format.parse("2015-04-01");
			endDate 	= format.parse("2015-05-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		AuditTypeReference myNewAuditReferenceRecord = new AuditTypeReference(
				-1,
				"Badges access levels at Main HQ", 
				"Audit the 3 main access levels assigned to badges - Main HQ", 
				castJavaUtilDateToJavaSQLdate(startDate), 
				castJavaUtilDateToJavaSQLdate(endDate), 
				"Audit instructions in English", 
				"Audit instructions in French", 
				"{\"badgeNumber\"},{\"badgeLabel\"},{\"badgeActivation\"}", 
				"UDEA primary FR", 
				"{\"badgeType\"},{\"badgeDeactivation\"}", 
				"UDEA secondary FR", 
				false, 
				"Adapter-driven", 
				0,
				false, 
				null, 
				"eAUDIT_BatchOps",
				null, 
				"eAUDIT_BatchOps");

		int newAuditTypeReferenceRecordID = myNewAuditReferenceRecord.create(Main.mainDB);
		
		System.out.println("Created new AuditTypeReference record with id = " + newAuditTypeReferenceRecordID);
		
		/*
		 * Each entitlement corresponds to a specific Audit record.  It is important to note that the primary key of the Audit table, idAudit,
		 * is not an auto-increment field. This allows for the controlled creation of records starting at a specific value
		 */
		
		// Find the next idAudit
		int auditRecordPointer = Audit.findMaxIDauditValue(Main.mainDB);
		System.out.println("We can insert new records inside the Audit table, starting at idAudit = " + auditRecordPointer++ );
		
		/*
		 *  We could load Audit records then Entities, then Authorizers, but we can also use the jEAUDITlibray AuditDataLoaderObject.
		 *  This object is used to load organize all the data inside a HashMap which can then be loaded easily
		 */
		
		// This will hold all the data. The index represent the idAudit for each entitlement
		HashMap<Integer, AuditDataLoaderObject> auditDataLoaderObjectMap = new HashMap<Integer, AuditDataLoaderObject>();

		//This will hold the entitlement-to-ID mapping
		HashMap<String, Integer> entitlementToIDmap = new HashMap<String, Integer>();
		
		/*
		 * We loop through the entitites and organize them by entitlement, generating new idAudit as required
		 */
		for ( DemoAdapterData myCurrRecord: myEntities) {
						
			// Is this a new entitlement i.e. do we need the next available idAudit?
			
			if ( ! entitlementToIDmap.containsKey(myCurrRecord.getEntitlement()) ) {
				// Yes, we need a new idAudit
				auditRecordPointer++;
				entitlementToIDmap.put(myCurrRecord.getEntitlement(), auditRecordPointer);
				
				// We turn this record into an Entity record and it is the first element of a new ArrayList of Entitie records
				Entities myNewEntity = new Entities(
						-1, 
						myCurrRecord.getCardNumber(), 
						"{\""+myCurrRecord.getCardNumber()+"\",\""+myCurrRecord.getCardLabel()+"\","+myCurrRecord.getCardActivation()+"\"}", 
						"{\""+myCurrRecord.getCardType()+"\",\""+myCurrRecord.getCardDeactivation()+"\"}",  
						null, 
						"eAUDIT_BatchOps", 
						null, 
						"eAUDIT_BatchOps", 
						auditRecordPointer);
				ArrayList<Entities> entitlememtEntitites = new ArrayList<Entities> ();
				entitlememtEntitites.add(myNewEntity);
				
				// We build the ArrayList of Authorizers
				ArrayList<Authorizers> entitlementAuthorizers = new ArrayList<Authorizers>(); 
				if ( myCurrRecord.getEntitlement().equals("General access") ) {
					Authorizers authAlice = new Authorizers(-1, "Base", 801234, "Alice", "Fiorito", null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", auditRecordPointer) ;
					Authorizers authArlen = new Authorizers(-1, "Base", 825432, "Arlen", "Morey", null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", auditRecordPointer) ;
					entitlementAuthorizers.add(authAlice);
					entitlementAuthorizers.add(authArlen);
				} else if ( myCurrRecord.getEntitlement().equals("Secure access") ) {
					Authorizers authReina = new Authorizers(-1, "Base", 830987, "Reina", "Malpass", null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", auditRecordPointer) ;
					Authorizers authMichaela = new Authorizers(-1, "Base", 805959, "Michaela", "Iniguez", null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", auditRecordPointer) ;
					entitlementAuthorizers.add(authReina);
					entitlementAuthorizers.add(authMichaela);
				} else if ( myCurrRecord.getEntitlement().equals("Restricted access") ) {
					Authorizers authMichaela = new Authorizers(-1, "Base", 805959, "Michaela", "Iniguez", null, "eAUDIT_BatchOps", null, "eAUDIT_BatchOps", auditRecordPointer) ;
					entitlementAuthorizers.add(authMichaela);
				}  
				
				// We build the Audit object
				
				
				
				// We create the first AuditDataLoaderObject for this new entitlement/audit
								
				AuditDataLoaderObject myNewAuditLoaderObject = new AuditDataLoaderObject(
						auditRecordPointer, 
						auditRecordPointer, 
						null, 
						entitlementAuthorizers, 
						entitlememtEntitites);
				

				
//				public AuditDataLoaderObject(
//						int idAuditID, 
//						int idAuditTypeReference,
//						Audit audit,
//						ArrayList<Authorizers> auditAuthorizers,
//						ArrayList<Entities> auditEntities) {
//					super();
//					this.idAuditID 				= idAuditID;
//					this.idAuditTypeReference 	= idAuditTypeReference;
//					this.setAudit(audit);
//					this.auditAuthorizers 		= auditAuthorizers;
//					this.auditEntities 			= auditEntities;
				
				
			}
		}
		
		
		
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

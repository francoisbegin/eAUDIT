package com.telus.sddi.eAUDIT_BatchOps;

import java.util.ArrayList;

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
		 *  
		 *  Three ArrayList are defined to clearly illustrate the 3 entitlements groups (and their specific authorizers)
		 */
		
		ArrayList<DemoAdapterData> myEntitiesGeneralAccess		 	= new ArrayList<DemoAdapterData>();
		ArrayList<DemoAdapterData> myEntitiesSecureAccess 			= new ArrayList<DemoAdapterData>();
		ArrayList<DemoAdapterData> myEntitiesRestrictedAccess 		= new ArrayList<DemoAdapterData>();
		
		myEntitiesGeneralAccess.add(new DemoAdapterData("123456", "Guard station #1", "2014-03-04", "Generic card", "2032-03-10"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123457", "Guard station #2", "2015-03-04", "Generic card", "2032-03-10"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123458", "Pop Maching maintenance", "2013-12-11", "Generic card", "2032-03-10"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123459", "Kellee Mullet", "2014-06-21", "Contractor card", "2015-12-21"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123460", "Rayna Wight", "2014-03-04", "Employee card", "2032-03-10"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123461", "Lucio Wiles", "2015-02-02", "Employee card", "2032-03-10"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123462", "Nickolas Linke", "2015-01-10", "Contractor Card", "2015-12-21"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123463", "Lu Desantiago", "2010-03-01", "Contractor card", "2015-12-21"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123464", "Beulah Shoup", "2011-12-01", "Employee card", "2032-03-10"));
		myEntitiesGeneralAccess.add(new DemoAdapterData("123465", "Karole Harford", "2012-05-11", "Employee card", "2032-03-10"));
		
		myEntitiesSecureAccess.add(new DemoAdapterData("123456", "Guard station #1", "2014-03-04", "Generic card", "2032-03-10"));
		myEntitiesSecureAccess.add(new DemoAdapterData("123457", "Guard station #2", "2015-03-04", "Generic card", "2032-03-10"));
		myEntitiesSecureAccess.add(new DemoAdapterData("123464", "Beulah Shoup", "2011-12-01", "Employee card", "2032-03-10"));
		myEntitiesSecureAccess.add(new DemoAdapterData("123465", "Karole Harford", "2012-05-11", "Employee card", "2032-03-10"));
		
		myEntitiesRestrictedAccess.add(new DemoAdapterData("123456", "Guard station #1", "2014-03-04", "Generic card", "2032-03-10"));
		myEntitiesRestrictedAccess.add(new DemoAdapterData("123457", "Guard station #2", "2015-03-04", "Generic card", "2032-03-10"));
		myEntitiesRestrictedAccess.add(new DemoAdapterData("123460", "Rayna Wight", "2014-03-04", "Employee card", "2032-03-10"));
		myEntitiesRestrictedAccess.add(new DemoAdapterData("123461", "Lucio Wiles", "2015-02-02", "Employee card", "2032-03-10"));
		
		
		
		
		
		
	}

}

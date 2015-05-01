package com.telus.sddi.eAUDIT_BatchOps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.telus.sddi.jEAUDITlibrary.Audit;
import com.telus.sddi.jEAUDITlibrary.AuditDataLoaderObject;
import com.telus.sddi.jEAUDITlibrary.AuditTypeReference;
import com.telus.sddi.jEAUDITlibrary.Authorizers;
import com.telus.sddi.jEAUDITlibrary.Entities;

public class Ops_ExcelDataLoader {

	
	/**
	 * Method that turns an ArrayList of Strings into a JSON
	 * @param myArrayListOfString
	 * @return
	 */
	private static String turnArrayListToJSON (ArrayList<String> myArrayListOfString) {
		String returnedJSON = "{";
		
		for ( String myStringInArray : myArrayListOfString ) {
			returnedJSON += "\"" + myStringInArray + "\",";
		}
		
		returnedJSON = returnedJSON.substring(0,returnedJSON.length()-1);
		returnedJSON += "}";
		
		return returnedJSON;
	}
	
	
	
	/**
	 * Method that takes the data found in the AuditData sheet of an eAUDIT spreadsheet template and turn these data into a HashMap<Integer, AuditDataLoaderObject>.
	 * That HashMap in turn can easily be loaded into the eAUDIT database using the AuditDataLoaderObject.massLoad() method.
	 * @param sheet
	 * @param newAuditTypeReferenceID
	 * @return
	 */
	@SuppressWarnings("unused")
	private static HashMap<Integer, AuditDataLoaderObject> translateAudtiDataRecordsFromExcelSheetToHashMap(XSSFSheet sheet, int newAuditTypeReferenceID) {
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		// This is used to tell the first row apart from the other rows. 
		int rowCounter = 1;
		
		// This is where we will keep the position of the primary/secondary UDEA fields
		HashSet<Integer> positionOfUDEAprimaryFields = new HashSet<Integer>();
		HashSet<Integer> positionOfUDEAsecondaryFields = new HashSet<Integer>(); 
		
		
		// These are used to ensure we have the right fields in this sheet
		Boolean primaryKeyHeaderFound 		= false;
		Boolean authorizersHeaderFound 		= false;
		int numOfUDEAprimaryHeadersFound	= 0;
		int numOfUDEAsecondaryHeadersFound	= 0;
		
		 /*
         * This map will hold all of the data from AuditData into a HashMap. It is important to remember here that all entries inside the Audit table are 
         * sub-audits of the larger AuditTypeReference record. Therefore the index of the HashMap will be idAudit, which will need to be reserved and selected
         * prior to the data load. Once the HashMap has been created, loading the data will be very easy.  
         */
		HashMap<Integer, AuditDataLoaderObject> auditDataLoaderObjectMap = new HashMap<Integer, AuditDataLoaderObject>();
		
		// Where need to know where to start inserting records in the Audit table i.e. the next available value for idAudit
		int idAuditPointer = Audit.findMaxIDauditValue(Main.mainDB);
		idAuditPointer++;
		
		/*
		 * All unique entitlements represent a sub-audit of the AuditTypeReference, so we keep a map of entitlements & idAudit to help create auditDataLoaderObjectMap 
		 */
		HashMap<String, Integer> entitlementToIDauditMap = new HashMap<String, Integer>(); 
		
 
		// Traverse over each row of XLSX file
        while (rowIterator.hasNext()) {
        	Row row = rowIterator.next();
        	
        	//System.out.println("Processing row " + rowCounter);
        	
        	/**
        	 * This first row has the field headers. We expect to find
        	 * 		PrimaryKey
        	 * 		Entitlement
        	 * 		Authorizers
        	 * 		one or more UDEAprimary 
        	 * 		one or more UDEAsecondary
        	 * This method will auto-adjust based on that first row so that the primary and secondary UDEA are organized and saved correctly. Note that the
        	 * exact order in which these fields are presented must match the eact order in which the UDEAprimaryFieldsEN/FR and UDEAsecondaryFieldsFR where
        	 * defined in the AuditTypeRef sheet  
        	 */
        	if ( rowCounter == 1 ) {
        		
                Iterator<Cell> cellIterator = row.cellIterator();
                
                int fieldCounter = 1;
                
                while (cellIterator.hasNext()) {
                	
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                    	if ( cell.getStringCellValue().equals("PrimaryKey")) {
                    		primaryKeyHeaderFound 	= true;
                    	} else if ( cell.getStringCellValue().equals("Authorizers")) {
                    		authorizersHeaderFound 	= true;
                    	} else if ( cell.getStringCellValue().equals("UDEAprimary")) {
                    		numOfUDEAprimaryHeadersFound++;
                    		positionOfUDEAprimaryFields.add(fieldCounter);
                    	} else if ( cell.getStringCellValue().equals("UDEAsecondary")) {
                    		numOfUDEAsecondaryHeadersFound++;
                    		positionOfUDEAsecondaryFields.add(fieldCounter);
                    	} 
                        break;
                    default :
                 
                    }
                    
                    fieldCounter++;
               
                }
                
                rowCounter++;
        		
        	/**
        	 * The second row is where the data is kept. At this point, we have processed the first row and we know where the UDEAprimary and UDEAsecondary
        	 * fields are found thanks to HashSet<Integer> positionOfUDEAprimaryFields and HashSet<Integer> positionOfUDEAsecondaryFields
        	 */
        	} else {
        		
                Iterator<Cell> cellIterator = row.cellIterator();
                
                String primaryKey = "";
                String entitlement = "";
                String authorizers = "";
                String uDEAprimary = "";
                String uDEAsecondary = "";
                
                /*
                 * This keeps track of the field position (first, second, etc.) in the sheet. It gets correlated to HashSets positionOfUDEAprimaryFields 
                 * and positionOfUDEAsecondaryFields to determine if a value is a priary or secondary UDEA.
                 */
                int fieldCounter = 1;
                
                // These will hold the key values we are extracting from the spreadsheet
                ArrayList<String> authorizersArray = new ArrayList<String>();
                ArrayList<String> primaryUDEAarray = new ArrayList<String>();
                ArrayList<String> secondaryUDEAarray = new ArrayList<String>();
                
                while (cellIterator.hasNext()) {
                	
                    Cell cell = cellIterator.next();
                    
                    String cellContent = "";
                    
                    // We only deal with strings
                    switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                    	cellContent = cell.getStringCellValue();
                    	break;
                    case Cell.CELL_TYPE_BLANK:
                    	//
                    	break;
                    case Cell.CELL_TYPE_BOOLEAN:
                    	//
                    	break;
                    case Cell.CELL_TYPE_NUMERIC:
                    	cellContent = Double.toString(cell.getNumericCellValue());
                    	break;
                    }
                                      
                    if ( fieldCounter == 1 ) {
                    	primaryKey = cellContent;
                    } else if ( fieldCounter == 2 ) {
                    	entitlement = cellContent;
                    } else if ( fieldCounter == 3 ) {
                    	String[] authorizersStringArr = cellContent.split(";");
                    	for ( String myAuth : authorizersStringArr) {
                    		authorizersArray.add(myAuth);
                    	}
                    } else if ( positionOfUDEAprimaryFields.contains(fieldCounter) ) {
                    	primaryUDEAarray.add(cellContent);
                    } else if ( positionOfUDEAsecondaryFields.contains(fieldCounter) ) {
                    	secondaryUDEAarray.add(cellContent);
                    }
                    
                    fieldCounter++;
                    
                }
                
            	// At this point, we have fully read a data row and we can add it to auditDataLoaderObjectMap - either it's for a brand new audit/entitlement or we append to an existing one
                
        		Entities myRowRecordTranslatedIntoAnEntity = new Entities(
        				-1, 
        				primaryKey, 
        				turnArrayListToJSON(primaryUDEAarray),
        				turnArrayListToJSON(secondaryUDEAarray),
        				null, 
        				Main.toolName, 
        				null, 
        				Main.toolName, 
        				-1);	
                
                
                // We have seen this entitlement/audit before
                if ( entitlementToIDauditMap.containsKey(entitlement)) {
                	// Known entitlement, we need to add that row to an ecisting HashMap
                	int idAudit = entitlementToIDauditMap.get(entitlement);
                	AuditDataLoaderObject existingAuditDataLoaderObject = auditDataLoaderObjectMap.get(idAudit);
                	
    				// The records gets appended to entities
    				ArrayList<Entities> existingAuditEntities = existingAuditDataLoaderObject.getAuditEntities();
    				myRowRecordTranslatedIntoAnEntity.setAudit_idAudit(idAudit);
    				existingAuditEntities.add(myRowRecordTranslatedIntoAnEntity);
    				
    				// We need to increment the Audit.TotalNumberOfEntities by 1
    				Audit auditRecord = existingAuditDataLoaderObject.getAudit();
    				auditRecord.setTotalNumberOfEntities(auditRecord.getTotalNumberOfEntities() + 1);
    				
    				// And put it back into the HashMap 
    				auditDataLoaderObjectMap.put(idAudit, existingAuditDataLoaderObject);         	

    			// First time we see this object, we need to create it
                } else {
                	
    				// Update the idAuditPointer as this is a new Audit record
    				idAuditPointer++;
    				
    				// Add to entitlement-to-idAudit map
    				entitlementToIDauditMap.put(entitlement, idAuditPointer);
    				
    				// Create new ArrayList of Authorizers and add this manager
    				ArrayList<Authorizers> thisAuditAuthorizers = new ArrayList<Authorizers>();
    				
    				/*
    				 * To get a complete Authorizer record, you would need to work slightly harder here and come up with a mechanism to look up an
    				 * authorizer's details (first name and last name) in an authoritative data source. For this particular code, we just get
    				 * the employee IDs in AUthorizers
    				 */
  				
    				for ( String myAuthEmpID : authorizersArray ) {
    					Authorizers myNewAuthorizer = new Authorizers(
    							-1, 
    							"Base", 
    							Integer.parseInt(myAuthEmpID), 
    							"Need to look auth first name up", 
    							"Need to look auth last name up", 
    							null, 
    							Main.toolName, 
    							null, 
    							Main.toolName, 
    							idAuditPointer);

        				thisAuditAuthorizers.add(myNewAuthorizer);
    					
    				}

    				// We need to define the Audit record for the first time		
    				Audit entitlementAuditRecord = new Audit(
    						idAuditPointer, 
    						1, 
    						entitlement, 
    						"internal user field",
    						null, 
    						Main.toolName, 
    						null, 
    						Main.toolName, 
    						newAuditTypeReferenceID);
    				
    				// This current row record is the first entity for this audit
    				ArrayList<Entities> newListOfEntities = new ArrayList<Entities>(); 
    				myRowRecordTranslatedIntoAnEntity.setAudit_idAudit(idAuditPointer);
    				newListOfEntities.add(myRowRecordTranslatedIntoAnEntity);
    				
    				// Create new AuditDataLoaderObject
    				AuditDataLoaderObject newAuditDataLoaderObject = new AuditDataLoaderObject(
    						idAuditPointer, 
    						newAuditTypeReferenceID, 
    						entitlementAuditRecord,
    						thisAuditAuthorizers, 
    						newListOfEntities);
    				
    				// And put it into the HashMap 
    				auditDataLoaderObjectMap.put(idAuditPointer, newAuditDataLoaderObject);		                	
                          	
                }
           
                rowCounter++;
                
        	}
        }
        
        return auditDataLoaderObjectMap;
        
	}

	/**
	 * A method that reads an Excel sheet where all fields for a new AuditTypeReference object can be found 
	 * @param sheet
	 * @return
	 */
	private static int createNewAuditTypeReferenceRecordFromExcelSheet(XSSFSheet sheet) {
		
		int newAuditTypeReferenceID = -1;
				
		// These are the values we are looking for
		String auditName 				= null;
		String auditDescription 		= null;
		String auditInstructionsEN 		= null;
		String auditInstructionsFR 		= null;
		String uDEAprimaryFieldsEN 		= null;
		String uDEAprimaryFieldsFR 		= null;
		String uDEAsecondaryFieldsEN 	= null;
		String uDEAsecondaryFieldsFR 	= null;
		Date auditStart 				= null;
		Date auditEnd 					= null;
		Boolean auditByManager 			= null;
		String dataLoadType 			= null;
		int eMACyclesTimelineID			= 0;
		Boolean useEmac					= null;
		String auditManager 			= null;
	
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		int checkCounter = 0;
		
        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();

                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                	if ( cell.getStringCellValue().equals("AuditName")) {
                		auditName					= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("AuditDescription")) {
                		auditDescription			= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("AuditInstructionsEN")) {
                		auditInstructionsEN			= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("AuditInstructionsFR")) {
                		auditInstructionsFR			= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("UDEAprimaryFieldsEN")) {
                		uDEAprimaryFieldsEN			= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("UDEAprimaryFieldsFR")) {
                		uDEAprimaryFieldsFR			= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("UDEAsecondaryFieldsEN")) {
                		uDEAsecondaryFieldsEN		= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("UDEAsecondaryFieldsFR")) {
                		uDEAsecondaryFieldsFR		= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("AuditStart")) {
                		auditStart					= (Date) cellIterator.next().getDateCellValue();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("AuditEnd")) {
                		auditEnd					= (Date) cellIterator.next().getDateCellValue();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("AuditByManager")) {
                		if ( cellIterator.next().getNumericCellValue() == 1 ) {
                			auditByManager = true;
                		} else {
                		    auditByManager = false;
                		}
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("DataLoadType")) {
                		dataLoadType				= cellIterator.next().toString();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("eMACCyclesTimelineID")) {
                		eMACyclesTimelineID			= (int) cellIterator.next().getNumericCellValue();
                		checkCounter++;
                	} else if ( cell.getStringCellValue().equals("UseEMAC")) {
                		if ( cellIterator.next().getNumericCellValue() == 1 ) {
                			useEmac = true;
                		} else {
                			useEmac = false;
                		}
                		checkCounter++;
                	}  else if ( cell.getStringCellValue().equals("AuditManager")) {
                		auditManager				= cellIterator.next().toString();
                		checkCounter++;
                	}    
                    break;
                default :
             
                }
            }
        }
        
        // Did we get all the expected values from the template?
        if ( checkCounter != 15) {
        	System.out.println("The number of expected values from the template [15] does not match what was found ["+checkCounter+"]. Please ensure you have the latest eAUDIT Excel data load template. Data load FAILED");
        	Main.cleanUp();
        	System.exit(0);
        } else {
        	
    		AuditTypeReference myNewAuditReferenceRecord = new AuditTypeReference(
    				-1, 
    				auditName, 
    				auditDescription, 
    				castJavaUtilDateToJavaSQLdate(auditStart), 
    				castJavaUtilDateToJavaSQLdate(auditEnd), 
        			auditInstructionsEN,
        			auditInstructionsFR,
        			uDEAprimaryFieldsEN,
        			uDEAprimaryFieldsFR,
        			uDEAsecondaryFieldsEN,
        			uDEAsecondaryFieldsFR,
        			auditByManager,
        			dataLoadType,
        			eMACyclesTimelineID, 
    				useEmac, 
    				null, 
    				auditManager, 
    				null, 
    				auditManager);     
    				
    		newAuditTypeReferenceID = myNewAuditReferenceRecord.create(Main.mainDB);
      		
        }
				
				
		return newAuditTypeReferenceID; 
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
	
	
	/**
	 * A method that takes a special Excel template and loads up all the data into the eAUDIT database
	 * @param fullPathToSpreadsheet
	 */
	public static void loadFromExcelTemplate(String fullPathToSpreadsheet ) {
		
		FileInputStream file;
		try {
			file = new FileInputStream(new File(fullPathToSpreadsheet));
			//Get the workbook instance for XLS file
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook (file);
			
			// We expect specific Sheet Names as this is a tempalte
			
			if ( workbook.getSheetName(0).equals("AuditTypeRef") 
					&& workbook.getSheetName(1).equals("AuditData") ) {
				
				System.out.println("Processing sheet "+workbook.getSheetName(0));
				
				//Get first sheet to create AuditTypeReference object
				XSSFSheet sheetAuditTypeRef = workbook.getSheetAt(0);
				
				// Create AuditReferenceTypeRecord from what is found in this sheet
				int newAuditTypeReferenceID = createNewAuditTypeReferenceRecordFromExcelSheet(sheetAuditTypeRef);
				
		   		if ( newAuditTypeReferenceID < 0 ) {
					System.out.println("Error creating new AuditTypeReference record. Data load FAILED");
					Main.cleanUp();
					System.exit(0);
				}
				
				// We can proceed with the data load since the auditTypeReference object was successfully created. Get the second sheet to find data details
		   		System.out.println("Processing sheet "+workbook.getSheetName(1));
		   		XSSFSheet sheetAuditData = workbook.getSheetAt(1);
		   		
		   		// We turn that sheet into a HashMap for which we have an easy method to load into the db
		   		HashMap<Integer, AuditDataLoaderObject> sheetDataHashMap = translateAudtiDataRecordsFromExcelSheetToHashMap(sheetAuditData, newAuditTypeReferenceID);
		   		System.out.println("Sheet processed. There are " + sheetDataHashMap.size() + " separate entitlement audits to load for this AuditTypeReference record # " + newAuditTypeReferenceID );
		   		
		   		// Finally, we can leverage the jEAUDIT library to mass-load these data into the database
		   		AuditDataLoaderObject.massLoad(sheetDataHashMap, true, Main.mainDB);
		   		
		   		
		   			
			} else {
				System.out.println("Sheet do not match the template. Please ensure you have the latest eAUDIT Excel data load template. Data load FAILED");
            	Main.cleanUp();
            	System.exit(0);
			}
			
			
			
		} catch (FileNotFoundException e) {
			System.out.println("File " + fullPathToSpreadsheet + " not found. Processing cannot proceed: ");
        	Main.cleanUp();
        	System.exit(0);
		} catch (IOException e) {
			System.out.println("An unexpected error occured. Processing cannot proceed: ");
			e.printStackTrace();
        	Main.cleanUp();
        	System.exit(0);
		}

		
	}
}

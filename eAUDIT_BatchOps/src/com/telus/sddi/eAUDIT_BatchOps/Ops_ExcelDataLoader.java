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

import com.telus.sddi.UnifiedToolBoxV2.Parser;
import com.telus.sddi.jEAUDITlibrary.AuditDataLoaderObject;
import com.telus.sddi.jEAUDITlibrary.AuditTypeReference;
import com.telus.sddi.jEAUDITlibrary.Authorizers;
import com.telus.sddi.jEAUDITlibrary.Entities;
import com.telus.sddi.jEAUDITlibrary.Entitlement;
import com.telus.sddi.jEAUDITlibraryUtil.AuditDataLoaderObjectUtil;
import com.telus.sddi.jEAUDITlibraryUtil.AuditTypeReferenceUtil;
import com.telus.sddi.jEAUDITlibraryUtil.EntitlementUtil;


public class Ops_ExcelDataLoader {
	
	

	/**
	 * Method that turns an ArrayList of Strings into a JSON
	 * @param myArrayListOfString
	 * @return
	 */
	private static String turnArrayListToJSON (ArrayList<String> myArrayListOfString) {
		
		String returnedJSON = "";
		
		if ( myArrayListOfString.size() > 0 ) {
			returnedJSON = "{";
			
			for ( String myStringInArray : myArrayListOfString ) {
				returnedJSON += "\"" + myStringInArray + "\",";
			}
			
			returnedJSON = returnedJSON.substring(0,returnedJSON.length()-1);
			returnedJSON += "}";
		} else {
			returnedJSON = "{}";
		}
				
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
	private static HashMap<Integer, AuditDataLoaderObject> translateAuditDataRecordsFromExcelSheetToHashMap(XSSFSheet sheet, int newAuditTypeReferenceID) {
		
		// Get all details about the new AuditTypeReference record
		AuditTypeReference newAuditTypeReference = AuditTypeReferenceUtil.get(newAuditTypeReferenceID, Main.mainDB);
			
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
         * This map will hold all of the data from AuditData into a HashMap. It is important to remember here that all entries inside the Entitlement table are 
         * sub-audits of the larger AuditTypeReference record. Therefore the index of the HashMap will be idEntitlement, which will need to be reserved and selected
         * prior to the data load. Once the HashMap has been created, loading the data will be very easy. 
         * 
         * This map's index is idEntitlement as the Entitlement table is the one all others revolve around
         */
		HashMap<Integer, AuditDataLoaderObject> auditDataLoaderObjectMap = new HashMap<Integer, AuditDataLoaderObject>();
		
		// Where need to know where to start inserting records in the Entitlement table i.e. the next available value for idEntitlement
		int idEntitlementPointer = EntitlementUtil.findMaxIDentitlementValue(Main.mainDB);
		idEntitlementPointer++;
		
		/*
		 * All unique entitlements represent a sub-audit of the AuditTypeReference, so we keep a map of entitlements & idEntitlement to help create auditDataLoaderObjectMap 
		 */
		HashMap<String, Integer> entitlementToIDauditMap = new HashMap<String, Integer>(); 
		
 
		// Traverse over each row of XLSX file
        while (rowIterator.hasNext()) {
        	Row row = rowIterator.next();
        	
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
                       
                    // Ensure that the cell is considered as a STRING. It will be converted to numeric/boolean as required
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                   
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
                    
                    // Ensure that the cell is considered as a STRING. It will be converted to numeric/boolean as required
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    
                    String cellContent = cell.getStringCellValue();
                                                         
                    if ( fieldCounter == 1 ) {
                    	primaryKey = cellContent;
                    } else if ( fieldCounter == 2 ) {
                    	
        				/*
        				 * There are 2 distinct cases here:
        				 * 
        				 *    1. AuditTypeReference.AuditByManager = FALSE
        				 *    		We expect the spreadsheet to clearly provide entitlement names under the Entitlement column.
        				 *    2. AuditTypeReference.AuditByManager = TRUE
        				 *    		We expect the spreadsheet to clearly provide entitlement names under the Entitlement column BUT entitlements are manager specific. In other
        				 *          words, if the entitlement is 'Is a user of System A', then this needs to be changed to 'Is a user of system A reporting to [EmpID of mgr]'
        				 *          
                    	 * IMPORTANT NOTE:
                    	 * The code to handle audits by managers is not provided here as each enterprise has its own repository of hierarchical data. With that said, it
                    	 * should be trivial to implement this by
                    	 * 			Obtaining a HashMap of employee IDs-to-managerID from an authoritative source
                    	 * 			Looking up the primaryKey field in that map
                    	 * 			Updating the entitlement field accordingly
                    	 * 
                    	 * Here is some code for this
                    	 * 
                    	 *  if ( newAuditTypeReference.getAuditByManager() ) {
         						int authorizerEmpID			= 0;
    							if ( myCEDv2ReferenceMap.containsKey(Integer.parseInt(primaryKey) )) {
    								authorizerEmpID 	= Parser.returnNumericValue(myCEDv2ReferenceMap.get(Integer.parseInt(primaryKey)).getManagerPersonnelNumber());
    							}
    							entitlement = cellContent + " reporting to ["+authorizerEmpID+"]";
         					} else {
         						entitlement = cellContent;	
         					}
                    	 * 
                    	 */

                    	// This is the default if you do not check for audits where AuditByManager = TRUE
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
                	int idEntitlement = entitlementToIDauditMap.get(entitlement);
                	AuditDataLoaderObject existingAuditDataLoaderObject = auditDataLoaderObjectMap.get(idEntitlement);
                	
    				// The records gets appended to entities
    				ArrayList<Entities> existingAuditEntities = existingAuditDataLoaderObject.getAuditEntities();
    				myRowRecordTranslatedIntoAnEntity.setEntitlement_idEntitlement(idEntitlement);
    				existingAuditEntities.add(myRowRecordTranslatedIntoAnEntity);
    				
    				// We need to increment the Audit.TotalNumberOfEntities by 1
    				Entitlement entitlementRecord = existingAuditDataLoaderObject.getEntitlement();
    				entitlementRecord.setTotalNumberOfEntities(entitlementRecord.getTotalNumberOfEntities() + 1);
    				
    				// And put it back into the HashMap 
    				auditDataLoaderObjectMap.put(idEntitlement, existingAuditDataLoaderObject);         	

    			// First time we see this object, we need to create it
                } else {
                	
    				// Update the idEntitlementPointer as this is a new Audit record
    				idEntitlementPointer++;
    				
    				// Add to entitlement-to-idEntitlement map
    				entitlementToIDauditMap.put(entitlement, idEntitlementPointer);
    				
    				// Create new ArrayList of Authorizers and add this manager
    				ArrayList<Authorizers> thisAuditAuthorizers = new ArrayList<Authorizers>();
    				
    				
    				/*
    				 * There are 2 distinct cases here:
    				 * 
    				 *    1. AuditTypeReference.AuditByManager = FALSE
    				 *    		We expect the spreadsheet to provide one or more Authorizers under the Authorizer column of the AuditData sheet.
    				 *    		These values are expected to be employee IDs of authorizers, which will need to be matched to names (below)
    				 *    2. AuditTypeReference.AuditByManager = TRUE
    				 *    		We do not expect any Authorizers from the spreadsheet. The PrimaryKey of each entity record is the employee ID and we
    				 *          look up the employee manager, and take this person the de facto Authorizer for the entity 
    				 *          
    				 *          
    				 * IMPORTANT NOTE:
                     * The code to handle looking up managers details or simply their names is not provided here as each enterprise has its own repository 
                     * of hierarchical data. With that said, it should be trivial to implement this by
                     * 			Obtaining a HashMap of employee IDs-to-manager details from an authoritative source
                     * 			Looking up the primaryKey field in that map
                     * 			Updating the authorizers ArrayList accordingly
                     * 
                     * Here is some code for this
                      
                       if ( ! newAuditTypeReference.getAuditByManager() ) {
    				 		for ( String myAuthEmpID : authorizersArray ) {
    				 			String authorizerFirstName 	= "Not in hierarchy";
					 			String authorizerLastName 	= "UNKNOWN";
					 			int authorizerManagerEmpID	= 0;
					 			
					 			if ( myEmployeeToManagerMAP.containsKey(Parser.returnNumericValue(myAuthEmpID) )) {
					 				authorizerFirstName 	= myEmployeeToManagerMAP.get(Parser.returnNumericValue(myAuthEmpID)).getPreferredFirstName();
					 				authorizerLastName 		= myEmployeeToManagerMAP.get(Parser.returnNumericValue(myAuthEmpID)).getLastName();
					 				authorizerManagerEmpID	= Parser.returnNumericValue(myEmployeeToManagerMAP.get(Parser.returnNumericValue(myAuthEmpID)).getManagerPersonnelNumber());
					 			}
					 			Authorizers myNewAuthorizer = new Authorizers(
									-1, 
									"Prime", 
									Parser.returnNumericValue((myAuthEmpID)), 
									authorizerFirstName, 
									authorizerLastName, 
									authorizerManagerEmpID, 
									null, 
									Main.toolName, 
									null, 
									Main.toolName, 
									idEntitlementPointer);

    				 			thisAuditAuthorizers.add(myNewAuthorizer);	
        			 		}
    				 	} else {
    				 		String authorizerFirstName 	= "Not in hierarchy";
					 		String authorizerLastName 	= "UNKNOWN";
					 		String myAuthEmpID			= "0";
					 		
					 		if ( myEmployeeToManagerMAP.containsKey(  Parser.returnNumericValue(primaryKey))  ) {
					 				authorizerFirstName 	= myEmployeeToManagerMAP.get(Parser.returnNumericValue(primaryKey)).getPreferredFirstName();
					 				authorizerLastName 		= myEmployeeToManagerMAP.get(Parser.returnNumericValue(primaryKey)).getLastName();
					 				authorizerManagerEmpID	= Parser.returnNumericValue(myEmployeeToManagerMAP.get(Parser.returnNumericValue(primaryKey)).getManagerPersonnelNumber());
					 		}
					 			Authorizers myNewAuthorizer = new Authorizers(
									-1, 
									"Prime", 
									Parser.returnNumericValue((primaryKey)), 
									authorizerFirstName, 
									authorizerLastName, 
									authorizerManagerEmpID, 
									null, 
									Main.toolName, 
									null, 
									Main.toolName, 
									idEntitlementPointer);

    				 			thisAuditAuthorizers.add(myNewAuthorizer);	
    				 	}
                             
    				 */
    				
    				
    				/*
    				 *  This is just the default implemented here for demonstration purposes
    				 */
    				
     				if ( ! newAuditTypeReference.getAuditByManager() ) {
    					for ( String myAuthEmpID : authorizersArray ) {
    						Authorizers myNewAuthorizer = new Authorizers(
    								-1, 
    								"Prime", 
    								Parser.returnNumericValue((myAuthEmpID)), 
    								"Need to look up the first name", 			//
    								"Need to look up the last name", 			// See comments above on how to handle these
    								-1, 										//
    								null, 
    								Main.toolName, 
    								null, 
    								Main.toolName, 
    								idEntitlementPointer);
            				
        				}
     				}

    				// We need to define the Entitlement record for the first time		
    				Entitlement entitlementAuditRecord = new Entitlement(
    						idEntitlementPointer, 
    						1, 
    						entitlement, 
    						null, 
    						Main.toolName, 
    						null, 
    						Main.toolName, 
    						newAuditTypeReferenceID);
    				
    				// This current row record is the first entity for this audit
    				ArrayList<Entities> newListOfEntities = new ArrayList<Entities>(); 
    				myRowRecordTranslatedIntoAnEntity.setEntitlement_idEntitlement(idEntitlementPointer);
    				newListOfEntities.add(myRowRecordTranslatedIntoAnEntity);
    				
    				// Create new AuditDataLoaderObject
    				AuditDataLoaderObject newAuditDataLoaderObject = new AuditDataLoaderObject(
    						idEntitlementPointer, 
    						newAuditTypeReferenceID, 
    						entitlementAuditRecord,
    						thisAuditAuthorizers, 
    						newListOfEntities);
    				
    				// And put it into the HashMap 
    				auditDataLoaderObjectMap.put(idEntitlementPointer, newAuditDataLoaderObject);		                	
                          	
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
				
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		int checkCounter = 0;
		
		//This holds the new AuditTypeReference recors, which we construct as we iterate through the field from the template
		AuditTypeReference myNewAuditReferenceRecord = new AuditTypeReference();
		
        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();
                
                // Ensure that the cell is considered as a STRING. It will be converted to numeric/boolean as required
                cell.setCellType(Cell.CELL_TYPE_STRING);
                
                //System.out.println(cell.getStringCellValue());
                
                switch(cell.getStringCellValue()) {
				case "AuditName":
					myNewAuditReferenceRecord.setAuditName(cellIterator.next().toString());
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> AuditName = " + myNewAuditReferenceRecord.getAuditName());
					break;
				case "AuditDescription":
					myNewAuditReferenceRecord.setAuditDescription(cellIterator.next().toString());
					if (Main.verbose) System.out.println(" ---> AuditDescription = "+ myNewAuditReferenceRecord.getAuditDescription());
               		checkCounter++;
               		break;
				case "AuditInstructionsEN":
					myNewAuditReferenceRecord.setAuditInstructionsEN(cellIterator.next().toString());
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> AuditInstructionsEN = "+ myNewAuditReferenceRecord.getAuditInstructionsEN());
					break;	
				case "AuditInstructionsFR":
					myNewAuditReferenceRecord.setAuditInstructionsFR(cellIterator.next().toString());
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> AuditInstructionsFR = "+ myNewAuditReferenceRecord.getAuditInstructionsFR());
					break;	
				case "UDEAprimaryFieldsEN":
					myNewAuditReferenceRecord.setUDEAprimaryFieldsEN(cellIterator.next().toString());
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> UDEAprimaryFieldsEN = "+ myNewAuditReferenceRecord.getUDEAprimaryFieldsEN());
					break;	
				case "UDEAprimaryFieldsFR":
					myNewAuditReferenceRecord.setUDEAprimaryFieldsFR(cellIterator.next().toString());
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> UDEAprimaryFieldsFR = "+ myNewAuditReferenceRecord.getUDEAprimaryFieldsFR());
					break;	
				case "UDEAsecondaryFieldsEN":
					myNewAuditReferenceRecord.setUDEAsecondaryFieldsEN(cellIterator.next().toString());
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> UDEAsecondaryFieldsEN = "+ myNewAuditReferenceRecord.getUDEAsecondaryFieldsEN());
					break;	
				case "UDEAsecondaryFieldsFR":
					myNewAuditReferenceRecord.setUDEAsecondaryFieldsFR(cellIterator.next().toString());
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> UDEAsecondaryFieldsFR = "+ myNewAuditReferenceRecord.getUDEAsecondaryFieldsFR());
					break;	
				case "AuditStart":
					myNewAuditReferenceRecord.setAuditStart( castJavaUtilDateToJavaSQLdate((Date) cellIterator.next().getDateCellValue() ));
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> AuditStart = "+ myNewAuditReferenceRecord.getAuditStart());
					break;	
				case "AuditEnd":
					myNewAuditReferenceRecord.setAuditEnd( castJavaUtilDateToJavaSQLdate((Date) cellIterator.next().getDateCellValue() ));
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> AuditEnd = "+ myNewAuditReferenceRecord.getAuditEnd());
					break;	
				case "AuditByManager":
					if ( cellIterator.next().getNumericCellValue() == 1 ) {
						myNewAuditReferenceRecord.setAuditByManager(true);
               		} else {
               			myNewAuditReferenceRecord.setAuditByManager(false);
               		}
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> AuditByManager = "+ myNewAuditReferenceRecord.getAuditByManager());
					break;
				case "DataLoadType":
					myNewAuditReferenceRecord.setDataLoadType(cellIterator.next().toString());		
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> DataLoadType = "+ myNewAuditReferenceRecord.getDataLoadType());
					break;	
				case "eMACCyclesTimelineID":
					myNewAuditReferenceRecord.seteMACyclesTimelineID( (int) cellIterator.next().getNumericCellValue());
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> eMACCyclesTimelineID = "+ myNewAuditReferenceRecord.geteMACyclesTimelineID());
					break;	
				case "EntityTransferRule":
					// We have strict possibilities for this
					String myEntityTransferRule = cellIterator.next().toString();
					if ( myEntityTransferRule.equals("no transfer allowed") || 
							myEntityTransferRule.equals("transfer to peer authorizer") || 
							myEntityTransferRule.equals("transfer to TELUS manager") ) {
						myNewAuditReferenceRecord.setEntityTransferRule(myEntityTransferRule);	
	               		checkCounter++;
	               		if (Main.verbose) System.out.println(" ---> EntityTransferRule = "+ myNewAuditReferenceRecord.getEntityTransferRule());
					}
					
					break;
				case "AuditManager":
					// AuditManager is also the audit creator
					String auditCreator = cellIterator.next().toString();
					myNewAuditReferenceRecord.setCreatedBy(auditCreator);
					myNewAuditReferenceRecord.setCreatedDateTime(null);
					myNewAuditReferenceRecord.setLastUpdatedBy(auditCreator);
					myNewAuditReferenceRecord.setLastUpdatedDateTime(null);
               		checkCounter++;
               		if (Main.verbose) System.out.println(" ---> AuditManager = "+ myNewAuditReferenceRecord.getCreatedBy());
					break;	
				default:
					//System.out.println("Do nothing");
					break;
                }

            }
        }
        
        // Did we get all the expected values from the template?
        if ( checkCounter != 15) {
        	System.out.println("The number of expected values from the template [15] does not match what was found ["+checkCounter+"]. Please ensure you have the latest eAUDIT Excel data load template. Data load FAILED");
        	Main.cleanUp();
        	System.exit(0);
        } else {
        	   				
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
	 * A method that takes a special Escale template and loads up all the data into the eAUDIT database
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
		   		
		   		System.out.println("Processed sheet and created new auditTypeReference record with id = "+newAuditTypeReferenceID);
				
				// We can proceed with the data load since the auditTypeReference object was successfully created. Get the second sheet to find data details
		   		System.out.println("Processing sheet "+workbook.getSheetName(1));
		   		XSSFSheet sheetAuditData = workbook.getSheetAt(1);
		   		
		   		// We turn that sheet into a HashMap for which we have an easy method to load into the db
		   		HashMap<Integer, AuditDataLoaderObject> sheetDataHashMap = translateAuditDataRecordsFromExcelSheetToHashMap(sheetAuditData, newAuditTypeReferenceID);
		   		System.out.println("Sheet processed. There are " + sheetDataHashMap.size() + " separate entitlement audits to load for this AuditTypeReference record # " + newAuditTypeReferenceID );
		   		
		   		// Finally, we can leverage the jEAUDIT library to mass-load these data into the database
		   		AuditDataLoaderObjectUtil.bulkCreate(sheetDataHashMap, true, Main.mainDB);
		   		
		   		
		   			
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
package com.telus.sddi.eAUDIT_BatchOps;


import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ResourceBundle;



import com.telus.sddi.UnifiedToolBoxV2.ApplicationLogging;
import com.telus.sddi.UnifiedToolBoxV2.FileOps;
import com.telus.sddi.UnifiedToolBoxV2.Parser;


/**
 * The main class that runs the various functions of eAUDIT_BatchOps. See the various switches below.
 * @author Francois Begin
 *
 */
public class Main {
	
	/****************************************************************************************
	 * EDIT THE FOLLOWING TO ADJUST BASED ON YOUR ENVIRONMENT
	 ****************************************************************************************/
	
	public static Boolean isProduction		= false;
	
	/****************************************************************************************/
	
	
	public enum routines {
		loadFromExcelTemplate, loadFromAdapter, help, verbose, version
	}
	
	public static String  version = "2015-04-27.2";
	public static String versionHistory() {
		return
				"\n   2015-04-27.2 Initial release for SANS paper";
	}
	
	public static String help() {
		return 
				"   -loadFromAdapter\t\tLoad data into the eAUDIT adapter using an adapter \n" +
				"	-loadFromExcelTemplate\t\tLoad data into the eAUDIT database using an Excel template \n" +
				"	-version\t\t\t\tShows version.\n"+
				"	-verbose \t\t\t\tRun script in verbose mode.\n" +
				"	-help \t\t\t\t\tList switches available.\n";
	}
	
	/*
	 * Some key variables that we define for convenience and can be called from other classes
	 */
	public static String applicationStaticParams;
	public static ApplicationLogging logger;
	public static ResourceBundle myParams;
	
	public static Boolean overrideDBConnectionToPool = false;
	
	public static String mainDB;
	
	public static String errorLogFile;
	public static String toolName;
	public static String lockFile;
	
	public static Boolean verbose;
	
	
	public static void main(String[] args) throws SQLException, MalformedURLException {
		
		Boolean loadFromExcelTemplate						= false;
		Boolean loadFromAdapter								= false;
		
		/* 
		 * Initialize eAUDIT itself
		 */
	   	
	    // Define the main bundle where the static parameters can be found 
    	if ( isProduction) {
    		applicationStaticParams = "com.telus.sddi.eAUDIT_BatchOps.StaticParamsPR";
    	} else {
    		applicationStaticParams = "com.telus.sddi.eAUDIT_BatchOps.StaticParamsDV";	
    	}
    	
    	
    	// Our logger
    	logger 	= new ApplicationLogging(applicationStaticParams);
    	
    	// Load up the resource bundle. We will also be able to call this from other classes
		myParams = ResourceBundle.getBundle(applicationStaticParams);

		// Get other parameters that the application will be using
		verbose 				= Parser.returnTrueFalse(myParams.getString("verbose"));
		toolName				= myParams.getString("toolName");
    	lockFile 				= logger.getToolBasePath() + myParams.getString("TmpFilesPath") + "/lock";
				
		Boolean checkSwitches	= Parser.returnTrueFalse(myParams.getString("checkSwitches"));
		
		// Our main data source throughout all operations in this program
		mainDB					= myParams.getString("mainDB");
						
		// Were we passed any arguments? If so, they may override what is listed above 
		if ( args.length == 0 && checkSwitches) {
			System.out.println("No switch passed to program. Aborting. Run "+Main.toolName+" -help to see the available switches");
			System.exit(0);
		}
		
		String currentArg = "";
		
		try{
			
			for ( String arg : args ) {
				currentArg = arg;
				arg = arg.replace("-", "");
				switch(routines.valueOf(arg)) {
					case loadFromExcelTemplate:
						loadFromExcelTemplate						= true;
						break;
					case loadFromAdapter:
						loadFromAdapter								= true;
						break;
					case verbose:
						verbose										= true;
						break;
					case version:
						System.out.println("Version: "+version);
						System.out.println("History: "+versionHistory());
						System.exit(0);
						break;
					case help:
						System.out.println("You can pass the following arguments to " + toolName + ":\n" + help());
						System.exit(0);
						break;
					default:
						System.out.println("No switch passed to program. Aborting. Run " + toolName + " -help to see the available switches");
						System.exit(0);
						break;
				}
			}
			
		} catch(IllegalArgumentException e) {
			System.out.println("Switch '"+currentArg+"' is invalid. Here are the valid switches:\n");
			System.out.println(help());
			System.exit(0);
		}
		
		
		// Script starts here
		if ( isProduction ) {
			logger.write(null, Main.toolName + " " + version + " starting. Pointing to PROD ", "info");
		} else {
			logger.write(null, Main.toolName + " " + version + " starting. Pointing to DEV ", "info");
		}
		
		
		/*
		 * SELF-CHECKING ROUTINES 
		 */
		ErrorChecking.checkErrorFile();
		ErrorChecking.checkLockFile();

		
		/**
		 * Before you can run this, ensure that you have created the following directories
		 * 
		 * 		/export/data/eaudit
		 * 		/export/data/eaudit/tmp
		 * 		/export/data/eaudit/logs
		 * 		/export/data/eaudit/dataLoad
		 * 
		 * You also need DataLoadExample.xlsx inside /export/data/eaudit/dataLoad
		 * 
		 *  If you want to use another base directory path, you need to edit properties files StaticParamsDV/StaticParamsPR
		 */
		if ( loadFromExcelTemplate ) {
			Ops_ExcelDataLoader.loadFromExcelTemplate(logger.getToolBasePath()+"/dataLoad/DataLoadExample.xlsx");
		}
		
		if ( loadFromAdapter ) {
			Ops_AdapterDataLoad.loadFromAdapter();
		}
		
		cleanUp();
		
		logger.write(null, Main.toolName + " " + version + " done.", "info");
				
	}
	
	/**
	 * We clean up once we are done (remove lock file
	 */
	protected static void cleanUp() {
		
		if ( FileOps.removeLockFile(lockFile) ) {
			logger.write("-", "Removing lock file " + lockFile, "info");
		} else {
			logger.write("-", "An unexpected eror occured while attempting to remove lock file " +  lockFile, "error");
		}
	}
		
		
}
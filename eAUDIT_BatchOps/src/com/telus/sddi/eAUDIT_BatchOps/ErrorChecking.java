package com.telus.sddi.eAUDIT_BatchOps;

import java.io.IOException;

import com.telus.sddi.UnifiedToolBoxV2.DateUtils;
import com.telus.sddi.UnifiedToolBoxV2.FileOps;


/**
 * This class handles some basic error checking when the Main class is run. Things such as
 * 	Did we run into an unexpected error when we last ran
 *  Is the program already running (lock file)
 * @author Francois Begin
 *
 */
public class ErrorChecking {

	
	/**
	 * Just make sure that another instance of the batch program is not already running.
	 */
	public static void checkLockFile() {
		
		Main.logger.write("-", "Checking lock file " + Main.lockFile , "info");
		
		// We can lock up for 60 minutes but after that, we send a notification to the admins
		
		int returnCode = FileOps.createLockFile(Main.lockFile, 60);
		
		// Has been locked for more than 60 minutes
		if ( returnCode == 2 ) {
			
			String error = Main.toolName + " has been locked up for more than 60 minutes according to " + Main.lockFile +". Aborting program and emailing administrators (" + Main.myParams.getString("adminEmail") + "). The lock file can be removed if the script is not running and was not deleted for any reason.";
			Main.logger.write("-", error, "critical");
			
			//
			// You can choose to send an email alert here
			
			// Abort program
			System.exit(0);
			
		// Has not been locked for more than 60 minutes. Probably still running
		} else if ( returnCode == 1 ) {
			
			Main.logger.write("-", "Another instance of "+ Main.toolName + " is currently running. See lock file " + Main.lockFile + ". Aborting program", "warning");
			// Abort program
			System.exit(0);
		}
		
	}
	
	/**
	 * Check the error log file. This file contains unexpected errors. If it contains something, alert the administrator but move the file aside (timestamp)
	 * so that eLPM_BatchOps can run the next time 
	 */
	public static void checkErrorFile() {
		
		FileOps.createFileIfNotExist(Main.logger.getErrorLogFullPath());
		
		try {
			if ( FileOps.countLines(Main.logger.getErrorLogFullPath()) > 0 ) {
				Main.logger.write("-", "Error(s) found in log file " + Main.logger.getErrorLogFullPath() +". Aborting program and emailing administrator (" + Main.myParams.getString("adminEmail") + ")", "critical");
				
				String error = FileOps.readFileIntoString(Main.logger.getErrorLogFullPath());
				if ( error.length() > 2500 ) {
					error = error.substring(error.length() - 2500);
				}

				//
				// You can choose to send an email alert here
				
				// We save the error log file then clear it so that eLPM_BatchOps can run next time
				FileOps.copy(Main.logger.getErrorLogFullPath(), Main.logger.getErrorLogFullPath() + "."+DateUtils.getTimeStamp());
				FileOps.clearFileContents(Main.logger.getErrorLogFullPath());
				removeLockFile();			
				System.exit(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void removeLockFile() {
		
		if ( FileOps.removeLockFile( Main.lockFile) ) {
			Main.logger.write("-", "Removing lock file " +  Main.lockFile, "info");
		} else {
			Main.logger.write("-", "An unexpected eror occured while attempting to remove lock file " +  Main.lockFile, "error");
		}
	}
	
}

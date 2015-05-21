package com.telus.sddi.jEAUDITlibrary;

import java.util.ArrayList;

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
public class AuditDataLoaderObject {

	private int idAuditID;
	private int idAuditTypeReference;
	private Audit audit;
	private ArrayList<Authorizers> auditAuthorizers;
	private ArrayList<Entities> auditEntities;

	
	
	
	/**
	 * The class main constructor
	 * @param idAuditID
	 * @param idAuditTypeReference
	 * @param auditAuthorizers
	 * @param auditEntities
	 */
	public AuditDataLoaderObject(
			int idAuditID, 
			int idAuditTypeReference,
			Audit audit,
			ArrayList<Authorizers> auditAuthorizers,
			ArrayList<Entities> auditEntities) {
		super();
		this.idAuditID 				= idAuditID;
		this.idAuditTypeReference 	= idAuditTypeReference;
		this.setAudit(audit);
		this.auditAuthorizers 		= auditAuthorizers;
		this.auditEntities 			= auditEntities;
	}
	
	public int getIdAuditID() {
		return idAuditID;
	}
	public void setIdAuditID(int idAuditID) {
		this.idAuditID = idAuditID;
	}
	public ArrayList<Authorizers> getAuditAuthorizers() {
		return auditAuthorizers;
	}
	public void setAuditAuthorizers(ArrayList<Authorizers> auditAuthorizers) {
		this.auditAuthorizers = auditAuthorizers;
	}
	public ArrayList<Entities> getAuditEntities() {
		return auditEntities;
	}
	public void setAuditEntities(ArrayList<Entities> auditEntities) {
		this.auditEntities = auditEntities;
	}

	public int getIdAuditTypeReference() {
		return idAuditTypeReference;
	}

	public void setIdAuditTypeReference(int idAuditTypeReference) {
		this.idAuditTypeReference = idAuditTypeReference;
	}

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}
	
}

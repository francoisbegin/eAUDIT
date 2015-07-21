package com.telus.sddi.jEAUDITlibrary;

import java.util.ArrayList;

import com.telus.sddi.jEAUDITlibrary.Entitlement;
import com.telus.sddi.jEAUDITlibrary.Authorizers;
import com.telus.sddi.jEAUDITlibrary.Entities;


/**
 * This class is a meta-object that is used to organize entitlement data prior to a data load. Using this class, you can funnel all of the
 * entitlement data into a generic class from where the data load can be done quickly and efficiently.
 * 
 * The Entitlement table is one of the key tables in the data model of eAUDIT since it is the table that links 
 * 		Authorizers
 * 		AuditTypeReference 
 * 		Entities. 
 * 
 * One key field in class AuditDataLoaderObject is therefore idEntitlementID
 * 
 * @author T805959
 *
 */
public class AuditDataLoaderObject {

	private int idEntitlementID;
	private int idAuditTypeReference;
	private Entitlement entitlement;
	private ArrayList<Authorizers> auditAuthorizers;
	private ArrayList<Entities> auditEntities;	
	
	
	/**
	 * The class main constructor
	 * @param idEntitlementID
	 * @param idAuditTypeReference
	 * @param auditAuthorizers
	 * @param auditEntities
	 */
	public AuditDataLoaderObject(
			int idEntitlementID, 
			int idAuditTypeReference,
			Entitlement entitlement,
			ArrayList<Authorizers> auditAuthorizers,
			ArrayList<Entities> auditEntities) {
		super();
		this.idEntitlementID 				= idEntitlementID;
		this.idAuditTypeReference 			= idAuditTypeReference;
		this.setEntitlement(entitlement);
		this.auditAuthorizers 				= auditAuthorizers;
		this.auditEntities 					= auditEntities;
	}
	
	public int getIdEntitlementID() {
		return idEntitlementID;
	}
	public void setIdEntitlementID(int idEntitlementID) {
		this.idEntitlementID = idEntitlementID;
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

	public Entitlement getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(Entitlement entitlement) {
		this.entitlement = entitlement;
	}
	
}
package com.telus.sddi.jEAUDITlibrary;


/**
 * This class is a meta-object that is used to read and organize audit data back from the database. This class is used when making adjustments to manager-driven audits 
 * (which explains why authorizersObject is a single object and not an ArrayList
 * 
 * @author T805959
 *
 */
public class AuditDataReaderObject {
	
	public AuditDataReaderObject(Entities myEntityObject, Entitlement myEntitlement, Authorizers myAuthorizersObject ) {
		setEntityObject(myEntityObject);
		setEntitlement(myEntitlement);
		setAuthorizers(myAuthorizersObject);	
	}

	private Entities entity;
	private Entitlement entitlement;
	private Authorizers authorizers;
	
	public Entities getEntity() {
		return entity;
	}
	public void setEntityObject(Entities entity) {
		this.entity = entity;
	}
	public Entitlement getEntitlement() {
		return entitlement;
	}
	public void setEntitlement(Entitlement entitlement) {
		this.entitlement = entitlement;
	}
	public Authorizers getAuthorizers() {
		return authorizers;
	}
	public void setAuthorizers(Authorizers authorizers) {
		this.authorizers = authorizers;
	}
}
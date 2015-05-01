package com.telus.sddi.eAUDIT_BatchOps;

public class DemoAdapterData {
	
	public DemoAdapterData(String cardNumber, String cardLabel,
			String cardActivation, String cardType, String cardDeactivation, String entitlement) {
		super();
		this.cardNumber = cardNumber;
		this.cardLabel = cardLabel;
		this.cardActivation = cardActivation;
		this.cardType = cardType;
		this.cardDeactivation = cardDeactivation;
		this.entitlement = entitlement;
	}
	
	private String cardNumber;
	private String cardLabel;
	private String cardActivation;
	private String cardType;
	private String cardDeactivation;
	private String entitlement;
	
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardLabel() {
		return cardLabel;
	}
	public void setCardLabel(String cardLabel) {
		this.cardLabel = cardLabel;
	}
	public String getCardActivation() {
		return cardActivation;
	}
	public void setCardActivation(String cardActivation) {
		this.cardActivation = cardActivation;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardDeactivation() {
		return cardDeactivation;
	}
	public void setCardDeactivation(String cardDeactivation) {
		this.cardDeactivation = cardDeactivation;
	}
	public String getEntitlement() {
		return entitlement;
	}
	public void setEntitlement(String entitlement) {
		this.entitlement = entitlement;
	}
	
	

}

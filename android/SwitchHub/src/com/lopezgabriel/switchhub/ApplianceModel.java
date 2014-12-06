package com.lopezgabriel.switchhub;

public class ApplianceModel {
	
	private String objectId;
	private String applianceName;
	private boolean power;
	private boolean synced;
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getApplianceName() {
		return applianceName;
	}
	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}
	public boolean isPower() {
		return power;
	}
	public void setPower(boolean power) {
		this.power = power;
	}
	public boolean isSynced() {
		return synced;
	}
	public void setSynced(boolean synced) {
		this.synced = synced;
	}
	
}


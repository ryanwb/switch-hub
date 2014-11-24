package com.lopezgabriel.switchhub;

public class ApplianceModel {
	
	private String objectId;
	private int applianceId;
	private String applianceName;
	private boolean power;
	private boolean synced;
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public int getApplianceId() {
		return applianceId;
	}
	public void setApplianceId(int applianceId) {
		this.applianceId = applianceId;
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


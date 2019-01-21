package com.zenwerx.findierock.model;

import java.util.Date;

import android.location.Location;

public class Status {
	
	public Status()
	{	
	}
	
	public Status(Location location, Date date)
	{
		setLastLongitude(location.getLongitude());
		setLastLatitude(location.getLatitude());
		setLastUpdate(date);
	}
	
	private int statusId = 0;
	private Date lastUpdate = new Date(0);
	private double lastLatitude = 0.0f;
	private double lastLongitude = 0.0f;
	
	public void setLastLatitude(double lastLatitude) {
		this.lastLatitude = lastLatitude;
	}
	
	public double getLastLatitude() {
		return lastLatitude;
	}
	
	public void setLastLongitude(double lastLongitude) {
		this.lastLongitude = lastLongitude;
	}
	
	public double getLastLongitude() {
		return lastLongitude;
	}
	
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	public int getStatusId() {
		return statusId;
	}
}

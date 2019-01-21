package com.zenwerx.findierock.model;

import java.util.Date;

import com.zenwerx.findierock.helper.ConversionHelper;

import android.location.Location;

public class Event {
	private int eventId = 0;
	
	private double latitude = 0.0f;
	private double longitude = 0.0f;
	
	private String name = new String("");
	
	private String description = new String("");
	
	private Date startTime = new Date();
	
	private Artist[] artists = null;
	
	private int venueId = 0;
	private Venue venue = null;
	
	public int getVenueId() {
		return venueId;
	}

	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	public Artist[] getArtists() {
		return artists;
	}

	public void setArtists(Artist[] artists) {
		this.artists = artists;
	}

	public boolean ageOfMajority = false;
	
	public int getEventId()
	{
		return eventId;
	}
	
	public void setEventId(int eventId)
	{
		this.eventId = eventId;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public Date getStartTime()
	{
		return startTime;
	}
	
	public void setStartTime(Date start)
	{
		startTime = start;
	}
	
	public boolean getAgeOfMajority()
	{
		return ageOfMajority;
	}
	
	public void setAgeOfMajority(boolean ageOfMajority)
	{
		this.ageOfMajority = ageOfMajority;
	}
	
	public double getDistanceFrom(Location location)
	{
		// Lie if we don't get a valid location
		double lat = 0.0f;
		double lng = 0.0f;
		
		if (location != null)
		{
			lat = location.getLatitude();
			lng = location.getLongitude();
		}
		
		return
			ConversionHelper.GeoLocation.GetAbsoluteDistance(getLatitude(), getLongitude(), lat, lng);
	}	
}

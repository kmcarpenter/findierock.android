package com.zenwerx.findierock.model;

import com.zenwerx.findierock.data.FindieDbHelper;

public class Venue {
	private int venueId = 0;
	private String name = "";
	private String address = "";
	private String address2 = "";
	private String city = "";
	private String province = "";
	private String country = "";
	private String website = "";
	private double latitude = 0.0f;
	private double longitude = 0.0f;
	private String smallImage = "";
	private String largeImage = "";
	
	private Event[] events = null;
	
	public Event[] getEvents() {
		return events;
	}
	public void setEvents(Event[] events) {
		this.events = events;
	}
	
	public int getVenueId() {
		return venueId;
	}
	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setFavourite(boolean favourite) {
		if (favourite)
		{
			FindieDbHelper.getInstance().getFavouriteHelper().setFavourite(this);
		}
		else
		{
			FindieDbHelper.getInstance().getFavouriteHelper().deleteFavourite(this);
		}
	}
	public boolean isFavourite() {
		return FindieDbHelper.getInstance().getFavouriteHelper().IsFavourite(this);
	}
	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}
	public String getSmallImage() {
		return smallImage;
	}
	public void setLargeImage(String largeImage) {
		this.largeImage = largeImage;
	}
	public String getLargeImage() {
		return largeImage;
	}
}

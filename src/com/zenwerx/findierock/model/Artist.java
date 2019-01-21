package com.zenwerx.findierock.model;

import com.zenwerx.findierock.data.FindieDbHelper;

public class Artist {
	private int artistId = 0;
	private String name = "";
	private String bio = "";
	private String website = "";
	private Event[] upcomingEvents = null;
	private String smallImage = "";
	private String largeImage = "";
	private boolean fetchedAlbums = false;
	private Album[] albums = null;
	
	public int getArtistId() {
		return artistId;
	}
	
	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBio() {
		return bio;
	}
	
	public void setBio(String bio) {
		this.bio = bio;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}

	public void setUpcomingEvents(Event[] upcomingEvents) {
		this.upcomingEvents = upcomingEvents;
	}

	public Event[] getUpcomingEvents() {
		return upcomingEvents;
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

	public void setSmallImage(String smallimage) {
		this.smallImage = smallimage;
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

	public void setFetchedAlbums(boolean fetchedAlbums) {
		this.fetchedAlbums = fetchedAlbums;
	}

	public boolean didFetchAlbums() {
		return fetchedAlbums;
	}

	public void setAlbums(Album[] albums) {
		this.albums = albums;
	}

	public Album[] getAlbums() {
		return albums;
	}
}

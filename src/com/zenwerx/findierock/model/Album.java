package com.zenwerx.findierock.model;

import java.util.Date;
import com.zenwerx.findierock.data.FindieDbHelper;

public class Album {
	private int albumId;
	private String name;
	private Date releaseDate;
	private String moreUrl;
	private String smallImage;
	private String largeImage;
	private String lastFmId;
	private int artistId;
	private AlbumTrack[] tracks;
	
	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}
	
	public int getAlbumId() {
		return albumId;
	}
	
	public void setLargeImage(String largeImage) {
		this.largeImage = largeImage;
	}
	
	public String getLargeImage() {
		return largeImage;
	}
	
	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}
	
	public String getSmallImage() {
		return smallImage;
	}
	
	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl;
	}
	
	public String getMoreUrl() {
		return moreUrl;
	}
	
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public int getArtistId() {
		return artistId;
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

	public String getLastFmId() {
		return lastFmId;
	}

	public void setLastFmId(String lastFmId) {
		this.lastFmId = lastFmId;
	}
	
	public AlbumTrack[] getTracks()
	{
		return tracks;
	}
	
	public void setTracks(AlbumTrack[] tracks) {
		this.tracks = tracks;
	}
}

package com.zenwerx.findierock.model;

import com.zenwerx.findierock.data.FavouriteHelper.FavouriteType;

public class Favourite {
	private FavouriteType favouriteType = FavouriteType.FAV_NONE;
	private int favouriteId = 0;
	
	public void setFavouriteType(FavouriteType favouriteType) {
		this.favouriteType = favouriteType;
	}
	public FavouriteType getFavouriteType() {
		return favouriteType;
	}
	public void setFavouriteId(int favouriteId) {
		this.favouriteId = favouriteId;
	}
	public int getFavouriteId() {
		return favouriteId;
	}
	
}

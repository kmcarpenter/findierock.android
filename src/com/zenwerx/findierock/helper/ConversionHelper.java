package com.zenwerx.findierock.helper;

public class ConversionHelper {
	private static final double KM_TO_MI = 0.621371192;
	
	public static class Distance {
		public static double MilesToKilometres(double miles)
		{
			return miles/KM_TO_MI;
		}
		
		public static double KiloMetresToMiles(double km)
		{
			return km * KM_TO_MI; 
		}
	}
	
	public static class GeoLocation
	{
		public static double GetAbsoluteDistance(double latOrig, double longOrig, double latNew, double longNew)
		{
			double R = 6371; // km
			double dLat = toRad(latNew-latOrig);
			double dLon = toRad(longNew-longOrig);
			latOrig = toRad(latOrig);
			latNew = toRad(latNew);

			double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(latOrig) * Math.cos(latNew); 
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
			double d = R * c;
			
			return d;
			/*
			return
			(((Math.acos(Math.sin((latNew*Math.PI/180)) *
	                Math.sin((latOrig*Math.PI/180))+Math.cos((latNew*Math.PI/180)) *
	                Math.cos((longOrig*Math.PI/180)) * Math.cos(((longNew-
	                longOrig)*Math.PI/180))))*180/Math.PI)*60*1.1515*1.609344);
	                */
		}
		
		private static double toRad(double d)
		{
			return d * Math.PI / 180;
		}
	}
}

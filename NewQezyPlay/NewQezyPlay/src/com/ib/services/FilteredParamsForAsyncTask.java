package com.ib.services;
 
import java.util.ArrayList;
 

/**
 * @author Lalita 
 */
public   class FilteredParamsForAsyncTask   {

	public static ArrayList<String> CountriesSelection = new ArrayList<String>();
	public static  ArrayList<String> CategoriesSelection = new ArrayList<String>();
	public static  String UniqueDeviceId = null;
	public static String ChannelId = null;


	public FilteredParamsForAsyncTask(ArrayList<String> CountriesSelection,ArrayList<String> CategoriesSelection) {
		this.CountriesSelection = CountriesSelection;
		this.CategoriesSelection = CategoriesSelection;
	}
	
	public FilteredParamsForAsyncTask(String UniqueDeviceId,String ChannelId) {
		  this.UniqueDeviceId = UniqueDeviceId; 
		  this.ChannelId = ChannelId;
	}
	
	
	public FilteredParamsForAsyncTask(String UniqueDeviceId ) {
		  this.UniqueDeviceId = UniqueDeviceId; 
 	}
	
	
	
}


package com.ib.listeners;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import com.ib.displayadapters.Adapterlist;
import com.ib.displayadapters.CategoryAdapter;
import com.ib.displayadapters.FilteredCategoryAdapter;
import com.ib.qezyplay.R;
import com.ib.qezyplay.UserMainScreen;
import com.ib.services.FilteredParamsForAsyncTask;
import com.ib.services.FilteredSelection;
import com.ib.services.MainScreen;
import android.content.Context; 
import android.util.Log; 
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 
 * @author Ideabytes
 * @version 1.0.0
 *CountryCategoryListeners  - This implements the OnitemClickListener for  Countries and Category Section
 *Filtering Categories based on the selected Country or Countries  and Vice-Versa
 *
 */
public class CountryCategoryListeners  {

	private static final String TAG = null;
	public   Context context = null ; 
	public static ArrayList<String> CheckedCountries = new ArrayList<String>();  
	public static ArrayList<String> CheckedCategories =  new ArrayList<String>();
	public static int SelectedCategoryCount = 0;
	public static int SelectedCountryCount = 0;
	ImageView CategoriesImage, CountriesImage;
	FilteredCategoryAdapter filteredCategoryAdapter = new FilteredCategoryAdapter(context);
	CategoryAdapter categoryAdapter = new CategoryAdapter(context);

/**
 * Contructor for the passing the Reference of the  UserMainScreen(Main Activity)
 * @param a
 */
	public CountryCategoryListeners(Context a ) 
	{
		context = a;   
	}

	/**
	 *  Category Listeners
	 */
 	public OnItemClickListener categoryListItemListener = new  OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SelectionCategories(view,position); 
		}
	};  


	/**
	 * Country Listeners
	 */
	 
 	public OnItemClickListener countryListItemListener = new  OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SelectionCountries(view,position);

		}
	};

/**
 * SelectionCategories- The Method  Highlights the background for the selected Categories and 
 * Displays the Filtered Countries based on the selected Categories
 * @param view
 * @param position
 */
	private void SelectionCategories(View view, int position) {
 
		CategoriesImage = (ImageView)view.findViewById(R.id.categories_image); 
		if(! CategoriesImage.isSelected())
		{   
			Log.e(TAG,"   Entered  NOT SELECTED TO GET SELECTED" );    
			if(SelectedCountryCount < 3 )
			{ 
				SelectedCategoryCount++;
				CategoriesImage.setSelected(true);
				CategoriesImage.setBackgroundResource(R.drawable.list_selector);   

				if(SelectedCountryCount == 0)
				{
					CheckedCategories.add(MainScreen.CategoryId.get(position)); 
					DisplayFilteredChannels(CheckedCountries,CheckedCategories);
					UserMainScreen.CategoryListView.setAdapter(filteredCategoryAdapter); 
					UserMainScreen.ChannelListView.setAdapter(Adapterlist.FilteredChannelsAdapter); 
				}

				else if(SelectedCountryCount > 0)
				{
					CheckedCategories.add( FilteredSelection.CategoryId.get(position));      
					DisplayFilteredChannels(CheckedCountries,CheckedCategories);     
					UserMainScreen.CategoryListView.setAdapter(filteredCategoryAdapter); 
					UserMainScreen.ChannelListView.setAdapter(Adapterlist.FilteredChannelsAdapter); 
				}
			}
			else
			{
				Toast.makeText(context, "You cannot exceed 3 Selections", Toast.LENGTH_LONG).show();
			}
		}
		else if(CategoriesImage.isSelected())
		{  
			SelectedCategoryCount--;
			CategoriesImage.setSelected(false); 
			CategoriesImage.setBackgroundResource(R.drawable.list_selector);

			if(SelectedCountryCount == 0)
			{
				CheckedCategories.remove(FilteredSelection.CategoryId.get(position)); 
				DisplayFilteredChannels(CheckedCountries ,CheckedCategories);
				UserMainScreen.CategoryListView.setAdapter(categoryAdapter);
				UserMainScreen.ChannelListView.setAdapter(Adapterlist.FilteredChannelsAdapter);
			}

			if(SelectedCountryCount > 0)
			{
				CheckedCategories.remove(FilteredSelection.CategoryId.get(position)); 
				DisplayFilteredChannels(CheckedCountries ,CheckedCategories);
				UserMainScreen.CategoryListView.setAdapter(filteredCategoryAdapter);
				UserMainScreen.ChannelListView.setAdapter(Adapterlist.FilteredChannelsAdapter);

			}       
		} 
	}     
/**
 * SelectionCountries - The Method  Highlights the background for the selected Countries and 
 * Displays the Filtered Categories based on the selected Countries
 * @param view
 * @param position
 */

	private void SelectionCountries(View view, int position) {

		CountriesImage = (ImageView)view.findViewById(R.id.countries_images); 

		if(!CountriesImage.isSelected())
		{   
			Log.e(TAG,"   Entered  NOT SELECTED TO GET SELECTED" );    
			if(SelectedCountryCount < 3 )
			{ 
				CountriesImage.setSelected(true);
				CountriesImage.setBackgroundResource(R.drawable.list_selector);
				SelectedCountryCount++;
				CheckedCountries.add(MainScreen.CountryId.get(position));        
				DisplayFilteredChannels(CheckedCountries,CheckedCategories);
				UserMainScreen.CategoryListView.setAdapter(filteredCategoryAdapter);       
				UserMainScreen.ChannelListView.setAdapter(Adapterlist.FilteredChannelsAdapter);    
			}
			else
			{ 
				Toast.makeText (context, "You cannot exceed 3 Selections", Toast.LENGTH_LONG).show();
			}
		}
		else if(CountriesImage.isSelected())
		{  
			Log.e(TAG,"   Entered  SELECTED TO GET DESELECTED" );  
			CountriesImage.setSelected(false); 
			CountriesImage.setBackgroundResource(R.drawable.list_selector);
			SelectedCountryCount--;
			CheckedCountries.remove(MainScreen.CountryId.get(position));
			DisplayFilteredChannels(CheckedCountries ,CheckedCategories);
			UserMainScreen.CategoryListView.setAdapter(filteredCategoryAdapter);
			UserMainScreen.ChannelListView.setAdapter(Adapterlist.FilteredChannelsAdapter);
		}

	}

 /**
  * 
  * DisplayFilteredChannels - Display Filtered Channels based on the selected Countries and Categories
  * @param SelectionFilterForCountries
  * @param SelectionFilterForCategories
  */
	private void DisplayFilteredChannels(ArrayList<String> SelectionFilterForCountries , ArrayList<String> SelectionFilterForCategories) {


		FilteredParamsForAsyncTask filteredParamsForAsyncTask =  new  FilteredParamsForAsyncTask(SelectionFilterForCountries, SelectionFilterForCategories);
        FilteredSelection filteredSelection = new FilteredSelection(context);
		try {
			filteredSelection.execute(filteredParamsForAsyncTask).get(); 

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i = 0 ; i < FilteredSelection.CategorySelectedId.size() ; i++){

			Log.e(TAG," test in UserMainScreen"+FilteredSelection.CategorySelectedId.get(i));
		}

	}

}
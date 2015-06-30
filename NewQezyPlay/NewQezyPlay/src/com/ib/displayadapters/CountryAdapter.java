package com.ib.displayadapters;


import com.ib.qezyplay.R;
import com.ib.services.MainScreen;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *CountryAdapter - Adapter used for the Initial Launch for Displaying the UnFiltered Countries
 *@author Ideabytes
 *@version 1.0.0
 */
@SuppressLint("InflateParams")
public class CountryAdapter  extends BaseAdapter{

	protected static final String TAG ="CountryAdapter";
	public static  Context context = null ; 



	public  CountryAdapter(Context a ) {
		context = a ;		 
	}

	@Override
	public int getCount() {
		return  MainScreen.CountryThumbnails.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	@SuppressLint("ViewHolder")
	@Override            
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list_view, null);
		ImageView  countryimageview = (ImageView)view.findViewById(R.id.countries_images);
		TextView countrytextview = (TextView)view.findViewById(R.id.countries_name);

		Picasso.with(context)
		.load(MainScreen.CountryThumbnails.get(position))
		.noFade()
		.tag(context)
		.into(countryimageview);
		countrytextview.setText(MainScreen.CountryName.get(position));

		return view;
	}


}






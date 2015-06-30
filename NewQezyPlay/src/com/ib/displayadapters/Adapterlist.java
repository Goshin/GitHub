package com.ib.displayadapters;
 
import com.ib.qezyplay.R;
import com.ib.services.FilteredSelection;
import com.ib.services.GetFavoritesChannels;
import com.ib.services.GetRecentlyViewedChannels;
import com.ib.services.MainScreen;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapterlist - Implementation for various Adaptors for all the Horizontal List Views 
 * @author Ideabytes
 * @version 1.0.0
 *
 */
public class Adapterlist {

	protected static final String TAG = "Adapterlist.java";
	private static Context context ; 
	String LOGTAG = "Adapterlist.java";

	/**Constructor reference for the UserMainScreen.java 
	 * 
	 * @param a
	 */
	public  Adapterlist(Context a ) {
		context = a ;		 
	}
 
	/**
	 *  Adapter for the  Channel Horizontal List View  
	 */

	@SuppressLint({ "ViewHolder", "InflateParams" })
	public  static BaseAdapter ChannelAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return  MainScreen.ChannelThumbnails.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_view, null);
			ImageView channelimageview = (ImageView)retval.findViewById(R.id.channelimage);	

			Picasso.with(context)
			.load(MainScreen.ChannelThumbnails.get(position))
			.noFade()
			.tag(context)
			.into(channelimageview);			 
			return retval;
		}
	};


	/**
	 * Adapter for FilteredChannels,List displayed after giving the selected Countries and Categories
	 * 
	 */

	@SuppressLint({ "ViewHolder", "InflateParams" })
	public static  BaseAdapter FilteredChannelsAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return FilteredSelection.ChannelThumbnails.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_view, null);
			ImageView channelimageview = (ImageView)retval.findViewById(R.id.channelimage);	

			Picasso.with(context)
			.load(FilteredSelection.ChannelThumbnails.get(position))
			.noFade()
			.tag(context)
			.into(channelimageview);			 
			return retval;
		}
	};

	/**
	 *  Adapter for Categories Display
	 */

	@SuppressLint({ "ViewHolder", "InflateParams" })
	public  static BaseAdapter  CategoryAdapter = new BaseAdapter( ) {

		@Override
		public int getCount() {
			return  MainScreen.CategoryThumbnails.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_view, null);
			ImageView  categoryimageview = (ImageView)retval.findViewById(R.id.categories_image);
			TextView categorytextview = (TextView)retval.findViewById(R.id.categories_name);		

			Picasso.with(context)
			.load(MainScreen.CategoryThumbnails.get(position))
			.noFade()
			.tag(context)
			.into(categoryimageview);			 
			categorytextview.setText(MainScreen.CategoryName.get(position));
			return retval;
		}
	};


	/**
	 *  Adapter for Filtered Categories on the Country Selection  
	 */

	@SuppressLint({ "ViewHolder", "InflateParams" })
	public static   BaseAdapter  FilteredCategoryAdapter = new BaseAdapter( ) {

		@Override
		public int getCount() {
			return   FilteredSelection.CategoryThumbnails.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_view, null);
			ImageView  filteredcategoryimageview = (ImageView)retval.findViewById(R.id.categories_image);	
			TextView filteredcategorytextview = (TextView)retval.findViewById(R.id.categories_name);	

			Picasso.with(context)
			.load( FilteredSelection.CategoryThumbnails.get(position))
			.noFade()
			.tag(context)
			.into(filteredcategoryimageview);		 
			filteredcategorytextview.setText( FilteredSelection.CategoryName.get(position));

			if(!FilteredSelection.CategorySelectedId.get(position).equals("0") )
				Log.e(TAG," test 2 " +FilteredSelection.CategorySelectedId.get(position));

			filteredcategoryimageview.setBackgroundResource(R.drawable.list_selector);
			return retval;
		}
	};

	/**
	 *  Adapter  for  Favorites list .
	 */

	@SuppressLint({ "InflateParams", "ViewHolder" })
	public static  BaseAdapter  FavoritesAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return  GetFavoritesChannels.ChannelThumbnails.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override            
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list_view, null);
			ImageView   FavoriteImageView = (ImageView)retval.findViewById(R.id.favroriteimage);			

			Picasso.with(context)
			.load( GetFavoritesChannels.ChannelThumbnails.get(position))

			.noFade()
			.tag(context)
			.into(FavoriteImageView);
			//TextView title = (TextView) retval.findViewById(R.id.title);
			//title.setText(ChannelName.get(position));
			return retval;
		}
	};


	/**
	 *  Adapter For Recently Viewed Horizontal List
	 */

	@SuppressLint({ "InflateParams", "ViewHolder" })
	public static  BaseAdapter   RecentlyViewedAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return  GetRecentlyViewedChannels.ChannelThumbnails.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override            
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_viewed_list_view, null);
			ImageView   RecentlyViewedimageview = (ImageView)retval.findViewById(R.id.recentlyviewedimage);			

			Picasso.with(context)
			.load(GetRecentlyViewedChannels.ChannelThumbnails.get(position))
			.noFade()
			.tag(context)
			.into(RecentlyViewedimageview);
			//TextView title = (TextView) retval.findViewById(R.id.title);
			//title.setText(ChannelName.get(position));
			return retval;
		}
	};


}






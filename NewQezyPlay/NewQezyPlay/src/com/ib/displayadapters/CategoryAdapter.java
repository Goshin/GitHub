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
 * CategoryAdapter - Adapter for  category  on the Launch for the First Time  
 * @author Ideabytes
 * @version 1.0.0
 * 
 */
@SuppressLint({ "InflateParams", "ViewHolder" })
public class CategoryAdapter  extends BaseAdapter{
	protected static final String TAG = null;
	public static Context context  = null ; 

	String LOGTAG = null;
	/**
	 * Contructor to pass the Reference 
	 * @param a
	 */

	public  CategoryAdapter(Context a ) {
		context = a ;		 
	}

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

		View view = convertView;
     	view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_view, null);

		ImageView categoryimageview = (ImageView)view.findViewById(R.id.categories_image);
		TextView categorytextview = (TextView)view.findViewById(R.id.categories_name);

		Picasso.with(context)
		.load(MainScreen.CategoryThumbnails.get(position))
		.noFade()
		.tag(context)
		.into( categoryimageview);
		categorytextview.setText(MainScreen.CategoryName.get(position));
		return view;
	}


}






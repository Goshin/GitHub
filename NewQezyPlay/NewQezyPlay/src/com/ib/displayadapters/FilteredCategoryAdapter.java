package com.ib.displayadapters;

 
import com.ib.qezyplay.R; 
import com.ib.services.FilteredSelection; 
import com.squareup.picasso.Picasso; 
import android.content.Context; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter; 
import android.widget.ImageView;
import android.widget.TextView;


public class FilteredCategoryAdapter  extends BaseAdapter{
	protected static final String TAG = null;

	 public static Context context = null; 

	String LOGTAG = null; 

	public  FilteredCategoryAdapter(Context a ) {
		 context = a ;		 
	}

	 
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

 
		View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_view, null);
 
		ImageView  categoryimageview = (ImageView)view.findViewById(R.id.categories_image);
		TextView  categorytextview = (TextView)view.findViewById(R.id.categories_name);

		Picasso.with(context)
		.load(FilteredSelection.CategoryThumbnails.get(position))
		.noFade()
		.tag(this)
		.into(categoryimageview);
		
		categorytextview.setText(FilteredSelection.CategoryName.get(position));

		if(!FilteredSelection.CategorySelectedId.get(position).equals("0"))
		{
			categoryimageview.setSelected(true);
			categoryimageview.setBackgroundResource(R.drawable.list_selector);
		} 

		return view;

	}

}






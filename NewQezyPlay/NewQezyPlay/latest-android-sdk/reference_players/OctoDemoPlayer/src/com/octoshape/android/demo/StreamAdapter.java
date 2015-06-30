package com.octoshape.android.demo;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StreamAdapter extends ArrayAdapter<Stream> {

	private List<Stream> streams;
	private Context context;

	public StreamAdapter(Context context, int resource, List<Stream> streams) {
		super(context, resource, streams);
		this.context = context;
		this.streams = streams;
		Log.d("StreamAdapter", "Initialised stream adapter with " + streams.size() + " streams");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.stream_item, null);
		}
		Stream si = streams.get(position);
		if (si != null) {
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			TextView bt = (TextView) v.findViewById(R.id.bottomtext);
			if (tt != null) {
				tt.setText(si.getName());
			}
			if (bt != null) {
				bt.setText(si.getBitrate());
			}
		}
		return v;
	}
}
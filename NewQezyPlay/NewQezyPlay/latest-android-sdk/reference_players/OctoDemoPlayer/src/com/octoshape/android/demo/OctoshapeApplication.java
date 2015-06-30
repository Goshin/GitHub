package com.octoshape.android.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class OctoshapeApplication extends Application{

	public static String LOGTAG = "OctoshapeDemo";
	static List<Stream> mPlayList = new ArrayList<Stream>();
		
	@Override 
	public void onCreate(){
		super.onCreate();
		mPlayList.add(new Stream("octoshape://streams.octoshape.net/demo/live/nba/200", "ABR", "NBA - LIVE"));
		mPlayList.add(new Stream("octoshape://streams.octoshape.net/demo/live/nascar/200", "ABR", "Nascar - LIVE"));
		mPlayList.add(new Stream("octoshape://streams.octoshape.net/demo/live/pga/200", "ABR", "PGA - LIVE"));
		mPlayList.add(new Stream("octoshape://streams.octoshape.net/demoplayer/mobile/bbb/bbb_auto_mixedres", "ABR", "Big Buck Bunny - VOD"));
		mPlayList.add(new Stream("octoshape://streams.octoshape.net/demoplayer/mobile/sintel/auto_flv", "ABR", "Sintel - VOD"));
	}
	
	static public List<Stream> getPlayList(){
		return mPlayList;
	}
}

package com.octoshape.android.octoserviceplayer;

import octoshape.osa2.android.OctoshapeSystem;
import octoshape.osa2.Problem;
import octoshape.osa2.listeners.ProblemListener;

import com.octoshape.android.client.OctoshapePortListener;
import com.octoshape.android.client.OctoStatic;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.util.Log;

public class OctoService extends Activity {

	static final String LOGTAG = "OCTOSERVICE";
	OctoshapeSystem os;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_octo_service);
		os = OctoStatic.create(this, problemListener, new OctoshapePortListener() {
			
			@Override
			public void onPortBound(final String host,final int port) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						((TextView) findViewById(R.id.boundPort)).append(""+port);
					}
				});
			}
		});
		os.open();
	}
	ProblemListener problemListener = new ProblemListener() {
		@Override
		public void gotProblem(final Problem p) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Log.d(LOGTAG, "Problem: " + p.toString());
					if(p.getMessage()!=null)
						Toast.makeText(OctoService.this, "Problem: " + p.getMessage(),Toast.LENGTH_LONG).show();
				}
			});
		}
	};
	@Override
	public void onBackPressed() {
		OctoStatic.terminate(null);
		finish();
	}
}

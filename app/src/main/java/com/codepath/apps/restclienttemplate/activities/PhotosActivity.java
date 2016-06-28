package com.codepath.apps.restclienttemplate.activities;

import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.widget.GridView;

import com.codepath.apps.restclienttemplate.FlickrClientApp;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.PhotoArrayAdapter;
import com.codepath.apps.restclienttemplate.base.FlickrBaseActivity;
import com.codepath.apps.restclienttemplate.models.FlickrPhoto;
import com.codepath.apps.restclienttemplate.network.FlickrClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotosActivity extends FlickrBaseActivity {
	
	FlickrClient client;
	ArrayList<FlickrPhoto> photoItems;
	GridView gvPhotos;
	PhotoArrayAdapter adapter;

	@Override
	protected void handleUIMessage(Message pObjMessage) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);
		client = FlickrClientApp.getRestClient();
		photoItems = new ArrayList<FlickrPhoto>();
		gvPhotos = (GridView) findViewById(R.id.gvPhotos);
		adapter = new PhotoArrayAdapter(this, photoItems);
		gvPhotos.setAdapter(adapter);
		loadPhotos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos, menu);
		return true;
	}
	
	public void loadPhotos() {
		client.getInterestingnessList(new JsonHttpResponseHandler() { 
    		public void onSuccess(JSONObject json) {

                // Add new photos to SQLite
                try {
					JSONArray photos = json.getJSONObject("photos").getJSONArray("photo");
					for (int x = 0; x < photos.length(); x++) {
						String uid  = photos.getJSONObject(x).getString("id");
						FlickrPhoto p = FlickrPhoto.byPhotoUid(uid);
						if (p == null) { p = new FlickrPhoto(photos.getJSONObject(x)); };
						p.save();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
                
				// Load into GridView from DB
				for (FlickrPhoto p : FlickrPhoto.recentItems()) {
					adapter.add(p);
				}
            }
    	});
	}

}

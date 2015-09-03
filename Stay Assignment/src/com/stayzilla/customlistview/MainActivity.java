package com.stayzilla.customlistview;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.stayzilla.customlistview.adater.CustomListAdapter;
import com.stayzilla.customlistview.httprequest.HttpClientGet;
import com.stayzilla.customlistview.model.Model;
import com.stayzilla.customlistview.model.Movie;



public class MainActivity extends Activity {

	private ProgressDialog pDialog;
	private List<Movie> movieList = new ArrayList<Movie>();
	private ListView listView;
	private CustomListAdapter adapter;
	private String movieName;
	private int releasedYear;
	private double movieRating;
	private HttpClientGet httpClientGet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				movieName = movieList.get(position).getTitle();
				releasedYear = movieList.get(position).getYear();
				movieRating = movieList.get(position).getRating();
				/*Intent intent = new Intent(MainActivity.this,MovieActivity.class);
				intent.putExtra(Model.getSingleton().title, movieName);
				intent.putExtra(Model.getSingleton().rating, movieRating);
				intent.putExtra(Model.getSingleton().year, releasedYear);
				intent.putExtra(Model.getSingleton().img, posterUrl);
				startActivity(intent);*/
				Toast.makeText(MainActivity.this, "Movie: "+movieName + "\nYear:"+releasedYear+"\nRating:"+movieRating, Toast.LENGTH_LONG).show();
				
			}
		});

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		// changing action bar color
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#1b1b1b")));
		
		setMovieListData();

	}

	private void setMovieListData() {
		httpClientGet = new HttpClientGet() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

			}

			@Override
			protected void onPostExecute(String results) {
				super.onPostExecute(results);
				hidePDialog();				
				if (results != null) {
			
					try {
						JSONObject jsonObject = new JSONObject(results);
						JSONObject object = jsonObject.getJSONObject(Model
								.getSingleton().AADATA);
						
						JSONArray movies_array = object.getJSONArray(Model
								.getSingleton().MOVIES);
						for (int i = 0; i < movies_array.length(); i++) {
							JSONObject obj = movies_array.getJSONObject(i);
							Movie movie = new Movie();
							movie.setTitle(obj.getString("title"));
							movie.setThumbnailUrl(obj.getString("medium_cover_image"));
							movie.setRating(((Number) obj.get("rating"))
									.doubleValue());
							movie.setYear(obj.getInt("year"));
							// adding movie to movies array
							movieList.add(movie);
						}
						// notifying list adapter about data changes
//						// so that it renders the list view with updated data
						adapter = new CustomListAdapter(MainActivity.this, movieList);
						listView.setAdapter(adapter);
						Log.i("", "");
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(MainActivity.this,
							"Internal Network Error", Toast.LENGTH_LONG).show();
				}

			}
		};
		httpClientGet.execute(Model.getSingleton().MOVIE_LIST);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

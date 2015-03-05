package com.ratus.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ratus.gridimagesearch.R;
import com.ratus.gridimagesearch.adapters.ImageResultsAdapter;
import com.ratus.gridimagesearch.models.ImageResult;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    private EditText etQuery;
    private GridView gvResults;

    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();

        //Creates the data source
        imageResults = new ArrayList<ImageResult>();

        // Attaches the data source to an adapter
        aImageResults = new ImageResultsAdapter(this,imageResults);

        //Link the adaapter to the adapterview (gridview)
        gvResults.setAdapter(aImageResults);
    }

    private void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the image display activity
                Intent i = new Intent(SearchActivity.this,ImageDisplayActivity.class);

                // Get the display result
                ImageResult result = imageResults.get(position);
                i.putExtra("result",result);
                startActivity(i);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    // Fired whenever button is pressed
    public void onImageSearch(View v ){
       String query = etQuery.getText().toString();
        Toast.makeText(this, "Search for:" + query, Toast.LENGTH_SHORT).show();

        AsyncHttpClient client  = new AsyncHttpClient();

        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";

        client.get(searchUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG:", response.toString());
                JSONArray imageResultsJson = null;

                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    //clear the existing images from the array
                    imageResults.clear();
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));

                    //aImageResults.notifyDataSetChanged();

                    // When you make changes to the adapter you modify underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));

                    Log.i("Image Results", imageResults.toString());


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

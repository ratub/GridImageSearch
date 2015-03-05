package com.ratus.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rbhavsar on 3/3/2015.
 */
public class ImageResult implements Serializable {
    private static final long serialVersionUID = 6554608297851385131L;
    //private static final long serialVersionUID = 1L;
    public String fullUrl;
    public String thumbUrl;
    public String title;

    // new ImageResult(..raw item json..)
    public ImageResult(JSONObject json) {

        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    // Take an array of json images and return array of image results
    // ImageResult fromJSONArray([..,...])
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array){

        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

        for(int i = 0; i<array.length(); i++){

            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return results;
    }

}



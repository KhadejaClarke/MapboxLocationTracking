package com.khadejaclarke.mapboxlocationtracking.async;

import android.content.Context;
import android.os.AsyncTask;

import com.khadejaclarke.mapboxlocationtracking.MainActivity;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;

/**
 * AsyncTask to load data from the assets folder.
 */
public class LoadGeoJsonDataTask extends AsyncTask<Void, Void, FeatureCollection> {

    private final WeakReference<MainActivity> activityRef;

    private static final String PROPERTY_SELECTED = "selected";


    public LoadGeoJsonDataTask(MainActivity activity) {
        this.activityRef = new WeakReference<>(activity);
    }

    @Override
    protected FeatureCollection doInBackground(Void... params) {
        MainActivity activity = activityRef.get();

        if (activity == null) {
            return null;
        }

        String geoJson = loadGeoJsonFromAsset(activity, "trucks.geojson");
        return FeatureCollection.fromJson(geoJson);
    }

    @Override
    protected void onPostExecute(FeatureCollection featureCollection) {
        super.onPostExecute(featureCollection);
        MainActivity activity = activityRef.get();
        if (featureCollection == null || activity == null) {
            return;
        }

        // This example runs on the premise that each GeoJSON Feature has a "selected" property,
        // with a boolean value. If your data's Features don't have this boolean property,
        // add it to the FeatureCollection 's features with the following code:
        for (Feature singleFeature : featureCollection.features()) {
            singleFeature.addBooleanProperty(PROPERTY_SELECTED, false);
        }

        activity.setUpData(featureCollection);
        new GenerateViewIconTask(activity).execute(featureCollection);
    }

    static String loadGeoJsonFromAsset(Context context, String filename) {
        try {
            // Load GeoJSON file from local asset folder
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, Charset.forName("UTF-8"));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
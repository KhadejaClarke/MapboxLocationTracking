package com.khadejaclarke.mapboxlocationtracking.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khadejaclarke.mapboxlocationtracking.MainActivity;
import com.khadejaclarke.mapboxlocationtracking.R;
import com.khadejaclarke.mapboxlocationtracking.models.Coordinates;
import com.khadejaclarke.mapboxlocationtracking.utils.SymbolGenerator;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;


import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * AsyncTask to generate Bitmap from Views to be used as iconImage in a SymbolLayer.
 * <p>
 * Call be optionally be called to update the underlying data source after execution.
 * </p>
 * <p>
 * Generating Views on background thread since we are not going to be adding them to the view hierarchy.
 * </p>
 */
public class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {

    private final HashMap<String, View> viewMap = new HashMap<>();
    private final WeakReference<MainActivity> activityRef;
    private final boolean refreshSource;

    private static final String PROPERTY_ID = "name";
    private static final String PROPERTY_COORDS = "coordinates";


    GenerateViewIconTask(MainActivity activity, boolean refreshSource) {
        this.activityRef = new WeakReference<>(activity);
        this.refreshSource = refreshSource;
    }

    GenerateViewIconTask(MainActivity activity) {
        this(activity, false);
    }

    @SuppressWarnings("WrongThread")
    @Override
    protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
        MainActivity activity = activityRef.get();
        if (activity != null) {
            HashMap<String, Bitmap> imagesMap = new HashMap<>();
            LayoutInflater inflater = LayoutInflater.from(activity);

            FeatureCollection featureCollection = params[0];

            for (Feature feature : featureCollection.features()) {

                BubbleLayout bubbleLayout = (BubbleLayout)
                        inflater.inflate(R.layout.symbol_layer_info_window_layout_callout, null);

                String id = feature.getStringProperty(PROPERTY_ID);
                TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
                titleTextView.setText(id);

                String coords = feature.getStringProperty(PROPERTY_COORDS);
                Type listType = new TypeToken<List<Coordinates>>(){}.getType();
                List<Coordinates> coordinates = new Gson().fromJson(coords, listType);
                TextView descriptionTextView = bubbleLayout.findViewById(R.id.info_window_description);
                descriptionTextView.setText(
                        String.format(activity.getString(R.string.coords), coordinates.get(0), coordinates.get(1)));

                int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                bubbleLayout.measure(measureSpec, measureSpec);

                float measuredWidth = bubbleLayout.getMeasuredWidth();

                bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

                Bitmap bitmap = SymbolGenerator.generate(bubbleLayout);
                imagesMap.put(id, bitmap);
                viewMap.put(id, bubbleLayout);
            }

            return imagesMap;

        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
        super.onPostExecute(bitmapHashMap);
        MainActivity activity = activityRef.get();
        if (activity != null && bitmapHashMap != null) {
            activity.setImageGenResults(bitmapHashMap);
            if (refreshSource) {
                activity.refreshSource();
            }
        }
        Toast.makeText(activity, R.string.tap_on_marker_instruction, Toast.LENGTH_SHORT).show();
    }
}
package se.jolo.prototypenavigator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.location.LocationServices;

import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.activities.FileBrowser;
import se.jolo.prototypenavigator.activities.Loader;
import se.jolo.prototypenavigator.activities.Map;
import se.jolo.prototypenavigator.model.Route;


public final class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "MainActivity";
    private Uri uri;
    private static Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // gps goes here
        if (!LocationServices.getLocationServices(this).isGPSEnabled()) {
            showGpsDialog();
        }

        loader = new Loader(this);

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Intent switchToFileBrowser = new Intent(this, FileBrowser.class);
            startActivity(switchToFileBrowser);
        } else {
            uri = (Uri) extras.get("uri");

            Intent mapIntent = new Intent(this, Map.class).putExtra("uri", uri);
            startActivity(mapIntent);
            //loader.execute(uri);
//            try {
//                Route route = loader.get();
//                Log.d(LOG_TAG, "in MainActivity " + route.getUuid());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void showGpsDialog() {
    }

    public static Loader getLoader(){
        return loader;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
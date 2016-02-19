package se.jolo.prototypenavigator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
    private LocationManager locationManager;
    private boolean isGpsEnabled = false;
    private boolean allowInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGps();

        if (allowInit) {
            init();
            allowInit = false;
        }

    }

    public void initGps() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGpsEnabled) {
            showGpsDialog();
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        if (isGpsEnabled) {
            allowInit = true;
        }
    }

    public void init() {
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Intent switchToFileBrowser = new Intent(this, FileBrowser.class);
            startActivity(switchToFileBrowser);
        } else {
            uri = (Uri) extras.get("uri");
            Intent mapIntent = new Intent(this, Map.class).putExtra("uri", uri);
            startActivity(mapIntent);
        }
    }

    private void showGpsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS not enabled! This app is pointless without GPS.");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (allowInit) {
            init();
            allowInit = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (allowInit) {
            init();
            allowInit = false;
        }
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
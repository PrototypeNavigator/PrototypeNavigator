package se.jolo.prototypenavigator.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.utils.Locator;

public final class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "MainActivity";
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            Locator.isGpsEnabled = savedInstanceState.getBoolean("gps");
            Locator.allowInit = savedInstanceState.getBoolean("init");
        }

        initGps();

        if (Locator.allowInit) {
            init();
            Locator.allowInit = false;
        }

    }

    public void initGps() {

        Locator.isGpsEnabled = Locator.isGpsEnabled(this);

        if (!Locator.isGpsEnabled) {
            showGpsDialog();
            Locator.isGpsEnabled = Locator.isGpsEnabled(this);
        }

        if (Locator.isGpsEnabled) {
            Locator.allowInit = true;
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
                Locator.allowInit = true;
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Locator.allowInit = false;
            }
        });

        alertDialog.show();
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
        outState.putBoolean("gps", Locator.isGpsEnabled);
        outState.putBoolean("init", Locator.allowInit);

        super.onSaveInstanceState(outState);
    }
}
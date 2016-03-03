package se.jolo.prototypenavigator.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.utils.Locator;

public final class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static String LOG_TAG = "MainActivity";
    private TextView tvWelcome;
    private Button btnLoadNewRoute, btnLoadPreRoute;
    private Spinner spnrLoadRoute;
    private Loader loader;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            Locator.isGpsEnabled = savedInstanceState.getBoolean("gps");
            Locator.allowInit = savedInstanceState.getBoolean("init");
        }

        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        btnLoadNewRoute = (Button) findViewById(R.id.btnLoadNewRoute);
        btnLoadPreRoute = (Button) findViewById(R.id.btnLoadPreRoute);

        initGps();
        setButtonClickListener();
        loadSpinner();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvWelcome.setText(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void setButtonClickListener() {

        btnLoadNewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Locator.allowInit) {
                    init();
                }
            }
        });

        btnLoadPreRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void loadSpinner() {
        loader = new Loader(this);

        spnrLoadRoute = (Spinner) findViewById(R.id.spnrLoadRoute);
        spnrLoadRoute.setOnItemSelectedListener(this);

        List<String> fileNames = new ArrayList<>();

        File[] files = loader.loadSavedFiles();
        for (File f : files) {
            fileNames.add(f.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, fileNames);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnrLoadRoute.setAdapter(adapter);
    }

    public void initGps() {

        Locator.isGpsEnabled = Locator.isGpsEnabled(this);

        if (!Locator.isGpsEnabled) {
            showGpsDialog();
            Locator.isGpsEnabled = Locator.isGpsEnabled(this);
            Locator.allowInit = Locator.isGpsEnabled;
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
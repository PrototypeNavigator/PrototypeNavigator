package se.jolo.prototypenavigator.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.task.Loader;
import se.jolo.prototypenavigator.utils.Locator;

/*The Launcher class launches the app and gives the user a couple of different options to choose a route.*/
public final class Launcher extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static String LOG_TAG = "Launcher";
    private TextView tvWelcome;
    private Loader loader;
    private String fileName;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        Drawable background = getResources().getDrawable(R.drawable.sky2);
        relativeLayout.setBackground(background);

        loader = new Loader(this);

        tvWelcome = (TextView) findViewById(R.id.tvWelcome);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        initGps();

        setButtonClickListener();
        loadSpinner();
    }

    /*********************************************************************************************/
    /****                                     Spinner                                         ****/
    /*********************************************************************************************/

    /*Spinner containing a list of previously loaded routes.*/
    public void loadSpinner() {

        Spinner spnrLoadRoute = (Spinner) findViewById(R.id.spnrLoadRoute);
        spnrLoadRoute.setOnItemSelectedListener(this);

        if (loader.loadSavedFiles() != null) {

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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvWelcome.setText(parent.getItemAtPosition(position).toString());
        fileName = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /*********************************************************************************************/
    /****                                   Load Route                                        ****/
    /*********************************************************************************************/

    /**
     * Initialize route from a new file. If bundle is null launch file-browser-activity to
     * locate new file. If bundle contains uri, launch Map activity with uri to chosen new file.
     */
    public void initNewFile() {
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Intent switchToFileBrowser = new Intent(this, FileBrowser.class);
            startActivity(switchToFileBrowser);
        } else {
            Uri uri = (Uri) extras.get("uri");
            Intent mapIntent = new Intent(this, Map.class).putExtra("uri", uri);
            startActivity(mapIntent);
        }
    }

    /**
     * Initialize route from previously selected file.
     */
    public void initPreLoadedFile() {
        if (loader.loadSavedFiles().length > 0) {
            Intent mapIntent = new Intent(this, Map.class).putExtra("str", fileName);
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "No route to load. Please choose a new file", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Buttons to launch either initialization with new file or with previously save file.
     */
    private void setButtonClickListener() {
        Button btnLoadNewRoute = (Button) findViewById(R.id.btnLoadNewRoute);
        btnLoadNewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewFile();
            }
        });

        Button btnLoadPreRoute = (Button) findViewById(R.id.btnLoadPreRoute);
        btnLoadPreRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPreLoadedFile();
            }
        });
    }

    /*********************************************************************************************/
    /****                                        GPS                                          ****/
    /*********************************************************************************************/

    /**
     * Check if GPS is enabled. If not launch settings dialog.
     */
    public void initGps() {
        Locator.isGpsEnabled = Locator.isGpsEnabled(this);

        if (!Locator.isGpsEnabled) {
            showGpsDialog();
            Locator.isGpsEnabled = Locator.isGpsEnabled(this);
        }
    }

    /**
     * Settings dialog, launched if GPS isn't enabled.
     */
    private void showGpsDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Launcher.this);

        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS not enabled! This app is pointless without GPS.");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
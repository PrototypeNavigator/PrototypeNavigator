package se.jolo.prototypenavigator.activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;

import com.mapbox.mapboxsdk.constants.MyBearingTracking;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;

import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.Locator;
import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.Router;
import se.jolo.prototypenavigator.model.Route;

public class Map extends AppCompatActivity {

    private final static String LOG_TAG = "MapActivity";
    private final static String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicHJvdG90eXBldGVhbSIsImEiOiJjaWs2bXQ3Y3owMDRqd2JtMTZsdjhvbzVnIn0.NBH7u7RG-lqxGq_PEIjFjw";
    private FloatingActionButton findMeBtn;
    private List<Waypoint> waypoints = null;
    private MapView mapView;
    private Router router;
    private TextView textView;
    private ViewGroup viewGroup;
    private Route route;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        Loader loader = new Loader(this);

        mapView = loadMap(savedInstanceState);
        
        Locator.enableLocation(this, this, mapView);
        Locator.toggleTracking(this, this, mapView);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("test");

        textView = (TextView) findViewById(R.id.textTop);
        viewGroup = (ViewGroup) findViewById(R.id.textAndMenu);
        setSupportActionBar(myToolbar);

        findMeBtn = (FloatingActionButton) findViewById(R.id.findMeBtn);
        uri = (Uri) extras.get("uri");

        loader.execute(uri);


        try {
            route = loader.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        router = new Router(this, mapView, MAPBOX_ACCESS_TOKEN);
        router.setCurrentLocation(Locator.getLocation(this))
                .loadWaypoints(route)
                .loadRoute();

        waypoints = router.getWaypoints();

        // centroid goes here
        LatLng centroid = new LatLng(Locator.getLocation(this).getLatitude(),Locator.getLocation(this).getLongitude());
        setCentroid(centroid);

        addMarkers(waypoints);


        findMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera(new LatLng(mapView.getLatLng()));
                router.onLocationChanged(router.getLocation());
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(viewGroup, new Slide());
                toggleVisibility(textView);
                
            }
        });

        mapView.onCreate(savedInstanceState);
    }


    public void toggleBearing() throws SecurityException{
        mapView.setMyBearingTrackingMode(MyBearingTracking.COMPASS);
    };


    public void animateCamera(LatLng latLng) {
        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 13, 45, 0)));
    }

    private MapView loadMap(Bundle savedInstanceState) {

        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setAccessToken(MAPBOX_ACCESS_TOKEN);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        mapView.onCreate(savedInstanceState);

        mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Waypoint target = new Waypoint(point.getLongitude(), point.getLatitude());
                router.checkOffRoute(target);
            }
        });

        return mapView;
    }

    private void addMarkers(List<Waypoint> waypoints) {
        for (Waypoint w : waypoints) {
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(w.getLatitude(), w.getLongitude())));
            Log.d("marker", w.toString());
        }
    }

    private void setCentroid(LatLng centroid) {
        mapView.setCenterCoordinate(centroid);
        animateCamera(centroid);
    }


    private static void toggleVisibility(View... views) {
        for (View view : views) {
            boolean isVisible = view.getVisibility() == View.VISIBLE;
            view.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        toggleBearing();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

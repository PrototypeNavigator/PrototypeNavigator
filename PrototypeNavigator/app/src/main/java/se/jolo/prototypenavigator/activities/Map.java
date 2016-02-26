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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.CallCounter;
import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.Router;
import se.jolo.prototypenavigator.model.Route;

public class Map extends AppCompatActivity implements LocationListener {

    private final static String LOG_TAG = "MapActivity";
    private final static String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicHJvdG90eXBldGVhbSIsImEiOiJjaWs2bXQ3Y3owMDRqd2JtMTZsdjhvbzVnIn0.NBH7u7RG-lqxGq_PEIjFjw";
    private final static int PERMISSIONS_LOCATION = 0;
    private MapView mapView;
    private FloatingActionButton findMeBtn;
    private LocationServices locationServices;
    private List<Waypoint> waypoints = null;
    private Uri uri;
    private Route route;
    private Router router;
    private TextView textView;
    private ViewGroup viewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        Loader loader = new Loader(this);

        locationServices = LocationServices.getLocationServices(this);
        findMeBtn = (FloatingActionButton) findViewById(R.id.findMeBtn);
        mapView = loadMap(savedInstanceState);
        uri = (Uri) extras.get("uri");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("test");

        textView = (TextView) findViewById(R.id.textTop);
        viewGroup = (ViewGroup) findViewById(R.id.textAndMenu);
        setSupportActionBar(myToolbar);
        loader.execute(uri);


        try {
            route = loader.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        enableLocation();


        // next stop routing
        router = new Router(this, mapView, MAPBOX_ACCESS_TOKEN);
        router.loadWaypoints(route)
                .setCurrentLocation(LocationServices.getLocationServices(this).getLastLocation())
                .loadFullRoute()
                .loadRoute();

        waypoints = router.getWaypoints();

        // centroid goes here
        LatLng centroid = new LatLng(locationServices.getLastLocation().getLatitude(),locationServices.getLastLocation().getLongitude());
        setCentroid(centroid);

        addMarkers(waypoints);


        findMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera(new LatLng(mapView.getLatLng()));
                toggleTracking();
                onLocationChanged(locationServices.getLastLocation());
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

    public void enableLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_LOCATION);
        } else {
            mapView.setMyLocationEnabled(true);
        }
    }

    public void toggleTracking() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_LOCATION);
        } else {
            mapView.setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
        }
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

    @Override
    public void onLocationChanged(Location location) {
        animateCamera(new LatLng(location.getLatitude(), location.getLongitude()));
        router.setCurrentLocation(location).loadRoute();
        router.removePolyline(router.getPolylineToNextStop());
        Toast.makeText(this, "calls made ::: " + CallCounter.getCounts(), Toast.LENGTH_LONG).show();
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

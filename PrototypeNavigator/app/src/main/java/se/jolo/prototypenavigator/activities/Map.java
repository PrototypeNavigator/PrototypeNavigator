package se.jolo.prototypenavigator.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
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
                .loadFullRoute()
                .loadRoute();

        waypoints = router.getWaypoints();

        // centroid goes here
        LatLng centroid = new LatLng(
                (waypoints.get(0).getLatitude() + waypoints.get(waypoints.size() - 1).getLatitude()) / 2,
                (waypoints.get(0).getLongitude() + waypoints.get(waypoints.size() - 1).getLongitude()) / 2);
        setCentroid(centroid);

        addMarkers(waypoints);

        findMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera(new LatLng(mapView.getLatLng()));
                router.onLocationChanged(router.getLocation());
            }
        });

        mapView.onCreate(savedInstanceState);
    }

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
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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

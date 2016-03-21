package se.jolo.prototypenavigator.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.directions.service.models.RouteStep;
import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.demo.MockLocationProvider;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.utils.Locator;
import se.jolo.prototypenavigator.utils.RouteManager;
import se.jolo.prototypenavigator.utils.Speech;

public class Map extends AppCompatActivity {

    private final static String LOG_TAG = "MapActivity";
    private final static String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicHJvdG90eXBldGVhbSIsImEiOiJjaWs2bXQ3Y3owMDRqd2JtMTZsdjhvbzVnIn0.NBH7u7RG-lqxGq_PEIjFjw";

    private FloatingActionButton findMeBtn;
    private TextView textView;
    private Toolbar myToolbar;
    private Button plus;
    private Button minus;

    private Speech speech;

    private List<Waypoint> waypoints = null;
    private List<RouteStep> steps;
    private RouteManager routeManager;
    private MapView mapView;
    private LatLng centroid;
    private Locator locator;
    private Route route;

    private boolean day = true;

    private ViewGroup viewGroup;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locator = new Locator(this, this);
        locator.init();

        Loader loader = new Loader(this);
        Bundle extras = getIntent().getExtras();

        loadRoute(extras, loader);

        routeManager = loadManager(locator);

        myToolbar = makeToolbar();
        viewGroup = makeViewGroup();
        textView = makeTextView();
        mapView = loadMap();
       // waypoints = routeManager.getWaypoints();
        findMeBtn = initFindMeBtn();
        plus = initPlusBtn();
        minus = initMinusBtn();
        centroid = setCentroid(locator);

        setSupportActionBar(myToolbar);
        addMarkers();

        locator.setMapViewAndRouteManager(mapView, routeManager);

        mapView.onCreate(savedInstanceState);
    }

    /*********************************************************************************************/
    /****                                     Other                                           ****/
    /*********************************************************************************************/

    private ViewGroup makeViewGroup() {
        return (ViewGroup) findViewById(R.id.textAndMenu);
    }

    private Toolbar makeToolbar() {
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        return myToolbar;
    }

    /**
     * Initialize TextView and setting OnClickListener to toggle visibility.
     *
     * @return newly initialized TextView
     */
    private TextView makeTextView() {

        textView = (TextView) findViewById(R.id.textTop);

        textView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(viewGroup, new AutoTransition());
                toggleVisibility(textView);
                speech = new Speech(Map.this);
                speech.say(textView.getText().toString());
            }
        });

        return textView;
    }

    /**
     * Initialize FloatingActionButton to call animateCamera() with at current location.
     * If able to get location calls onLocationChange() in RouteManager.
     *
     * @return FloatingActionButton
     */
    private FloatingActionButton initFindMeBtn() {

        findMeBtn = (FloatingActionButton) findViewById(R.id.findMeBtn);

        findMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera(new LatLng(mapView.getLatLng()));

//                if (Locator.ableToGetLocation) {
//                    routeManager.onLocationChanged(locator.getLocation());
//                }

                Toast.makeText(v.getContext(), "at: "
                                + routeManager.getNextStop().getOrder() + " "
                                + routeManager.getNextStop().getStopPoint().getType(),
                        Toast.LENGTH_LONG).show();
            }
        });

        return findMeBtn;
    }

    /**
     * Initialize plus button
     * @return plus
     * */
    private Button initPlusBtn(){

        plus = (Button) findViewById(R.id.plus);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double currentZoom = mapView.getZoom();
                mapView.setZoom(currentZoom + 1, true);
            }
        });
        return plus;
    }

    /**
     * Initialize plus button
     * @return minus
     * */
    private Button initMinusBtn(){

        minus = (Button) findViewById(R.id.minus);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double currentZoom = mapView.getZoom();
                Log.d(LOG_TAG, mapView.getZoom()+"");
                Log.d("id",minus.getId()+"");
                mapView.setZoom(currentZoom - 1, true);
            }
        });
        return minus;
    }

    /**
     * Toggles visibility of NextStep-view
     *
     * @param views NextStep
     */
    private static void toggleVisibility(View... views) {
        for (View view : views) {
            boolean isVisible = view.getVisibility() == View.VISIBLE;
            view.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private Drawable getScaledDrawable(int newWidth, int newHeight, int id) {

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), id);

        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return new BitmapDrawable(this.getResources(), scaledBitmap);
    }

    /*********************************************************************************************/
    /****                                     Route                                           ****/
    /*********************************************************************************************/

    /**
     * Initialize RouteManager, loading RouteItems and Waypoints. Setting current location.
     * Loads selected route.
     *
     * @return loaded RouteManager
     */
    private RouteManager loadManager(Locator locator) {
        routeManager = new RouteManager(this, locator);
        routeManager.loadRouteItemsAndWaypoints(route).loadPolylines();
        return routeManager;
    }

    /**
     * Loads a newly selected Route from URI, or loads previously saved Route.
     *
     * @param extras bundle containing URI
     * @param loader loading the route from file
     */
    public void loadRoute(Bundle extras, Loader loader) {

        if (extras.get("uri") != null) {

            uri = (Uri) extras.get("uri");
            loader.execute(uri);

            try {
                route = loader.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        } else if (extras.get("str") != null) {

            try {
                route = loader.getPreLoadedRoute(extras.getString("str"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*********************************************************************************************/
    /****                                      Map                                            ****/
    /*********************************************************************************************/

    /**
     * Initializes the MapView setting values and location tracking if able.
     *
     * @return newly set MapView
     */
    private MapView loadMap() {

        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setAccessToken(MAPBOX_ACCESS_TOKEN);
        if(day){
            mapView.setStyleUrl(Style.LIGHT);
        }else {
            mapView.setStyleUrl(Style.DARK);
        }


        mapView.setCompassGravity(Gravity.BOTTOM);
        mapView.setLogoVisibility(View.INVISIBLE);
        mapView.removeView(mapView.getTouchables().get(1));
        mapView.setOnMapClickListener(new MapView.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Waypoint target = new Waypoint(point.getLongitude(), point.getLatitude());
                routeManager.checkOffRoute(target);
            }
        });

        if (Locator.ableToGetLocation) {
            Locator.enableLocation(mapView);
            Locator.toggleTracking(mapView);
        } else {
            Toast.makeText(this, "Unable to acquire location. Please leave the "
                    + "woods/cave/cellar and/or the elevator", Toast.LENGTH_LONG).show();
        }

        mapView.addPolyline(routeManager.getPolylineToNextStop());
        textView.setText(routeManager.getInstruction().getReadableInstruction());
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setTitle(routeManager.getNextStop().getStopPointItems().get(0).getDeliveryAddress());

        return mapView;
    }

    /**
     * Adds Waypoint markers for full Route.
     *
     *
     */
    private void addMarkers() {
        IconFactory mIconFactory = IconFactory.getInstance(this);
        Drawable mIconDrawable = getScaledDrawable(25,25, R.drawable.dot2);

        Icon icon = mIconFactory.fromDrawable(mIconDrawable);

        List<RouteItem> routeItems = routeManager.getRouteItems();
        for (int i = 0; i<routeItems.size(); i++){
            RouteItem r = routeItems.get(i);
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(r.getStopPoint().getNorthing(), r.getStopPoint().getEasting()))
                    .icon(icon)
                    .title(routeItems.get(i).getStopPointItems().get(0).getDeliveryAddress()));

        }
    }

    /**
     * Set centroid to current location if able, else set it to next waypoint.
     *
     * @param locator LocationHandler
     * @return LatLng, centroid position
     */
    private LatLng setCentroid(Locator locator) {

        LatLng centroid = (locator.getLocation() != null)
                ? new LatLng(locator.getLocation().getLatitude(), locator.getLocation().getLongitude())
                : new LatLng(waypoints.get(0).getLatitude(), waypoints.get(0).getLongitude());

        mapView.setCenterCoordinate(centroid);
        animateCamera(centroid);

        return centroid;
    }

    /**
     * Set camera position, angle and zoom.
     *
     * @param latLng new camera position
     */
    public void animateCamera(LatLng latLng) {
        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(
                routeManager.getCameraPosition(latLng)));
    }

    /*********************************************************************************************/
    /****                                      Menu                                           ****/
    /*********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loadRoute:
                Intent sendToFileBrowser = new Intent(this, FileBrowser.class);
                startActivity(sendToFileBrowser);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            case R.id.showDetails:
                Intent sendToDetails = new Intent(this, RouteListActivity.class);
                sendToDetails.putExtra("route", route);
                startActivity(sendToDetails);
                return true;
            case R.id.showRoute:
                PolylineOptions polylineOptions = routeManager.getPolylinefullRoute();

                if(item.getTitle().equals("Visa rutt på karta")){
                    item.setTitle("göm rutt");
                    mapView.addPolyline(polylineOptions);
                    return true;
                }else {
                    item.setTitle("Visa rutt på karta");
                    mapView.removeAnnotation(polylineOptions.getPolyline());
                    return true;
                }
            case R.id.startStopDemo:
                if (locator.isDemoRunning()) {
                    locator.getHandler().removeCallbacks(locator.getTask());
                    locator.setDemoRunning(false);
                } else {
                    locator.mockLocation();
                    locator.setDemoRunning(true);
                }
            case R.id.dayVsNight:
                if(item.getTitle().equals("Dag")){
                    item.setTitle("Natt");
                    day = false;
                    loadMap();
                }else {
                    item.setTitle("Dag");
                    day = true;
                    loadMap();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    /*********************************************************************************************/
    /****                                     Lifecycle                                       ****/
    /*********************************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Start");
        mapView.onStart();
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "Resume");
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "Pause");
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "Stop");
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "Destroy");
        super.onDestroy();
        locator.getMockLocationProvider().shutdown();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

package se.jolo.prototypenavigator.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.singleton.RouteHolder;
import se.jolo.prototypenavigator.task.ImageLoader;
import se.jolo.prototypenavigator.utils.UrlBuilderMarkerImg;

public class MarkerService extends Service {

    private static final String LOG_TAG = "MarkerService";
    private List<MarkerOptions> markers;
    private List<RouteItem> routeItems;

    public Thread thread;

    private void addMarkers() throws ExecutionException, InterruptedException {

        IconFactory mIconFactory = IconFactory.getInstance(this);

        Log.d(LOG_TAG, "RouteHolder ::: routeItems.size() ::: " + RouteHolder.INSTANCE.getRoute().getRouteItems().size()
                        + " routeItems.size() ::: " + routeItems.size());
        routeItems = RouteHolder.INSTANCE.getRoute().getRouteItems();

        for (int i = 0; i < routeItems.size(); i++) {

            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(UrlBuilderMarkerImg.getMarkerUrl(i + 1));
            Bitmap bitmap = imageLoader.get();

            Drawable mIconDrawable = getScaledDrawable(50, 50, bitmap);
            Icon icon = mIconFactory.fromDrawable(mIconDrawable);

            RouteItem routeItem = routeItems.get(i);

            markers.add(new MarkerOptions()
                    .position(new LatLng(routeItem.getStopPoint().getNorthing(), routeItem.getStopPoint().getEasting()))
                    .icon(icon)
                    .title(routeItems.get(i).getStopPointItems().get(0).getDeliveryAddress()));

            Log.d(LOG_TAG, "markers ::: " + markers.size());
        }

        sendMarkersToMap();
    }

    private Drawable getScaledDrawable(int newWidth, int newHeight, Bitmap bitmap) {

        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return new BitmapDrawable(this.getResources(), scaledBitmap);
    }

    private void sendMarkersToMap() {

        Log.d(LOG_TAG, "context ::: " + RouteHolder.INSTANCE.getContext());

        Intent markersIntent = new Intent("markers");
        markersIntent.putExtra("Status", "done");
        RouteHolder.INSTANCE.setMarkers(markers);

        Log.d(LOG_TAG, "markers ::: " + markers.isEmpty());

        LocalBroadcastManager.getInstance(RouteHolder.INSTANCE.getContext()).sendBroadcast(markersIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        markers = new ArrayList<>();
        routeItems = new ArrayList<>();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    addMarkers();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

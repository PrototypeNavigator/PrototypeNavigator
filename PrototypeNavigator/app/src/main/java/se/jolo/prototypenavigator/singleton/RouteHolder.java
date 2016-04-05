package se.jolo.prototypenavigator.singleton;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.List;

import se.jolo.prototypenavigator.model.Route;

/* The RouteHolder class holds information that is necessary for the app, across multiple activities. */
public enum  RouteHolder implements Parcelable {
    INSTANCE {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(getContext());
            dest.writeValue(getRoute());
            dest.writeValue(getMapView());
            dest.writeList(getMarkers());
        }
    };

    private Context context;
    private Route route;
    private MapView mapView;
    private List<MarkerOptions> markers;

    RouteHolder(){}

    RouteHolder(Parcel in) {
        in.readList(markers, MarkerOptions.class.getClassLoader());
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }
    public void setMarkers(List<MarkerOptions> markers) {
        this.markers = markers;
    }
    public List<MarkerOptions> getMarkers() {
        return markers;
    }
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }
    public MapView getMapView() {
        return mapView;
    }
    public void setRoute(Route route){
        this.route=route;
    }
    public Route getRoute(){
        return route;
    }

}

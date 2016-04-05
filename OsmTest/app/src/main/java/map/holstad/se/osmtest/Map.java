package map.holstad.se.osmtest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Map extends AppCompatActivity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapView map = (MapView) findViewById(R.id.map);
        String[] baseUrl = new String[1];
        baseUrl[0] = "http://overlay.openstreetmap.nl/openfietskaart-rcn/";
        map.getController().setCenter(new GeoPoint(54.178181, 15.579021));

/*        map.setTileSource(new OnlineTileSourceBase("USGS Topo", 0, 18, 256, "",
                new String[]{"http://basemap.nationalmap.gov/ArcGIS/rest/services/USGSTopo/MapServer/tile/"}) {
            @Override
            public String getTileURLString(MapTile aTile) {
                return getBaseUrl() + aTile.getZoomLevel() + "/" + aTile.getY() + "/" + aTile.getX()
                        + mImageFilenameEnding;
            }
        });*/
        final ITileSource tileSource = TileSourceFactory.MAPQUESTAERIAL;
        map.setTileSource(tileSource);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
    }
}
package se.jolo.prototypenavigator.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

import se.jolo.prototypenavigator.model.Route;

/**
 * Created by Joel on 2016-02-09.
 */
public final class FileLoader {

    private static final String TAG = "XmlDeserializer";
    private Context context;

    public FileLoader(Context context) {
        this.context = context;
    }

    public InputStream loadFile() {
        return context.getResources().openRawResource(context.getResources()
                .getIdentifier("routexmltest", "raw", context.getPackageName()));
    }

    public Route routeDeserialize() {
        Serializer serializer = new Persister();
        Route route = null;

        Log.v(TAG, "inside Desirializer");
        try {
            route = serializer.read(Route.class, loadFile());
            Log.v(TAG, route.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return route;
    }
}

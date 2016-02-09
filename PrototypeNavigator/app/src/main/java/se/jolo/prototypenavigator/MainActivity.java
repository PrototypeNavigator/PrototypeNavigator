package se.jolo.prototypenavigator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import se.jolo.prototypenavigator.route.model.Route;
import se.jolo.prototypenavigator.utils.FileLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream fileIn = getResources().openRawResource(getResources()
                .getIdentifier("routexmltest", "raw", getPackageName()));

        InputStreamReader InputRead = new InputStreamReader(fileIn);

        Serializer serializer = new Persister();
        try {
            Route route = serializer.read(Route.class, fileIn);
            Toast.makeText(this, route.getType(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("file ", "...");
    }


}

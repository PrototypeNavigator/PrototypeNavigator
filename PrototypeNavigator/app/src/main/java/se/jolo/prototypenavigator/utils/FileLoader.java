package se.jolo.prototypenavigator.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Joel on 2016-02-09.
 */
public class FileLoader extends AppCompatActivity {

    Context context;

    public FileLoader(Context context) {
        this.context = context;
    }

    public InputStream loadFile() {

        InputStream fileIn = context.getResources().openRawResource(getResources()
                .getIdentifier("routexmltest", "raw", getPackageName()));

        InputStreamReader InputRead = new InputStreamReader(fileIn);

        return fileIn;

    }
}

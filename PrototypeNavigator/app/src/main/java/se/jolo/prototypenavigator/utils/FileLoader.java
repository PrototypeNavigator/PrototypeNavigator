package se.jolo.prototypenavigator.utils;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public String xmlToString() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(loadJsonFile()));
        StringBuilder builder = new StringBuilder();
        String receiver;

        while ((receiver = reader.readLine()) != null) {
            builder.append(receiver);
        }

        reader.close();
Log.e(TAG, builder.toString());
        return builder.toString();
    }

    public InputStream loadJsonFile() {
        return context.getResources().openRawResource(context.getResources()
                .getIdentifier("codebeautify", "raw", context.getPackageName()));
    }

    public InputStream loadFile() {
        return context.getResources().openRawResource(context.getResources()
                .getIdentifier("routexmltest", "raw", context.getPackageName()));
    }

    public File getAsJson() throws IOException, JSONException {
        String content = XML.toJSONObject(xmlToString()).toString();
        File file = new File(context.getResources().toString());
        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
        writer.write(content);
        writer.close();
        Log.v(TAG, content);
        return file;
    }
}

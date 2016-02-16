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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import se.jolo.prototypenavigator.model.Route;

/**
 * Created by Joel on 2016-02-09.
 */
public final class FileLoadAndConvert {

    private static final String TAG = "XmlDeserializer";
    private Context context;

    public FileLoadAndConvert(Context context) {
    }

    public String xmlToString(String xmlPath) {

        StringBuilder builder = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(loadFile(xmlPath)))) {

            builder = new StringBuilder();
            String receiver;

            while ((receiver = reader.readLine()) != null) {
                builder.append(receiver);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG, builder.toString());

        return builder.toString();
    }

    public InputStream loadFile(String xmlPath) throws IOException{
        InputStream inputStream = new FileInputStream(xmlPath);
        return  inputStream;
    }

    public String xmlToJson(String xmlPath) throws IOException, JSONException {
        return XML.toJSONObject(xmlToString(xmlPath)).toString();
    }
}

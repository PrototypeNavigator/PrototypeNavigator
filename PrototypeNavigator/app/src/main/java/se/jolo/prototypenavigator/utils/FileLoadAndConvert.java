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
import java.io.Reader;

import se.jolo.prototypenavigator.model.Route;

/**
 * Created by Joel on 2016-02-09.
 */
public final class FileLoadAndConvert {

    private static final String TAG = "XmlDeserializer";
    private Context context;

    public FileLoadAndConvert(Context context) {
        this.context = context;
    }

    public String xmlToString() {

        StringBuilder builder = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(loadFile()))) {

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

    // sparar för den är fin och vem vet, kanske behövs.
    public Reader xmlToReader() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(loadFile()))) {

            return reader;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public InputStream loadJsonFile() {
        return context.getResources().openRawResource(context.getResources()
                .getIdentifier("codebeautify", "raw", context.getPackageName()));
    }

    public InputStream loadFile() {
        return context.getResources().openRawResource(context.getResources()
                .getIdentifier("routexmltest", "raw", context.getPackageName()));
    }

    public String xmlToJson() throws IOException, JSONException {
        return XML.toJSONObject(xmlToString()).toString();
    }
}

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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

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




}

package se.jolo.prototypenavigator.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.utils.JsonMapper;

public final class Loader extends AsyncTask<Uri, Integer, Route> {
    private Context context;
    private Route route = null;
    private Activity activity;

    public Loader(Context context){
        this.context = context;
    }

    @Override
    protected Route doInBackground(Uri... params) {
        try {
            String xmlString = readFileContent(params[0]);
            route = loadRoute(xmlString);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return route;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Route route) {
        super.onPostExecute(route);
        Log.d("route", "" + route.getName());
    }


    private String readFileContent(Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        String xmlFromFile = FileUtils.readFileToString(streamToFile(inputStream));

        return xmlFromFile;

    }


    private File streamToFile(InputStream in) throws IOException {
        final File tempFile = File.createTempFile("temp", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

    private Route loadRoute(String xmlString) {

        JsonMapper jsonMapper = new JsonMapper();

        try {
            return jsonMapper.objectifyRoute(xmlString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}

package se.jolo.prototypenavigator.activities;

import android.content.Context;
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
import java.util.Random;

import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.utils.JsonMapper;

public final class Loader extends AsyncTask<Uri, Integer, Route> {

    private Context context;
    private Route route = null;

    public Loader(Context context) {
        this.context = context;
    }

    private String readFileContent(Uri uri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        String xmlFromFile = FileUtils.readFileToString(streamToFile(inputStream));

        saveLoadedFile(xmlFromFile);

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

    private boolean saveLoadedFile(String content) {

        try {
            FileOutputStream out = new FileOutputStream(
                    new File(context.getFilesDir(), "RouteXmlFile-" + new Random().nextInt(1000)));
            out.write(content.getBytes());
            out.close();

            loadSavedFiles();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public File[] loadSavedFiles() {
        return context.getFilesDir().listFiles();
    }

    public void getPreLoadedFileAsString(String fileName) {

        File[] files = loadSavedFiles();

        for (File f : files) {
            if (f.getName().equals(fileName)) {

            }
        }
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

    /*********************************************************************************************/
    /****                                    AsycTasks                                        ****/
    /*********************************************************************************************/
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
        Log.d("route", "in Loader " + route.getName());
    }
}

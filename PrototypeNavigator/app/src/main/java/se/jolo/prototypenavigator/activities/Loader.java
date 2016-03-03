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

    private String readFileContent(File file) throws IOException {
        return FileUtils.readFileToString(file);
    }

    private File streamToFile(InputStream in) throws IOException {

        final File tempFile = File.createTempFile("temp", ".tmp");
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        return tempFile;
    }

    private boolean checkForDuplicate(FileOutputStream out, String content) throws IOException {

        File tmpFile = File.createTempFile("tmp", ".tmp");
        tmpFile.deleteOnExit();

        out = new FileOutputStream(tmpFile);
        out.write(content.getBytes());
        out.close();

        for (File f : loadSavedFiles()) {
            if (FileUtils.contentEquals(tmpFile, f)) {

                return true;
            }
        }

        return false;
    }

    private boolean saveLoadedFile(String content) {

        FileOutputStream out = null;

        try {
            if (checkForDuplicate(out, content)) {
                Log.d("Loader", "file exists");

                return false;
            } else {

                out = new FileOutputStream(new File(
                        context.getFilesDir(), "RouteXmlFile-" + new Random().nextInt(1000)));
                out.write(content.getBytes());
                out.close();

                Log.d("Loader", "new file saved");

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public File[] loadSavedFiles() {
        return context.getFilesDir().listFiles();
    }

    public Route getPreLoadedRoute(String fileName) throws IOException {

        File[] files = loadSavedFiles();

        for (File f : files) {
            if (f.getName().equals(fileName)) {
                return loadRoute(readFileContent(f));
            }
        }

        return null;
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

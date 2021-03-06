package se.jolo.prototypenavigator.task;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.ArrayList;
import java.util.Random;

import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.utils.Cipher;
import se.jolo.prototypenavigator.utils.JsonMapper;
/* The Looader class loads a xml file returns a route object. */
public final class Loader extends AsyncTask<Uri, Integer, Route> {

    private Context context;
    private Route route = null;
    private String fileName;
    public Loader(Context context) {
        this.context = context;
    }

    /* Convert a files content to a string. Gets file with provided URI. */
    private String readFileContent(Uri uri) throws IOException {
        fileName = uri.getLastPathSegment().replace("primary:", "");
        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        byte[] decodedXml = FileUtils.readFileToByteArray(streamToFile(inputStream));
        String key = "apelsin";
        byte[] byteContent = Cipher.decrypt(decodedXml, key.getBytes());

        String content = new String(byteContent);

        saveLoadedFile(content);
        return content;
    }

    /* Convert a files content to a string. */
    private String readFileContent(File file) throws IOException {
        return FileUtils.readFileToString(file);
    }

    /* Converts InputStream to file. */
    private File streamToFile(InputStream in) throws IOException {

        final File tempFile = File.createTempFile("temp", ".tmp");
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        return tempFile;
    }

    /* Checks in Application directory for files with provided string content. */
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

    /* Saves String content as a file. */
    private boolean saveLoadedFile(String content) {

        FileOutputStream out = null;

        try {
            if (checkForDuplicate(out, content)) {
                Log.d("Loader", "file exists");

                return false;
            } else {

                out = new FileOutputStream(new File(
                        context.getFilesDir(), "Route-" + fileName));
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

    /* Returns a list of files from Application files directory. */
    public File[] loadSavedFiles() {

        File[] allFiles = context.getFilesDir().listFiles();
        ArrayList<File> routeFilesList = new ArrayList<>();

        for (int i = 0; i<allFiles.length; i++){
            File file = allFiles[i];
            String fileName = file.getName();
            if (fileName.startsWith("Route")){
                routeFilesList.add(file);
            }
            Log.d("fileName", file.getName());
        }

        File[] routeFiles = new File[routeFilesList.size()];

        return routeFiles = routeFilesList.toArray(routeFiles);
    }

    /* Searches with filename through saved route files. If match found return as Route object. */
    public Route getPreLoadedRoute(String fileName) throws IOException {

        File[] files = loadSavedFiles();

        for (File f : files) {
            if (f.getName().equals(fileName)) {
                return loadRoute(readFileContent(f));
            }
        }

        return null;
    }

    /* Takes a string and converts it to a Route object. */
    private Route loadRoute(String xmlString) {

        JsonMapper jsonMapper = new JsonMapper();

        try {
            Log.d("json", String.valueOf(jsonMapper.objectifyRoute(xmlString)).toString());
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

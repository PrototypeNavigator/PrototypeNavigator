package se.jolo.prototypenavigator.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Holstad on 01/03/16.
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private Exception exception;

    public ImageLoader(){}


    protected Bitmap doInBackground(String... urls) {
        try {
            URL url= new URL(urls[0]);
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
            return bitmap;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(Bitmap bitmap) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
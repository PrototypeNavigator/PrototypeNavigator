package se.jolo.prototypenavigator.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Holstad on 01/03/16.
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private Exception exception;

    public ImageLoader(){}


    protected Bitmap doInBackground(String... urls) {
        Bitmap bitmap;
        try {
            URL url= new URL(urls[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            String stringNumber = httpURLConnection.getHeaderField("content-length");
            int contentLength = Integer.parseInt(stringNumber);
            Log.d("content", stringNumber);
            if (contentLength>10000){
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());

            }else {

                bitmap=null;
            }

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
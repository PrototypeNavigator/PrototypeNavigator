package se.jolo.prototypenavigator.task;

import android.os.AsyncTask;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.jolo.prototypenavigator.model.Instruction;

/**
 * Created by Holstad on 10/03/16.
 */
public class OsrmJsonTask extends AsyncTask<URL,String,HashMap<String,List>> {
    private String jsonString;
    private String TAG = "PolylineDecoder";
    private String ROUTE_GEOMETRY = "route_geometry";
    private String INSTRUCTIONS = "route_instructions";
    private List<LatLng> polyline;
    private HashMap<String,List> lists;
    private ArrayList <Instruction> instructions;
    private  JSONObject jsonRoot;
    @Override
    protected HashMap<String,List> doInBackground(URL... params) {

         lists = new HashMap<>();

        try {
            URL url = params[0];
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            jsonString = getStringFromInputStream(httpURLConnection.getInputStream());
            jsonRoot = new JSONObject(jsonString);
            Log.d(TAG, jsonRoot.toString());

            //Making list with route geometry
            String locations = jsonRoot.getString(ROUTE_GEOMETRY);
            Log.d(TAG, locations.toString());

            polyline = decode(locations);

            /*for (LatLng latLng:polyline){
                Log.d(TAG,latLng.toString());
            }*/

            // Making list with instructions
            instructions = new ArrayList<>();
            JSONArray jsonInstructionArray = jsonRoot.getJSONArray(INSTRUCTIONS);

            for (int i=0;i<jsonInstructionArray.length();i++){
                JSONArray currentJsonArray = jsonInstructionArray.getJSONArray(i);
                if (i==(jsonInstructionArray.length()-1)){
                    break;
                }else {
                    Instruction instruction = new Instruction(currentJsonArray.getString(0),currentJsonArray.getString(1),
                            currentJsonArray.getInt(2),currentJsonArray.getInt(3),(float)currentJsonArray.getInt(4),currentJsonArray.getString(5),
                            currentJsonArray.getString(6),(float)currentJsonArray.getInt(7),currentJsonArray.getString(8));
                    instructions.add(instruction);
                }
            }

            /*for (Instruction i: instructions){
                Log.d(TAG,i.toString());
            }*/

            // Adding lists to hashmap
            lists.put(ROUTE_GEOMETRY,polyline);
            lists.put(INSTRUCTIONS, instructions);


            Log.d(TAG,jsonString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lists;
    }

    @Override
    protected void onPostExecute(HashMap polyline) {
        super.onPostExecute(polyline);
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    /**********************************************************************************************/
    /**********************             Polyline Decoder                      *********************/
    /**********************************************************************************************/

    private static List<LatLng> decode(String encodedPath) {
        int len = encodedPath.length();
        ArrayList path = new ArrayList();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while(index < len) {
            int result = 1;
            int shift = 0;
            
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while(b >= 31);

            lat += (result & 1) != 0?~(result >> 1):result >> 1;
            result = 1;
            shift = 0;

            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while(b >= 31);

            lng += (result & 1) != 0?~(result >> 1):result >> 1;
            path.add(new LatLng((double)lat * 0.1E-5D, (double)lng * 0.1E-5D));
        }

        return path;
    }

}

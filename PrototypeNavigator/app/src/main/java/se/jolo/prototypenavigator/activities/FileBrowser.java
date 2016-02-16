package se.jolo.prototypenavigator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import se.jolo.prototypenavigator.MainActivity;
import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.utils.RealPathUtil;

public class FileBrowser extends AppCompatActivity {

    private final int REQUEST_ID = 146;
    private String xmlPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        openFileBrowser();

    }

    private void openFileBrowser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID
                && resultCode == FileBrowser.RESULT_OK) {
            xmlPath = data.getData().getPath();
            Log.d("path", "Path= " + xmlPath);
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("xmlPath", xmlPath);


            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }


}

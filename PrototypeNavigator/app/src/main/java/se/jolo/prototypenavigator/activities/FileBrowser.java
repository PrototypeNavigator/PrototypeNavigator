package se.jolo.prototypenavigator.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import se.jolo.prototypenavigator.R;

/* The FileBrowser class opens the android filebrowser and lets the user select a xml-file. */
public class FileBrowser extends AppCompatActivity {

    private final int REQUEST_ID = 146;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        openFileBrowser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri currentUri = null;

        Intent intent = new Intent(this, Map.class);

        if (intent.hasExtra("uri")){
            intent.getExtras().clear();
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ID && data != null) {
            currentUri = data.getData();
            intent.putExtra("uri", currentUri);
            startActivity(intent);
        }
    }

    /*Opens a filebrowser that shows files of the selected filetype, text/xml. */
    private void openFileBrowser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/xml");
        startActivityForResult(intent, REQUEST_ID);
    }

}

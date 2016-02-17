package se.jolo.prototypenavigator.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import se.jolo.prototypenavigator.MainActivity;
import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.utils.RealPathUtil;

public class FileBrowser extends AppCompatActivity {

    private final int REQUEST_ID = 146;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        openFileBrowser();

    }

    private void openFileBrowser(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Uri currentUri = null;

        Intent intent = new Intent(this, MainActivity.class);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ID && data != null) {
            currentUri = data.getData();
            try {
                String content =
                        readFileContent(currentUri);
                intent.putExtra("xmlString", content);
                startActivity(intent);

            } catch (IOException e) {
                // Handle error here
            }

        }
    }

    private String readFileContent(Uri uri) throws IOException {

        InputStream inputStream = getContentResolver().openInputStream(uri);

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


}
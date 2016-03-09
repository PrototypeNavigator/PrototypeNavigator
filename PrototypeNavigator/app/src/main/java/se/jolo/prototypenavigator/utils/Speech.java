package se.jolo.prototypenavigator.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;

import se.jolo.prototypenavigator.R;

/**
 * Created by Holstad on 09/03/16.
 */
public class Speech {
    private SpeechSynthesis synthesis;
    private Activity activity;
    private String TAG = "Speech";
    public Speech (Activity activity){
        this.activity=activity;
    }

    public void say(String whatToSay) {
        try {
            synthesis = SpeechSynthesis.getInstance(activity);
            synthesis.setVoiceType("swswedishfemale");
            synthesis.setSpeechSynthesisEvent(new SpeechSynthesisEvent() {
                public void onPlaySuccessful() {
                    Log.i(TAG, "onPlaySuccessful");
                }

                public void onPlayStopped() {
                    Log.i(TAG, "onPlayStopped");
                }

                public void onPlayFailed(Exception e) {
                    Log.e(TAG, "onPlayFailed");

                }

                public void onPlayStart() {
                    Log.i(TAG, "onPlayStart");
                }

                @Override
                public void onPlayCanceled() {
                    Log.i(TAG, "onPlayCanceled");
                }


            });
            synthesis.setStreamType(AudioManager.STREAM_MUSIC);



        } catch (InvalidApiKeyException e) {
            Log.e(TAG, "Invalid API key\n" + e.getStackTrace());
            Toast.makeText(activity.getApplicationContext(), "ERROR: Invalid API key", Toast.LENGTH_LONG).show();
        }
        String ttsText = whatToSay;
        try {
            synthesis.speak(ttsText);
        } catch (BusyException e) {
            e.printStackTrace();
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }
}

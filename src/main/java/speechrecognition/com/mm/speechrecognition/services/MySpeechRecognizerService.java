package speechrecognition.com.mm.speechrecognition.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

import speechrecognition.com.mm.speechrecognition.activities.Utility;

import static speechrecognition.com.mm.speechrecognition.activities.landingActivity.MyPREFERENCES;

/**
 * Created by shahr on 19/12/17.
 */

public class MySpeechRecognizerService extends Service implements RecognitionListener {

    private SpeechRecognizer speechRecognizer;
    SQLiteDatabase db;
    SharedPreferences sharedpreferences;
    public MySpeechRecognizerService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createSpeechObject();
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS command(audiopath text,voice text);");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private void createSpeechObject()
    {
        if(speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speechRecognizer.setRecognitionListener(this);
        promptSpeechInput();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float v) {
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
    }

    @Override
    public void onEndOfSpeech() {
    }


    @Override
    public void onError(int i) {
    }

    @Override
    public void onResults(Bundle results) {
      String command=null;
      String AudioPath=null;
    command=  sharedpreferences.getString("command",null);
    AudioPath=  sharedpreferences.getString("audio",null);
     Uri   ring = Uri.parse(sharedpreferences.getString("uri", "defaultString"));

        Cursor c=db.rawQuery("SELECT * FROM command", null);
        if(c.moveToFirst())
        {
            command=  c.getString(0);
           AudioPath= c.getString(1);
              // ring = Uri.parse(         c.getString(2));
//            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
        }
        Toast.makeText(getApplicationContext(),command,Toast.LENGTH_LONG).show();
        ArrayList<String> res = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
      //  Toast.makeText(getApplicationContext(),res.toString(),Toast.LENGTH_LONG).show();
        if (res != null && res.get(0).toLowerCase().contains(command)) {
        // String filepath= Environment.getExternalStorageDirectory()+"/Download/1.mp3";
           // Uri uri=Uri.parse("content://media/internal/audio/media/35");

         //   Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
           // Uri notification = Uri.parse(AudioPath);


            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ring);
           String name= r.getTitle(MySpeechRecognizerService.this);
            if(r.getTitle(MySpeechRecognizerService.this).equalsIgnoreCase(AudioPath)) {

                r.play();
            }
        }

        createSpeechObject();
    }

    @Override
    public void onPartialResults(Bundle bundle) {
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
    }

    void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplication().getPackageName());


        speechRecognizer.startListening(intent);
    }
}

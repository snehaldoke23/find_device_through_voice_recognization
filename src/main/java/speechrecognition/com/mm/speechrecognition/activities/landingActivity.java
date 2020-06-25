package speechrecognition.com.mm.speechrecognition.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import speechrecognition.com.mm.speechrecognition.R;
import speechrecognition.com.mm.speechrecognition.services.MySpeechRecognizerService;

/**
 * Created by unity2 on 12/19/2017.
 */

public class landingActivity extends AppCompatActivity {

    private  final String TAG = landingActivity.this.getClass().getName();
    ImageView _audio, _music;
    TextView _command, _audioSong;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String mp3path = "mp3Path";
    public static final String command = "command";
    SharedPreferences sharedpreferences;
    Button btn_submit;
    String getMp3path;
    Uri ringUri;
    String getCommand;
    SharedPreferences.Editor editor;
    final int RQS_RINGTONEPICKER = 1;
    private Context context;
  //  Button btn_startservice;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final String LOG_TAG = landingActivity.class.getSimpleName();
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("DELETE FROM command;");

        db.execSQL("CREATE TABLE IF NOT EXISTS command(audiopath text,voice text);");
        setContentView(R.layout.activity_landing);
        _audio = (ImageView) findViewById(R.id.audioCommand);
        _music = (ImageView) findViewById(R.id.selectMusic);
        _command = (TextView) findViewById(R.id.commnd);
        _audioSong = (TextView) findViewById(R.id.audioSong);
        btn_submit = (Button) findViewById(R.id.submit);
        /*btn_startservice = (Button) findViewById(R.id.btn_startservice);
        btn_startservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(landingActivity.this, new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_RECORD_AUDIO);
                    return;
                }
               // startService(new Intent(landingActivity.this, VoiceService.class));


               // Intent intent = new Intent(landingActivity.this, BootReceiver.class);
//                intent.setAction("voice");
//                PendingIntent pintent = PendingIntent.getService(landingActivity.this, 0, intent, 0);
//                AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 30*1000, pintent);

            }
        });*/
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("command",_command.getText().toString());
                editor.putString("audio",_audioSong.getText().toString());
                editor.putString("uri",ringUri.toString());

                editor.commit();
               // Utility.commands=_command.getText().toString();
                db.execSQL("INSERT INTO command VALUES('"+_command.getText().toString()+"','"+
                        _audioSong.getText().toString()+"');");
                Toast.makeText(getApplicationContext(), " Submitted Successfully", Toast.LENGTH_LONG).show();
                Intent pushIntent = new Intent(landingActivity.this, MySpeechRecognizerService.class);
                startService(pushIntent);

            }
        });
        _audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promptSpeechInput();
            }
        });

        isReadStoragePermissionGranted();
//        isWriteStoragePermissionGranted();

        _music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent_upload = new Intent();
                intent_upload.setType("audio*//*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload, 1);*/
               /* Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                intent1.setType("audio*//*");
                startActivityForResult(
                        Intent.createChooser(intent1, "Choose Sound File"), 6);*/

               /* Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mpeg");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.Ringtone)), 1);
                MediaPlayer player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);*/

                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent, RQS_RINGTONEPICKER);



            }
        });

        this.context = this;

//        Intent alarm = new Intent(this.context, AlarmReceiver.class);
//        alarm.putExtra("log", "xxxxx");
//        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarm, 0);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 10, pendingIntent);
//

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              //  startService(new Intent(landingActivity.this, VoiceService.class));
            } else {
                finish();
            }
        }
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission

                }else{

                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission

                }else{

                }
                break;
        }




    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    getCommand = result.get(0);
                    _command.setText(result.get(0));
                  //  Utility.commands=result.get(0);
                }
                break;
            }


        }

        if(requestCode == RQS_RINGTONEPICKER && resultCode == RESULT_OK){
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringUri=uri;
            Ringtone  ringTone = RingtoneManager.getRingtone(getApplicationContext(), uri);
            _audioSong.setText(ringTone.getTitle(landingActivity.this));

           // String path=    uri.getPath();
//            Toast.makeText(landingActivity.this,
//                    ringTone.getTitle(landingActivity.this),
//                    Toast.LENGTH_LONG).show();
            //ringTone.play();
        }
        
        
        
        

        /*if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                //the selected audio.
                Uri uri = data.getData();
                getMp3path = uri.getPath();
                _audioSong.setText(uri.getPath());
            }
        }*/

        /*if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                Uri audioFileUri = data.getData();
                String path = audioFileUri.getPath();
                System.out.println(path);
                String path = audioCursor.getString(audioCursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA));
                // Now you can use that Uri to get the file path, or upload it, ...
            }
        }*/
      /*  if (resultCode == RESULT_OK && requestCode == 1) {
            Uri i = data.getData();  // getData
            String s = i.getPath(); // getPath
            getMp3path=s;

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Utility.KeywordName, getCommand);
            editor.putString(Utility.SONGURL, getMp3path);
            editor.commit();
            //Uri path=   Environment.getExternalStorageDirectory().getPath()+ "/calm.mp3";
            File k = new File(s);  // set File from path
            if (s != null) {      // file.exists

                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
                values.put(MediaStore.MediaColumns.TITLE, "ring");
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                values.put(MediaStore.MediaColumns.SIZE, k.length());
                values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                values.put(MediaStore.Audio.Media.IS_ALARM, true);
                values.put(MediaStore.Audio.Media.IS_MUSIC, false);
               Uri iUri= MediaStore.Audio.Media.INTERNAL_CONTENT_URI;*/

               /* List<String> songlist = new ArrayList<>();
                ContentResolver contentResolver = getContentResolver();
                Uri uri1 = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

                Cursor cursor = contentResolver.query(uri1, null, null, null, null);
                if (cursor == null) {
                    // query failed, handle error.
                } else if (!cursor.moveToFirst()) {
                    // no media on the device
                } else {
                    do {

                   //     String fullPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        // ...process entry...
                        Log.e("Full Path : ", fullPath);


                  //      songlist.add(fullPath);
                    } while (cursor.moveToNext());
                }
*/

               /* byte[] buffer = null;
                try {
                InputStream fIn = new FileInputStream(i.getEncodedPath());
                int size=0;


                    size = fIn.available();
                    buffer = new byte[size];
                    fIn.read(buffer);
                    fIn.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                  //  return false;
                }
                String path=   Environment.getExternalStorageDirectory().getPath();
                //String path = "/sdcard/yourapp/temp/";
                String filename = "filename"+".mp3";

                boolean exists = (new File(path)).exists();
                if (!exists){new File(path).mkdirs();}

                FileOutputStream save;
                try {
                    save = new FileOutputStream(path+filename);
                    save.write(buffer);
                    save.flush();
                    save.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                   // return false;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                  //  return false;
                }
*/









/*

                Uri uri = MediaStore.Audio.Media.getContentUriForPath(k
                        .getAbsolutePath());
                getContentResolver().delete(
                        uri,
                        MediaStore.MediaColumns.DATA + "=\""
                                + k.getAbsolutePath() + "\"", null);
                Uri newUri = getContentResolver().insert(uri, values);

                Toast.makeText(getApplicationContext(),k.getAbsolutePath(),Toast.LENGTH_LONG).show();
                _audioSong.setText(k.getAbsolutePath());
                try {
                    RingtoneManager.setActualDefaultRingtoneUri(
                            landingActivity.this, RingtoneManager.TYPE_RINGTONE,
                            newUri);
                } catch (Throwable t) {

                }
            }
       }
*/


    }



    /*@Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }




}

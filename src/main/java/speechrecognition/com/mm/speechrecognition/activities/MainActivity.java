package speechrecognition.com.mm.speechrecognition.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import speechrecognition.com.mm.speechrecognition.services.MySpeechRecognizerService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent pushIntent = new Intent(MainActivity.this, MySpeechRecognizerService.class);
        startService(pushIntent);
    }
}

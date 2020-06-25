package speechrecognition.com.mm.speechrecognition.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by shahr on 19/12/17.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, MySpeechRecognizerService.class);
            context.startService(pushIntent);
        }
    }
}

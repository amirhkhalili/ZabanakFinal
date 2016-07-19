package ir.armaani.hv.zabanak;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationPayload;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import ir.armaani.hv.zabanak.activities.MessageActivity;

/**
 * Created by Siamak on 19/07/2016.
 */
public class NotificationHandler extends NotificationExtenderService {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    protected boolean onNotificationProcessing(OSNotificationPayload notification) {
        JSONObject data = notification.additionalData;

        try {
            Integer notificationType;
            JSONObject notificationPayload;
            notificationType = data.getInt("notificationType");
            notificationPayload = data.getJSONObject("notificationPayload");

            switch (notificationType) {
                case 1:
                    String messageTitle = notificationPayload.getString("messageTitle");
                    String messageContent = notificationPayload.getString("messageContent");
                    showNotification(messageTitle , messageContent , MessageActivity.class , null);
                    break;
                case 2:

                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return true;
    }

    protected void showNotification(String title , String content , Class<?> target , JSONObject data) {
        int mNotificationId =  getId();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        builder.setContentText(content);
        builder.setContentTitle(title);
        builder.setSmallIcon(R.mipmap.ic_zabanak);
        builder.setAutoCancel(true);

        Intent resultIntent = new Intent(this,target);
        resultIntent.putExtra("title" , title);
        resultIntent.putExtra("content" , content);
        if (data != null)
            resultIntent.putExtra("data" , data.toString());

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, builder.build());
    }

    private Integer getId() {
        return atomicInteger.incrementAndGet();
    }
}

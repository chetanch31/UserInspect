package visual.camp.sample.app.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ScreenCaptureForegroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String channelId = intent.getStringExtra("channel_id");
        if (channelId == null) {
            channelId = "default_channel_id";
        }

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(1, new NotificationCompat.Builder(this, channelId).build());
        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

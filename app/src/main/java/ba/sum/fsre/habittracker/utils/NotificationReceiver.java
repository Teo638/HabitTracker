package ba.sum.fsre.habittracker.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import ba.sum.fsre.habittracker.MainActivity;
import ba.sum.fsre.habittracker.R;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "habit_channel";
    private static final String EXTRA_TYPE = "notification_type";

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String title = "HabitTracker";
        String message = "Ne zaboravite na svoje ciljeve!";


        if ("MORNING".equals(type)) {
            title = "Dobro jutro!";
            message = "Ne zaboravite na svoje ciljeve!";
        } else if ("EVENING".equals(type)) {
            title = "Vrijeme za pregled";
            message = "Jeste li ispunili sve ciljeve danas?";
        }

        showNotification(context, title, message);
    }

    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "HabitTracker podsjetnici",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }


        Intent tapIntent = new Intent(context, MainActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, tapIntent, PendingIntent.FLAG_IMMUTABLE
        );


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
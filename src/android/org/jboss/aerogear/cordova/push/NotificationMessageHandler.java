/*
 * JBoss, Home of Professional Open Source.
 * Copyright Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.cordova.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.jboss.aerogear.android.store.DataManager;
import org.jboss.aerogear.android.store.sql.SQLStore;
import org.jboss.aerogear.android.store.sql.SQLStoreConfiguration;
import org.jboss.aerogear.android.unifiedpush.MessageHandler;

import java.util.Collection;

public class NotificationMessageHandler implements MessageHandler {

    public static int NOTIFICATION_ID = 237;

    private SQLStore<Message> store;

    @Override
    public void onMessage(Context context, Bundle bundle) {
        if (bundle != null) {

            openSQLStore(context);

            String message = bundle.getString("alert");

            if (!PushPlugin.isInForeground() && message != null && !message.isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createChannel(context);
                }
                createNotification(context, bundle);
            }
            PushPlugin.sendMessage(bundle);
        }
    }

    private void openSQLStore(Context context) {
        if (store == null) {
            store = (SQLStore<Message>) DataManager.config("messageStore", SQLStoreConfiguration.class)
                    .withContext(context)
                    .store(Message.class);
            store.openSync();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(Context context) {

        String appName = getAppName(context);

        NotificationChannel channel = new NotificationChannel(appName, appName,
                NotificationManager.IMPORTANCE_LOW);
        channel.setDescription(appName);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }

    public void createNotification(Context context, Bundle extras) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String appName = getAppName(context);

        Message message = new Message(extras);
        store.save(message);

        Intent notificationIntent = new Intent(context, PushHandlerActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("message", extras);

        // OLD
        // Required Intent.FILL_IN_ACTION together with AndroidLaunchMode="singleTop" (instead of PendingIntent.FLAG_UPDATE_CURRENT).
        // PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, Intent.FILL_IN_ACTION);

        // NEW
        // Set PendingIntent.FLAG_UPDATE_CURRENT to get message extras in PushHandlerActivity when the user tap on notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final String title = extras.getString("title");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, appName)
                        .setDefaults(Notification.DEFAULT_ALL)
                        //.setSmallIcon(context.getApplicationInfo().icon)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(title != null ? title : appName)
                        .setTicker(appName)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        Collection<Message> messageList = store.readAll();
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        for (Message item : messageList) {
            style.addLine(item.getAlert());
        }
        builder.setStyle(style);
        builder.setSmallIcon(getNotificationIcon(builder, context));
        builder.setContentText(extras.getString("alert"));

        String msgcnt = extras.getString("msgcnt");
        if (msgcnt != null) {
            builder.setNumber(Integer.parseInt(msgcnt));
        } else if (!messageList.isEmpty()) {
            builder.setNumber(messageList.size());
        }

        // Remove all old notification since it will group the new one with olds
        manager.cancelAll();

        NOTIFICATION_ID = (int) System.currentTimeMillis();
        manager.notify(appName, NOTIFICATION_ID, builder.build());
    }

    private static String getAppName(Context context) {
        CharSequence appName = context.getPackageManager()
                .getApplicationLabel(context.getApplicationInfo());
        return (String) appName;
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x2d6588;
            notificationBuilder.setColor(color);

            int icon = context.getResources().getIdentifier("icon_white", "drawable", context.getPackageName());

            //Condition to ensure the icon exists
            if(icon == 0) icon = context.getApplicationInfo().icon;

            return icon;
        } else {
            return context.getApplicationInfo().icon;
        }
    }
}

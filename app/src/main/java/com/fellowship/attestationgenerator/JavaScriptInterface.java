package com.fellowship.attestationgenerator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
public class JavaScriptInterface {

    private Context context;
        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @JavascriptInterface
        public void getBase64FromBlobData(String base64Data) throws IOException {
            convertBase64StringToPdfAndStoreIt(base64Data);
        }
        public static String getBase64StringFromBlobUrl(String blobUrl) {
            if(blobUrl.startsWith("blob")){
                return "javascript: var xhr = new XMLHttpRequest();" +
                        "xhr.open('GET', '"+ blobUrl +"', true);" +
                        "xhr.setRequestHeader('Content-type','application/pdf');" +
                        "xhr.responseType = 'blob';" +
                        "xhr.onload = function(e) {" +
                        "    if (this.status == 200) {" +
                        "        var blobPdf = this.response;" +
                        "        var reader = new FileReader();" +
                        "        reader.readAsDataURL(blobPdf);" +
                        "        reader.onloadend = function() {" +
                        "            base64data = reader.result;" +
                        "            Android.getBase64FromBlobData(base64data);" +
                        "        }" +
                        "    }" +
                        "};" +
                        "xhr.send();";
            }
            return "javascript: console.log('It is not a Blob URL');";
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        private void convertBase64StringToPdfAndStoreIt(String base64PDf) throws IOException {
            Log.e("BASE 64", base64PDf);
            final int notificationId = 1;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime now = LocalDateTime.now();
            String currentDateTime = dtf.format(now);
            final File dwldsPath = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/attestation_derogatoire_"+currentDateTime+".pdf");
            byte[] pdfAsBytes = Base64.decode(base64PDf.replaceFirst("^data:application/pdf;base64,", ""), 0);
            FileOutputStream os;
            os = new FileOutputStream(dwldsPath, false);
            os.write(pdfAsBytes);
            os.flush();

            if (dwldsPath.exists()) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                Uri apkURI = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName() + ".provider", dwldsPath);
                intent.setDataAndType(apkURI, MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                PendingIntent pendingIntent = PendingIntent.getActivity(context,1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                String CHANNEL_ID = "NOTIF_ATT_CHANNEL";
                final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel= new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_LOW);
                    Notification notification = new Notification.Builder(context,CHANNEL_ID)
                            .setContentText("Un fichier est en cours de téléchargement")
                            .setContentTitle("Fichier téléchargé")
                            .setContentIntent(pendingIntent)
                            .setChannelId(CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.sym_action_chat)
                            .build();
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(notificationChannel);
                        notificationManager.notify(notificationId, notification);
                    }

                } else {
                    NotificationCompat.Builder b = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(android.R.drawable.sym_action_chat)
                            //.setContentIntent(pendingIntent)
                            .setContentTitle("Téléchargement de l'attestation")
                            .setContentText("Téléchargement de l'attestation dérogatoire de déplacement");

                    if (notificationManager != null) {
                        notificationManager.notify(notificationId, b.build());
                        Handler h = new Handler();
                        long delayInMilliseconds = 1000;
                        h.postDelayed(new Runnable() {
                            public void run() {
                                notificationManager.cancel(notificationId);
                            }
                        }, delayInMilliseconds);
                    }
                }
            }
            Toast.makeText(context, "FICHIER PDF TELECHARGE!", Toast.LENGTH_SHORT).show();
        }
    }
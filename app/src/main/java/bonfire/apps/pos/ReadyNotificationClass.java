package bonfire.apps.pos;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.modules.DataModule;

public class ReadyNotificationClass extends Service {
    String time, dater;
    public static String dates = " ";
    RequestQueue rq;
    int counter = 0;
    public static boolean newItems = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                while (true) {

                    Log.e("Count before Thread", String.valueOf(counter));
                    try {
                        Thread.sleep(2000);
                        long date = System.currentTimeMillis();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+05:45"));
                        String dateTime = sdf.format(new Date());
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat da = new SimpleDateFormat("yyyy/MM/dd");
                        da.setTimeZone(TimeZone.getTimeZone("GMT+05:45"));
                        time = da.format(new Date());
                        // todays.setText(todayDate);

                        Date c = Calendar.getInstance().getTime();


                        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        // SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        //df.format(c);
                        dater = df.format(c);
                        Log.e("Counter", String.valueOf(counter));
                        counter++;

                        try {
                            notifier();
                            getDataFromSqlite();

                        } catch (Exception e) {
                            Log.e("Thread Error 1",e.toString());
                        }
                    } catch (Exception e) {
                        Log.e("Thread Error 2",e.toString());
                    }
                }
            }
        }).start();

        super.onCreate();
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void getDataFromSqlite() {
        try {
            SQLiteAdapter sa = new SQLiteAdapter(ReadyNotificationClass.this);
            List<DataModule> dm = new ArrayList<DataModule>();
            sa.getCount(new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));


            dm = sa.getNotificationData();
            for (int i = 0; i < dm.size(); i++) {
                String tableno = dm.get(i).getTableno();
                String waiter = dm.get(i).getWaitername();
                String kotdetailid = dm.get(i).getKotdetailid();
                String itemname = dm.get(i).getItemName();
                String kot_date = dm.get(i).getKot_date();
                createNotification(itemname, tableno, waiter, i, ReadyNotificationClass.this);
                sa.update(kotdetailid);
                Log.e("KOT_DATE", kot_date);
                //delete
              //  Log.e("KOt date")

            }

        } catch (Exception e) {
            Log.e("NOTIFICATION ERROR", e.toString());
        }
    }

    public void notifier() {
        ServerManager sm = new ServerManager(ReadyNotificationClass.this);
        StringRequest st =
                new StringRequest(Request.Method.POST,
                        sm.getServer() + "/GetNotification",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(String response) {
                                //    Log.e("response", response);

                                try {
                                    JSONObject object = new JSONObject(response);

                                    JSONArray jAry = object.getJSONArray("Notification");

                                    for (int i = 0; i < jAry.length(); i++) {
                                        JSONObject jo = jAry.getJSONObject(i);
                                        String itemname = jo.getString("itemsName");
                                        String tableno = jo.getString("TableNo");
                                        String waiter = jo.getString("WaiterName");
                                        String kotid = jo.getString("ID_kot");
                                        String kotDetailId = jo.getString("KotDetailId");
                                        String kot_date = jo.getString("Kot_Date");

                                        final String id_kot = jo.getString("KotDetailId");
                                        //   String notify = jo.getString("Notify");
                                        try {
                                            SQLiteAdapter sa = new SQLiteAdapter(ReadyNotificationClass.this);
                                            String idea = sa.check_KotD_ID(kotDetailId);
                                            if (idea.matches("nope")) {
                                                sa.insert(kotid, kotDetailId, itemname, tableno, waiter, kot_date);
                                                newItems = true;

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e("Cannot insert", e.toString());
                                        }


                                      /*      if (notify.matches("0")) {
                                                createNotification(itemname, tableno, waiter, i, ReadyNotificationClass.this);

                                            }
                                            if (notify.matches("0")) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        while (true) {
                                                            try {
                                                                Thread.sleep(1000);

                                                                stopNotification(id_kot);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }).start();

                                            }
*/
                                        Intent intent = new Intent("order.ready");
                                        sendBroadcast(intent);


                                    }
                                } catch (Exception e) {
                                    Log.e("JSON ERROR", e.toString());
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "No Data");

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("Date", dater);
                        //map.put("Time", time);
                        return map;
                    }
                };
/*
// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 524 * 524); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        rq = new RequestQueue(cache, network);

// Start the queue
        rq.start();*/
        try {
           rq = getRequestQueue();
            // rq = Volley.newRequestQueue(ReadyNotificationClass.this);
            rq.add(st);
        } catch (Exception e) {
            Log.e("Request Notification 1" , e.toString());
        }
    }
    public RequestQueue getRequestQueue() {
        if (rq == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
           return rq = Volley.newRequestQueue(ReadyNotificationClass.this.getApplicationContext());
        }
        return rq;
    }

    /*
     * for stopping Notification
     * */

    public void stopNotification(final String id_kotdetail) {
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                new ServerManager(ReadyNotificationClass.this).getServer() + "/UpdateNotification",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            Log.e("Success", object.getString("Message"));


                        } catch (Exception e) {
                            Log.e("faliure", e.toString());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> ider = new HashMap<>();
                ider.put("KDid", id_kotdetail);


                return ider;
            }
        };
        try {
            RequestQueue rq = Volley.newRequestQueue(ReadyNotificationClass.this);

            rq.add(sr);

        } catch (Exception e) {
            Log.e("notification request", e.toString());
        }

    }



    /*
    Notification Function
     */

    private NotificationManager manager;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createNotification(String name, String tableno, String waiter, int notifyID, Context context) {
        // int NOTIFY_ID = notifyID;
        String id = String.valueOf(notifyID);
        String title = "Order Ready " + notifyID;
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        //  NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        // NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            assert manager != null;
            NotificationChannel mChannel = manager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                manager.createNotificationChannel(mChannel);
            }

            String msger = "Tableno = " + tableno + " Waiter = " + waiter;
            String ready = name + " Ready ";
            //inboxStyle.setBigContentTitle(ready);
            // inboxStyle.addLine(ready);
            //inboxStyle.addLine(msger);
            //  bigTextStyle.bigText(ready + msger );
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, NotificationData.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle(ready)
                    //.setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setSmallIcon(R.mipmap.posnepal)
                    .setContentText(msger)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(ready);


        } else {
            String msger = "Tableno = " + tableno + " Waiter = " + waiter;
            String ready = name + " Ready ";
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, NotificationData.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle(ready)
                    //  .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setSmallIcon(R.mipmap.posnepal)
                    .setContentText(msger)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(ready)
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notificaiton = builder.build();
        manager.notify(notifyID, notificaiton);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onDestroy();
    }
}

package bonfire.apps.pos.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.R;
import bonfire.apps.pos.adapter.TableAdapter;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.modules.DataModule;

public class TableActivity extends AppCompatActivity {
    List<DataModule> dataModules = new ArrayList<>();
    RecyclerView tableView;
    String table = "/GetTable";
    List<String> departList = new ArrayList<>();
    List<String> userList = new ArrayList<>();
    String username, password;
    ServerManager sm;
    ProgressDialog pd;
    String deviceID;
    EditText searchTable;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);
        tableView = findViewById(R.id.tableView);
        searchTable = findViewById(R.id.searchTables);
        searchTable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Toast.makeText(TableActivity.this, "Before Text Changed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String searchText = s.toString();
//                if(searchText.isEmpty()){
//                    dataModules.clear();
//                    getTable();
//                }
//                else{
//                    dataModules.clear();
//                    getTable(searchText);
//
//                }
//                Toast.makeText(TableActivity.this, "onText Changed " + s , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable s) {
               // String searchText = s.toString();
                if((s.toString()).isEmpty()){
                    dataModules.clear();
                    getTable();
                }
                else{
                    dataModules.clear();
                    getTable(s.toString());

                }




            }
        });


        tableView.hasFixedSize();
        int orientation = getResources().getConfiguration().orientation;

        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size. x;
        int height = size. y;
        new SQLiteAdapter(TableActivity.this).getCount();
        if (width > 750) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                tableView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4 ));

            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                tableView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 6));
        } else {
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                tableView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3 ));

            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                tableView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
        }

        sm = new ServerManager(getApplicationContext());
        pd= new ProgressDialog(TableActivity.this);
        pd.setTitle("Loading..!!!");
        pd.setMessage("Please Wait..");
        dataModules.clear();

        pd.show();
         getTable();
      /*  try {
            startService(new Intent(TableActivity.this, ReadyNotificationClass.class));
            BroadcastReceiver noticeReciver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals("order.ready")) {
                        Toast.makeText(context, "YOUR Order IS Ready", Toast.LENGTH_SHORT).show();
                    }
                }
            };
        }
        catch (Exception e){
            Log.e("serviceError", "cannot stop");
            e.printStackTrace();
            stopService(new Intent(TableActivity.this,ReadyNotificationClass.class));
            Log.e("Stopped" , "Notification Stopped");
        }*/


    }

    public void getTable() {


        StringRequest sr = new StringRequest(Request.Method.POST,
                new ServerManager(getApplicationContext()).getServer() + table,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        //    Log.e("table", response);
                        try {
                            JSONObject o = new JSONObject(response);
                            JSONArray a = o.getJSONArray("TableDetails");
                            for (int i = 0; i < a.length(); i++) {
                                JSONObject oo = a.getJSONObject(i);
                                DataModule m = new DataModule();
                                m.setTableid(oo.getString("table_id"));
                                m.setTablename(oo.getString("tablename"));
                                m.setIs_available(oo.getString("is_available"));
                                m.setTableno(oo.getString("tableno"));
                                m.setFirst_order(oo.getString("first_order_time"));
                                m.setLast_order(oo.getString("last_order_time"));
                                m.setTable_cost(oo.getString("table_cost"));
                                dataModules.add(m);
                            }
                            TableAdapter adapter = new TableAdapter(TableActivity.this, dataModules);
                            tableView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tableError", error.toString());
                try {
                    //   pd.dismiss();
                    getTable();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        try {
            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            rq.add(sr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getTable(final String s) {
        dataModules.clear();
        final List<DataModule> sTable = new ArrayList<DataModule>();
        sTable.clear();
        //Toast.makeText(TableActivity.this, "ss " + s, Toast.LENGTH_SHORT).show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                new ServerManager(getApplicationContext()).getServer() + table,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        //    Log.e("table", response);
                        try {
                            JSONObject o = new JSONObject(response);
                            JSONArray a = o.getJSONArray("TableDetails");
                            for (int i = 0; i < a.length(); i++) {
                                JSONObject oo = a.getJSONObject(i);
                                DataModule m = new DataModule();
                                m.setTableid(oo.getString("table_id"));
                                m.setTablename(oo.getString("tablename"));
                                String s1 = oo.getString("tablename");
                                m.setIs_available(oo.getString("is_available"));
                                m.setTableno(oo.getString("tableno"));
                                m.setFirst_order(oo.getString("first_order_time"));
                                m.setLast_order(oo.getString("last_order_time"));
                                m.setTable_cost(oo.getString("table_cost"));
                                if(s1.toUpperCase().contains(s.toUpperCase())){
                                 //   Toast.makeText(TableActivity.this, s1+ " s " + s, Toast.LENGTH_SHORT).show();
                                    sTable.add(m);
                                }

                            }
                            TableAdapter adapter = new TableAdapter(TableActivity.this, sTable);
                            tableView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tableError", error.toString());
                try {
                    //   pd.dismiss();
                    getTable(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        try {
            RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
            rq.add(sr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_user,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutUser){
            ServerManager sm = new ServerManager(TableActivity.this);
            sm.logOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     //  stopService(new Intent(TableActivity.this,ReadyNotificationClass.class));
       // Log.e("Stopped" , "Notification Stopped");

    }

}

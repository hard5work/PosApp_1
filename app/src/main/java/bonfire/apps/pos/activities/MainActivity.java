package bonfire.apps.pos.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.R;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.fragments.DrinksFragment;
import bonfire.apps.pos.fragments.FoodFragment;
import bonfire.apps.pos.fragments.ShishaFragment;
import bonfire.apps.pos.modules.DataModule;
import bonfire.apps.pos.ui.main.SectionStateAdapter;
import bonfire.apps.pos.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    int orientations;
    ImageButton serverCheck;
    public String url;
    EditText serverIP;
    TextView title;
    ServerManager mngr;
    Button btnSubmit;
    ImageButton close;
    List<DataModule> dataModules = new ArrayList<>();

    String table = "/GetTable";
    FloatingActionButton fab,ot;
    public static SearchView searchView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final SectionStateAdapter stateAdapter = new SectionStateAdapter(getSupportFragmentManager(), this);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        fab = findViewById(R.id.fab);
        ot = findViewById(R.id.ot);
        title = findViewById(R.id.title);

       // viewPager.setAdapter(sectionsPagerAdapter);
        stateAdapter.addFragment(new FoodFragment());
        stateAdapter.addFragment(new DrinksFragment());
        stateAdapter.addFragment(new ShishaFragment());
        viewPager.setAdapter(stateAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        mngr = new ServerManager(MainActivity.this);
        searchView = findViewById(R.id.search_category);
        tabs.setupWithViewPager(viewPager);


        ServerManager sm = new ServerManager(MainActivity.this);
        String tb = sm.getTableno();
        String t = "Table " + tb;
        if (!sm.isLoggedIn()){
            Intent intent = new Intent(MainActivity.this, LoginAndServerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        title.setText(t);
     //   viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
       /* tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    sectionsPagerAdapter.refreshData(tab.getPosition());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
*/


        serverCheck = findViewById(R.id.serverCheck);
        serverCheck.setVisibility(View.GONE);

        if (!isInternetOn())
        {
            AlertDialog.Builder  builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("No Internet Connection");
            builder.setTitle("Network Error!!");
            builder.setIcon(android.R.drawable.alert_light_frame);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(true);

        }

        if (mngr.isLoggedIn()){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mngr.logOut();
                    fab.setVisibility(View.GONE);


                }
            });
        }




        serverCheck.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                @SuppressLint("InflateParams") final View server = LayoutInflater.from(getApplicationContext()).inflate(R.layout.change_server_pop, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(server);
              final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);
                serverIP = server.findViewById(R.id.serverIP);
                btnSubmit = server.findViewById(R.id.submitServer);
                close = server.findViewById(R.id.closeServer);
                ServerManager serverM = new ServerManager(getApplicationContext());
                String u =  serverM.getServer();
                if (u == null || u.isEmpty()) {
                    serverIP.setText("http://192.168.10.125/pos/TestService.asmx");
                }
                else
                    serverIP.setText(u);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        url = serverIP.getText().toString().trim();
                        ServerManager serverM = new ServerManager(getApplicationContext());

                        serverM.setServer(url);
                        SQLiteAdapter adapter = new SQLiteAdapter(MainActivity.this);
                        adapter.deleteDatabase();
                        Toast.makeText(MainActivity.this, "ServerChanged", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });


            }
        });



    }
    public boolean isInternetOn(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null)
            networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public void getTable() {
        StringRequest sr = new StringRequest(Request.Method.POST,
                new ServerManager(getApplicationContext()).getServer() + table,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                          //  TableAdapter adapter = new TableAdapter(TableActivity.this, dataModules);
                           // tableView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tableError", error.toString());

            }
        }) {

        };
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(sr);
    }


}
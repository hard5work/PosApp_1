package bonfire.apps.pos.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bonfire.apps.pos.R;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.database.ServerManager;


public class LoginAndServerActivity extends AppCompatActivity {

    TextView headerText;
    List<String> userList = new ArrayList<>();
    EditText pasword, serverIP;
    Spinner users;
   // AutoCompleteTextView users;
    String pass, user, url;
    Button login, btnSubmit;
    int pasleng;
    ServerManager sm;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_login_new);
        users = findViewById(R.id.spinUser);
        pasword = findViewById(R.id.userPass);
        login = findViewById(R.id.login);
        btnSubmit = findViewById(R.id.submitServer);
        serverIP = findViewById(R.id.serverIP);
        headerText = findViewById(R.id.headerText);


        sm = new ServerManager(getApplicationContext());

        String u = sm.getServer();
        if (u == null || u.isEmpty()) {
            serverIP.setText("http://192.168.10.125/pos/TestService.asmx");
        } else
            serverIP.setText(u);
        btnSubmit.setClickable(false);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = serverIP.getText().toString().trim();
                ServerManager serverM = new ServerManager(getApplicationContext());

                serverM.setServer(url);
                SQLiteAdapter adapter = new SQLiteAdapter(LoginAndServerActivity.this);
                adapter.deleteDatabase();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginAndServerActivity.this);
                builder.setTitle("Restart Notification");
                builder.setMessage("Clear All Notificaiton");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteAdapter adapter = new SQLiteAdapter(LoginAndServerActivity.this);
                        adapter.deleteNotification();
                       looper();
                    }
                });
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       looper();
                    }
                });
                AlertDialog dialog = builder.create();
                if (!adapter.getCounts().matches("0")) {
                    dialog.show();
                    dialog.setCancelable(false);
                }
                else {
                    looper();
                }
               // adapter.deleteNotification();

            }
        });

        if (sm.isLoggedIn()) {
            Intent intent = new Intent(LoginAndServerActivity.this, TableActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            LoginAndServerActivity.this.finish();

        }

        {
            //for userspinner
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = new StringRequest(Request.Method.POST,
                    new ServerManager(getApplicationContext()).getServer() + "/GetUserList"
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject oj = new JSONObject(response);
                        JSONArray ja = oj.getJSONArray("UserDetails");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject a = ja.getJSONObject(i);
                            String de = a.getString("UserName");
                            String id = a.getString("UserID");
                            userList.add(de);

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginAndServerActivity.this,
                                android.R.layout.simple_dropdown_item_1line, userList);
                       // adapter.setDropDownViewResource(R.layout.spinner_text_color);
                        users.setAdapter(adapter);
                        btnSubmit.setClickable(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    btnSubmit.setClickable(true);


                }
            });
            requestQueue.add(request);
        }


        users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user = adapterView.getItemAtPosition(i).toString();
                //    Toast.makeText(TableActivity.this, "username " + username, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                pass = pasword.getText().toString().trim();

                if (pass.length() > 0) {

                    //     Toast.makeText(TableActivity.this, Build.DEVICE, Toast.LENGTH_SHORT).show();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.POST,
                            new ServerManager(getApplicationContext()).getServer() + "/Login"
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //   Toast.makeText(LoginAndServerActivity.this, response, Toast.LENGTH_SHORT).show();
                            try {

                                JSONObject oj = new JSONObject(response);

                                String status = oj.getString("status");

                                if (status.matches("yes")) {

                                    String username = oj.getString("username");
                                    String id = oj.getString("Id");
                                    String type = oj.getString("type");
                                    //  dialog.dismiss();
                                    sm.setUsername(username);
                                    //  String com = department.getText().toString();
                                    // sm.setRemarks(com);
                                    sm.setType(type);
                                    sm.setUserId(id);
                                    Intent intent = new Intent(LoginAndServerActivity.this, TableActivity.class);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    LoginAndServerActivity.this.finish();
                                    //  CombinationActivity.this.finish();
                                }
                                if (status.matches("no")) {
                                    Toast.makeText(LoginAndServerActivity.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                // dialog.dismiss();
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", error.toString());
                            // dialog.dismiss();
                            Toast.makeText(LoginAndServerActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("UserName", user);
                            map.put("Password", pass);
                            map.put("device_id", "1");
                            map.put("device_type", "123");
                            return map;
                        }
                    };
                    requestQueue.add(request);
                } else
                    Toast.makeText(LoginAndServerActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void looper(){
        Toast.makeText(LoginAndServerActivity.this, "Server Changed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginAndServerActivity.this, LoginAndServerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        LoginAndServerActivity.this.finish();
    }
}

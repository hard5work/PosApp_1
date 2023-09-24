package bonfire.apps.pos.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Objects;

import bonfire.apps.pos.R;
import bonfire.apps.pos.adapter.CartViewAdapter;
import bonfire.apps.pos.adapter.MenuAdapter;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.modules.DataModule;

public class CombinationActivity extends AppCompatActivity {
    RecyclerView menuView;
    public static RecyclerView cartView;
    List<DataModule> dom = new ArrayList<>();
    List<DataModule> com = new ArrayList<>();
    TextView changeTable, cartitems;

    AlertDialog dialog;
    String username, password;

    List<String> userList = new ArrayList<>();
    int orientation;
    public static Button add, clear;
    ProgressDialog progress;
    String itemID;
    Toolbar toolbar;
    ServerManager sm;
    SQLiteAdapter sa;
    CartViewAdapter adapter;
    String itemName;
    public static TextView totalAmt;
    public static int saSize;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.combine_list_cart_dish);
        menuView = findViewById(R.id.new_menu);
        cartView = findViewById(R.id.new_cart);
        add = findViewById(R.id.place_order);
        clear = findViewById(R.id.cancel_cart_list);
        totalAmt = findViewById(R.id.sumAmount);
        changeTable = findViewById(R.id.changeTable);
        cartitems = findViewById(R.id.cartItems);
        sm = new ServerManager(getApplicationContext());
        sa = new SQLiteAdapter(getApplicationContext());
        String mm = "Total : " + sa.getAmtT();
        totalAmt.setText(mm);
        itemID = getIntent().getStringExtra("itemID");
        progress = new ProgressDialog(CombinationActivity.this);
        progress.setMessage("Please Wait....");
        progress.setTitle("Loading..!!");
        progress.setCancelable(false);
        itemName = getIntent().getStringExtra("itemName");
        String menuName = "Catergory- " + itemName;
        setTitle(menuName);

        String cartss = "Cart Items for Table : " + sm.getTableno();
        cartitems.setText(cartss);

        changeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CombinationActivity.this, TableActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                CombinationActivity.this.finish();
            }
        });

        orientation = getResources().getConfiguration().orientation;

        menuView.hasFixedSize();
        cartView.hasFixedSize();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            menuView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        } else
            menuView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

        cartView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        dom.clear();
        foodList();
        com.clear();
        com = sa.getAllDatas();

        adapter = new CartViewAdapter(CombinationActivity.this, com);
        // adapter.notifyDataSetChanged();
        cartView.setAdapter(adapter);



        // Toast.makeText(this, "act " + saSize, Toast.LENGTH_SHORT).show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saSize = sa.getAllDatas().size();
                //  Toast.makeText(CombinationActivity.this, ">0", Toast.LENGTH_SHORT).show();
                if (saSize > 0) {
                    // onClicker();
                    doOrder();
                }

            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteAdapter adapter1 = new SQLiteAdapter(getApplicationContext());
                adapter1.deleteDatabase();
                com = adapter1.getAllDatas();
                CartViewAdapter adapter = new CartViewAdapter(CombinationActivity.this, com);
                adapter.notifyDataSetChanged();
                cartView.setAdapter(adapter);
                String ta = "Total : " + adapter1.getAmtT();
                totalAmt.setText(ta);
                //   ListDishesActivity.menuTextView.setText(String.valueOf(adapter1.getQntyT()));


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView toolbarSearch = (SearchView) searchItem.getActionView();
        toolbarSearch.setQueryHint("Search.....");
//        toolbarSearch.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                assert inputMethodManager != null;
//                inputMethodManager.hideSoftInputFromWindow(toolbarSearch.getWindowToken(), 0);
//                return true;
//            }
//        });

        toolbarSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    dom.clear();
                    foodList();
                } else {
                    dom.clear();
                    foodList(s);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void foodList() {
        try {
            progress.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dom.clear();

         RequestQueue rqe = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        StringRequest srs = new StringRequest(Request.Method.POST, new ServerManager(getApplicationContext()).getServer() + "/GetProductCombo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Log.e("response", response);


                        try {
                            progress.dismiss();
                            JSONObject object = new JSONObject(response);

                            JSONArray jAry = object.getJSONArray("ProductDetails");

                            for (int i = 0; i < jAry.length(); i++) {
                                JSONObject jo = jAry.getJSONObject(i);

                                DataModule m = new DataModule();
                                List<DataModule> comboo = new ArrayList<>();
                                m.setItemID(jo.getInt("product_id"));
                                m.setItemImage(jo.getString("product_image"));
                                m.setItemName(jo.getString("productname"));
                                m.setItemPric(jo.getDouble("productprice"));
                                m.setProductDetail(jo.getString("productdetails"));
                                m.setIsComboItem(jo.getString("IsComboItem"));
                                String iscom = jo.getString("IsComboItem");
                                if (iscom.matches("True")) {


                                    JSONArray jam = jo.getJSONArray("ComboCat");
                                    for (int j = 0; j < jam.length(); j++) {
                                        JSONObject com = jam.getJSONObject(j);
                                        List<DataModule> ccombo = new ArrayList<>();
                                        DataModule n = new DataModule();
                                        n.setCCatId(com.getString("CCatId"));
                                        n.setCCategoryName(com.getString("CCategoryName"));
                                        JSONArray aj = com.getJSONArray("ComboCatItems");
                                        for (int k = 0; k < aj.length(); k++) {
                                            JSONObject oo = aj.getJSONObject(k);
                                            DataModule o = new DataModule();
                                            o.setCombo_product_id(oo.getString("Combo_product_id"));
                                            o.setCombo_product_name(oo.getString("Combo_productname"));
                                            ccombo.add(o);
                                        }
                                        n.setCombo(ccombo);

                                        comboo.add(n);
                                    }
                                    m.setCombos(comboo);
                                }

                                dom.add(m);


                            }

                            MenuAdapter adapter = new MenuAdapter(CombinationActivity.this, dom);
                            menuView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.e("error", error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
//

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("CategoryID", itemID);
                return map;
            }
        };
        rqe.add(srs);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void foodList(final String s) {
        dom.clear();
        final List<DataModule> sdom = new ArrayList<DataModule>();
        sdom.clear();
        RequestQueue rqs = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        StringRequest sr = new StringRequest(Request.Method.POST, new ServerManager(getApplicationContext()).getServer() + "/GetProductCombo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    Log.e("response", response);

                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray jAry = object.getJSONArray("ProductDetails");

                            for (int i = 0; i < jAry.length(); i++) {
                                JSONObject jo = jAry.getJSONObject(i);

                                DataModule m = new DataModule();
                                List<DataModule> comboo = new ArrayList<>();
                                List<DataModule> ccombo = new ArrayList<>();
                                m.setItemID(jo.getInt("product_id"));
                                m.setItemImage(jo.getString("product_image"));
                                m.setItemName(jo.getString("productname"));
                                String s1 = jo.getString("productname");
                                m.setItemPric(jo.getDouble("productprice"));
                                m.setProductDetail(jo.getString("productdetails"));
                                m.setIsComboItem(jo.getString("IsComboItem"));
                                String iscom = jo.getString("IsComboItem");
                                if (iscom.matches("True")) {


                                    JSONArray jam = jo.getJSONArray("ComboCat");
                                    for (int j = 0; j < jam.length(); j++) {
                                        JSONObject com = jam.getJSONObject(j);
                                        DataModule n = new DataModule();
                                        n.setCCatId(com.getString("CCatId"));
                                        n.setCCategoryName(com.getString("CCategoryName"));
                                        JSONArray aj = com.getJSONArray("ComboCatItems");
                                        for (int k = 0; k < aj.length(); k++) {
                                            JSONObject oo = aj.getJSONObject(k);
                                            DataModule o = new DataModule();
                                            o.setCombo_product_id(oo.getString("Combo_product_id"));
                                            o.setCombo_product_name(oo.getString("Combo_productname"));
                                            ccombo.add(o);
                                        }
                                        n.setCombo(ccombo);

                                        comboo.add(n);
                                    }
                                    m.setCombos(comboo);
                                }

                                if (s1.toUpperCase().contains(s.toUpperCase()))
                                    sdom.add(m);


                            }

                            MenuAdapter adapter = new MenuAdapter(CombinationActivity.this, sdom);
                            adapter.notifyDataSetChanged();
                            menuView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
//

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("CategoryID", itemID);
                return map;
            }
        };
        rqs.add(sr);
    }

    public void onClicker() {
        @SuppressLint("InflateParams") View v =
                LayoutInflater.from(CombinationActivity.this)
                        .inflate(R.layout.new_login_layout, null);
        final EditText department = v.findViewById(R.id.spinDepart);
        final Spinner users = v.findViewById(R.id.spinUser);
        final EditText pasword = v.findViewById(R.id.userPass);
        final Button login = v.findViewById(R.id.login);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(CombinationActivity.this);
        builder.setView(v);
        final AlertDialog dialog = builder.create();

        if (!sm.isLoggedIn()) {
            dialog.show();
            dialog.setCancelable(false);
        } else {
            Intent intent = new Intent(CombinationActivity.this, TableActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //   CombinationActivity.this.finish();

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_dropdown_item_1line, userList);
                        users.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(request);
        }

        users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                username = adapterView.getItemAtPosition(i).toString();
                //    Toast.makeText(TableActivity.this, "username " + username, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                password = pasword.getText().toString().trim();

                if (password.length() > 0) {

                    //     Toast.makeText(TableActivity.this, Build.DEVICE, Toast.LENGTH_SHORT).show();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.POST,
                            new ServerManager(getApplicationContext()).getServer() + "/Login"
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //   Toast.makeText(TableActivity.this, response, Toast.LENGTH_SHORT).show();
                            try {

                                JSONObject oj = new JSONObject(response);
                                String username = oj.getString("username");
                                String id = oj.getString("Id");
                                String type = oj.getString("type");

                                String status = oj.getString("status");

                                if (status.matches("yes")) {
                                    dialog.dismiss();
                                    sm.setUsername(username);
                                    String com = department.getText().toString();
                                    sm.setRemarks(com);
                                    sm.setType(type);
                                    sm.setUserId(id);
                                    Intent intent = new Intent(CombinationActivity.this, TableActivity.class);
                                    //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    //  CombinationActivity.this.finish();
                                }

                            } catch (JSONException e) {
                                dialog.dismiss();
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", error.toString());
                            dialog.dismiss();
                            Toast.makeText(CombinationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("UserName", username);
                            map.put("Password", password);
                            map.put("device_id", "1");
                            map.put("device_type", "123");
                            return map;
                        }
                    };
                    requestQueue.add(request);
                } else
                    Toast.makeText(CombinationActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();

            }
        });


/*
                    View commenter = LayoutInflater.from(getApplicationContext()).inflate(R.layout.new_login_layout, null);
                    final EditText inst = commenter.findViewById(R.id.addcomment);
                    final Button submit = commenter.findViewById(R.id.submitComment);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CombinationActivity.this);
                    builder.setView(commenter);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String com = inst.getText().toString();
                            sm.setRemarks(com);




                        }
                    });
*/


    }

    public void doOrder() {

        sa = new SQLiteAdapter(CombinationActivity.this);
        View v = LayoutInflater.from(CombinationActivity.this).inflate(R.layout.add_comment, null);
        final EditText name = v.findViewById(R.id.addcomment);
        final Button send = v.findViewById(R.id.submitComment);

        AlertDialog.Builder builder = new AlertDialog.Builder(CombinationActivity.this);
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setTitle("Placing Order");
                progress.setMessage("Please Wait!!!");
                progress.show();
                progress.setCancelable(false);
                send.setClickable(false);
                String cusname = name.getText().toString();
                //Toast.makeText(context, "Order placed", Toast.LENGTH_SHORT).show();
                //  dialog.dismiss();

                sm.setRemarks(cusname);


                final JSONObject object = new JSONObject();
                //JSONObject comboCat = new JSONObject();
                // JSONObject order = new JSONObject();
                try {
                    object.put("customername", " ");
                    object.put("contact", " ");
                    object.put("user_id", sm.getId());
                    object.put("user_name", sm.getUsername());
                    object.put("table_id", sm.getTableID());
                    object.put("table_no", sm.getTableno());
                    object.put("department_id", "1");
                    object.put("remark", sm.getRemark());

                    JSONArray jab = new JSONArray();
                    for (int i = 0; i < sa.getAllDatas().size(); i++) {

                        DataModule dam = sa.getAllDatas().get(i);
                        JSONObject oj = new JSONObject();
                        oj.put("product_id", dam.getItemID());
                        oj.put("qty", dam.getItemQty());
                        oj.put("price", dam.getItemPric());
                        oj.put("IsComboItem", dam.getIsComboItem());

                        JSONArray comCat = new JSONArray();
                        try {
                            for (int j = 0; j < sa.getProdcutData(String.valueOf(dam.getItemID()), dam.getSno()).size(); j++) {
                                DataModule dm = sa.getProdcutData(String.valueOf(dam.getItemID()), dam.getSno()).get(j);
                                JSONObject catoj = new JSONObject();
                                catoj.put("Combo_product_id", dm.getCombo_product_id());
                                catoj.put("CcatID", dm.getItemID());
                                comCat.put(catoj);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        oj.put("product_remark", dam.getInstruction());
                        oj.put("Combocat", comCat);


                        jab.put(oj);
                    }

                    object.put("order", jab);

                } catch (JSONException e) {
                    e.printStackTrace();
                    send.setClickable(true);
                }

                {
                    //  Toast.makeText(context, object.toString(), Toast.LENGTH_SHORT).show();
                    //  Log.e("jsonobject", object.toString());
                    StringRequest str = new StringRequest(Request.Method.POST,
                            new ServerManager(CombinationActivity.this).getServer() + "/PlaceOrder",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //     Toast.makeText(context,response, Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(context, "orderPlacedSUCCESS", Toast.LENGTH_SHORT).show();
                                    try {
                                        JSONObject oo = new JSONObject(response);
                                        String yes = oo.getString("status");
                                        if (yes.matches("yes")) {
                                            progress.dismiss();
                                            send.setClickable(true);
                                            Toast.makeText(CombinationActivity.this, "Order Placed at Table " + sm.getTableno(), Toast.LENGTH_SHORT).show();

                                            sa.deleteDatabase();
                                            dialog.dismiss();
                                            new ServerManager(CombinationActivity.this).logOut();
                                            Intent intent = new Intent(CombinationActivity.this, LoginAndServerActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            CombinationActivity.this.finish();
                                        }
                                        if(yes.matches("error")){
                                            progress.dismiss();
                                            Toast.makeText(CombinationActivity.this, "Please " +
                                                    "check kot entered", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        send.setClickable(true);
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            progress.dismiss();
                            send.setClickable(true);

                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("OrderJson", object.toString());
                            map.put("Type", "1");
                            return map;
                        }
                    };

                    RequestQueue qu = Volley.newRequestQueue(getApplicationContext());
                    qu.add(str);

                }


            }
        });
    }

}

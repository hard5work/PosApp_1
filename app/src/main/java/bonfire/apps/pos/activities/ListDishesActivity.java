package bonfire.apps.pos.activities;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import bonfire.apps.pos.adapter.MenuAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.modules.DataModule;

public class ListDishesActivity extends AppCompatActivity {
    RecyclerView menuItems, cartItems; //recycler view of menu and cart list
    Button btnPlaceOrder, btnCancelOrder; // button for sending data and canceling data
    int orientation; // value for the orientation of the phone / tablet

    StringRequest sr;
    RequestQueue rq;

    List<DataModule> dataModules = new ArrayList<>();
    Button menuCart;
    @SuppressLint("StaticFieldLeak")
    public static TextView menuTextView;
    String url = "/GetProductCombo";
    String itemID;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dishes);
        menuItems = findViewById(R.id.menuItems);
        // cartItems = findViewById(R.id.cartItems);
        //btnCancelOrder = findViewById(R.id.cancelOrder);
        //btnPlaceOrder = findViewById(R.id.placeOrder);
        setTitle("Menu");
        itemID = getIntent().getStringExtra("itemID");
        orientation = getResources().getConfiguration().orientation;
        menuItems.hasFixedSize();
        //  cartItems.hasFixedSize();


        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            menuItems.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            menuItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        //  cartItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        foodList();
    }

  //  @Override
  /*  public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        //   MenuItem item = menu.findItem(R.id.cartList);
        View cart = menu.findItem(R.id.cartList).getActionView();
        menuCart = (Button) cart.findViewById(R.id.button1);
        menuTextView = (TextView) cart.findViewById(R.id.badge_notification_1);
        SQLiteAdapter adapter = new SQLiteAdapter(getApplicationContext());
        int i = adapter.getQntyT();
        menuTextView.setText(String.valueOf(i));


        menuCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDishesActivity.this, CartListActivity.class);
                startActivity(intent);


            }
        });


        return super.onCreateOptionsMenu(menu);
    }
*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void foodList() {
        rq = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        sr = new StringRequest(Request.Method.POST, new ServerManager(getApplicationContext()).getServer() + url,
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

                                dataModules.add(m);


                            }

                            MenuAdapter adapter = new MenuAdapter(getApplicationContext(), dataModules);
                            menuItems.setAdapter(adapter);

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
        rq.add(sr);
    }


}

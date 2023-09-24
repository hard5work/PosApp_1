package bonfire.apps.pos.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import bonfire.apps.pos.R;
import bonfire.apps.pos.activities.MainActivity;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.modules.DataModule;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableHolder> {

    Context context;
    List<DataModule> mo;
    ServerManager sm;
    SQLiteAdapter sa;
    RecyclerView orderList;
    Button toTable, order;
    AlertDialog dialog;
    List<DataModule> oList = new ArrayList<>();

    public TableAdapter(Context context, List<DataModule> mo) {
        this.context = context;
        this.mo = mo;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public TableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TableHolder(LayoutInflater.from(context).inflate(R.layout.card_table_list, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TableHolder holder, int position) {
        DataModule m = mo.get(holder.getAdapterPosition());
        sm = new ServerManager(context);
        // sa = new SQLiteAdapter(context);

        holder.tableno.setText(m.getTableno());
        if (m.getIs_available().matches("N")) {
            holder.tableno.setBackgroundResource(R.color.red);
        } else
            holder.tableno.setBackgroundResource(R.color.greener);
        String in,last;
        if (m.getFirst_order().isEmpty() || m.getFirst_order() == null){
            in = "In: 00:00";
        }else {
            in = "In: " + m.getFirst_order();
        }
        if(m.getLast_order().isEmpty() || m.getLast_order() == null){
            last = "Ordered: 00:00";
        }else {
            last = "Ordered: " + m.getLast_order();
        }
       // holder.first.setText(m.getFirst_order());
       // holder.last.setText(m.getLast_order());
        holder.first.setText(in);
        holder.last.setText(last);
        String cost = " Rs : " + m.getTable_cost();
        holder.cost.setText(cost);


        holder.cardTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mo.get(holder.getAdapterPosition()).getIs_available().matches("Y")) {
                    Intent intent = new Intent(context, MainActivity.class);
                    sm.setTableno(mo.get(holder.getAdapterPosition()).getTableno());
                    sm.setTableID(mo.get(holder.getAdapterPosition()).getTableid());

                    context.startActivity(intent);
                    // doOrder(mo.get(holder.getAdapterPosition()).getTableno(), mo.get(holder.getAdapterPosition()).getTableid());

                }
                if (mo.get(holder.getAdapterPosition()).getIs_available().matches("N")) {
                    //getOrderDETAIL
                    View orderView = LayoutInflater.from(context).inflate(R.layout.ordered_items, null);
                    orderList = orderView.findViewById(R.id.pendingOrderList);
                    toTable = orderView.findViewById(R.id.toTable);
                    order = orderView.findViewById(R.id.toOrder);
                    AlertDialog.Builder orderBuilder = new AlertDialog.Builder(context);
                    orderBuilder.setView(orderView);
                    final AlertDialog oDialog = orderBuilder.create();
                    oDialog.show();
                    toTable.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            oDialog.dismiss();
                        }
                    });
                    orderList.hasFixedSize();
                    orderList.setLayoutManager(new LinearLayoutManager(context));
                    oList.clear();
                    {
                        StringRequest str = new StringRequest(Request.Method.POST,
                                new ServerManager(context).getServer() + "/Get_Pending_Order",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //     Toast.makeText(context,response, Toast.LENGTH_SHORT).show();
                                        // Toast.makeText(context, "orderPlacedSUCCESS", Toast.LENGTH_SHORT).show();
                                        try {
                                            JSONObject oo = new JSONObject(response);
                                            JSONArray jam = oo.getJSONArray("Order Details Available");
                                            for (int i = 0; i < jam.length(); i++) {
                                                JSONObject jo = jam.getJSONObject(i);
                                                DataModule dm = new DataModule();
                                                dm.setItemPric(jo.getDouble("rate"));
                                                dm.setItemQty(jo.getInt("quantity"));
                                                dm.setItemName(jo.getString("item_name"));
                                                dm.setItemAmount(jo.getDouble("amount"));
                                                oList.add(dm);

                                            }
                                            OrderListAdapter adapter = new OrderListAdapter(context, oList);
                                            orderList.setAdapter(adapter);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/x-www-form-urlencoded; charset=UTF-8";
                            }

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("TableID", mo.get(holder.getAdapterPosition()).getTableid());
                                return map;
                            }
                        };

                        RequestQueue qu = Volley.newRequestQueue(context);
                        qu.add(str);


                    }

                    order.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            oDialog.dismiss();
                            // doOrder(mo.get(holder.getAdapterPosition()).getTableno(), mo.get(holder.getAdapterPosition()).getTableid());
                            Intent intent = new Intent(context, MainActivity.class);
                            sm.setTableno(mo.get(holder.getAdapterPosition()).getTableno());
                            sm.setTableID(mo.get(holder.getAdapterPosition()).getTableid());
                            context.startActivity(intent);
                        }
                    });

                }
            }
        });

    }

    public void doOrder(final String tableno, final String tableid) {
        sa = new SQLiteAdapter(context);
        View v = LayoutInflater.from(context).inflate(R.layout.personal_information, null);
        final EditText name = v.findViewById(R.id.cname);
        final EditText num = v.findViewById(R.id.cnumber);
        final Button send = v.findViewById(R.id.submitinfo);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cusname = name.getText().toString();
                String cusnum = num.getText().toString();
                //Toast.makeText(context, "Order placed", Toast.LENGTH_SHORT).show();
                //  dialog.dismiss();


                final JSONObject object = new JSONObject();
                //JSONObject comboCat = new JSONObject();
                // JSONObject order = new JSONObject();
                try {
                    object.put("customername", cusname);
                    object.put("contact", cusnum);
                    object.put("user_id", sm.getId());
                    object.put("user_name", sm.getUsername());
                    object.put("table_id", tableid);
                    object.put("table_no", tableno);
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
                        for (int j = 0; j < sa.getProdcutData().size(); j++) {
                            DataModule dm = sa.getProdcutData().get(j);
                            JSONObject catoj = new JSONObject();
                            catoj.put("Combo_product_id", dm.getCombo_product_id());
                            catoj.put("CcatID", dm.getItemID());
                            comCat.put(catoj);

                        }
                        oj.put("product_remark", dam.getInstruction());
                        oj.put("Combocat", comCat);


                        jab.put(oj);
                    }

                    object.put("order", jab);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                {
                    //  Toast.makeText(context, object.toString(), Toast.LENGTH_SHORT).show();
                    //  Log.e("jsonobject", object.toString());
                    StringRequest str = new StringRequest(Request.Method.POST,
                            new ServerManager(context).getServer() + "/PlaceOrder",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //     Toast.makeText(context,response, Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(context, "orderPlacedSUCCESS", Toast.LENGTH_SHORT).show();
                                    try {
                                        JSONObject oo = new JSONObject(response);
                                        String yes = oo.getString("status");
                                        if (yes.matches("yes")) {
                                            Toast.makeText(context, "Order Placed", Toast.LENGTH_SHORT).show();
                                            //  sm.logOut();
                                            sa.deleteDatabase();
                                            dialog.dismiss();
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            ((Activity) context).finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();

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

                    RequestQueue qu = Volley.newRequestQueue(context);
                    qu.add(str);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mo.size();
    }

    public class TableHolder extends RecyclerView.ViewHolder {
        TextView tableno;
        RelativeLayout cardTable;
        // LinearLayout cardTable;
        TextView first, last, cost;

        public TableHolder(@NonNull View itemView) {
            super(itemView);

            tableno = itemView.findViewById(R.id.tableNo);
            cardTable = itemView.findViewById(R.id.cardTable);
            first = itemView.findViewById(R.id.first_time);
            last = itemView.findViewById(R.id.last_time);
            cost = itemView.findViewById(R.id.tableCost);
        }
    }


}

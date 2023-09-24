package bonfire.apps.pos.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.R;
import bonfire.apps.pos.adapter.CartViewAdapter;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.modules.DataModule;

public class CartListActivity extends AppCompatActivity {

    public RecyclerView cartRecycler;
    public static CartViewAdapter adapter;
    Button btnAdd, btnCancel;
    List<DataModule> dataModules = new ArrayList<>();
    SQLiteAdapter db;
    ServerManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        cartRecycler = findViewById(R.id.cartRecycler);
        btnAdd = findViewById(R.id.placeOrder);
        btnCancel = findViewById(R.id.cancelOrder);
        db = new SQLiteAdapter(getApplicationContext());

        sm = new ServerManager(getApplicationContext());

        cartRecycler.hasFixedSize();
        cartRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dataModules = db.getAllDatas();

         adapter = new CartViewAdapter(CartListActivity.this, dataModules);
        adapter.notifyDataSetChanged();
        cartRecycler.setAdapter(adapter);


        if(dataModules.size() > 0) {

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View commenter = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_comment, null);
                    final EditText inst = commenter.findViewById(R.id.addcomment);
                    final Button submit = commenter.findViewById(R.id.submitComment);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CartListActivity.this);
                    builder.setView(commenter);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String com = inst.getText().toString();
                            sm.setRemarks(com);

                            alertDialog.dismiss();
                            Intent intent = new Intent(CartListActivity.this, TableActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            CartListActivity.this.finish();


                        }
                    });


                }
            });
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteAdapter adapter1 = new SQLiteAdapter(getApplicationContext());
                adapter1.deleteDatabase();
                dataModules = db.getAllDatas();
                CartViewAdapter adapter = new CartViewAdapter(CartListActivity.this, dataModules);
                adapter.notifyDataSetChanged();
                cartRecycler.setAdapter(adapter);
                ListDishesActivity.menuTextView.setText(String.valueOf(adapter1.getQntyT()));


            }
        });
    }
}

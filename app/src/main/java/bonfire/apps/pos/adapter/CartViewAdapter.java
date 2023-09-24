package bonfire.apps.pos.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.R;
import bonfire.apps.pos.activities.CombinationActivity;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.modules.DataModule;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder> {
    Context con;
    Activity autoTa;
    List<DataModule> dataL;

    public CartViewAdapter(Context con, List<DataModule> dataL, Activity autoTa) {
        this.con = con;
        this.dataL = dataL;
        this.autoTa =autoTa;
    }
    public CartViewAdapter(Context con, List<DataModule> dataL) {
        this.con = con;
        this.dataL = dataL;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  return new CartViewHolder(LayoutInflater.from(con).inflate(R.layout.cart_cart_adapter, parent, false));
        return new CartViewHolder(LayoutInflater.from(con).inflate(R.layout.new_cart_card_design, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final DataModule data = dataL.get(position);
        holder.itemName.setText(data.getItemName());
        //   holder.itemImage.setImageResource(data.getItemImag());
        holder.qty.setText(String.valueOf(data.getItemQty()));
        final int a = data.getItemQty();
        holder.totalAmt.setText(String.valueOf(data.getItemAmount()));
        String price = "Price " + String.valueOf(data.getItemPric());
        holder.itemPrice.setText(price);
        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("InflateParams") View commenter = LayoutInflater.from(con).inflate(R.layout.add_comment_auto, null);
             //   final EditText inst = commenter.findViewById(R.id.addcomment);

                final AutoCompleteTextView namer = (AutoCompleteTextView) commenter.findViewById(R.id.addcommentauto);

                final Button submit = commenter.findViewById(R.id.submitComment);

                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setView(commenter);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //inst.setVisibility(View.GONE);
              //  namer.setVisibility(View.VISIBLE);

                  List<String> list = new ArrayList<>();
                  list= new SQLiteAdapter(con).getInstr();
              //  Toast.makeText(con, "" + list.size(), Toast.LENGTH_SHORT).show();
                ArrayAdapter<String> adapterInst =
                        new ArrayAdapter<String>(con,android.R.layout.simple_list_item_1,new SQLiteAdapter(con).getInstr());

                // CustomArrayAdapter adapterInst = new CustomArrayAdapter(con,list);
                namer.setThreshold(1);
                namer.setAdapter(adapterInst);

                namer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String com = (String) parent.getItemAtPosition(position);
                        Toast.makeText(con, com, Toast.LENGTH_SHORT).show();
                        new SQLiteAdapter(con).addInst(String.valueOf(data.getItemID()),
                                com, String.valueOf(data.getDbid()));
                    }
                });





                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        SQLiteAdapter adapter = new SQLiteAdapter(con);



                  //      inst.setVisibility(View.VISIBLE);
                     //   namer.setVisibility(View.GONE);
                        String com = namer.getText().toString();
                        Log.e("namers", com);
                        adapter.insertInstruction(com);

                        adapter.addInst(String.valueOf(data.getItemID()),
                                com, String.valueOf(data.getDbid()));
                        alertDialog.dismiss();
                    }
                });


            }
        });
        holder.deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getIsComboItem().matches("True")) {
                    SQLiteAdapter adapter = new SQLiteAdapter(con);
                    try {
                        adapter.dedudeComboQty(String.valueOf(data.getItemID()), String.valueOf(data.getSno()));

                    //  holder.cartCard.setVisibility(View.GONE);

                    dataL.remove(holder.getAdapterPosition());

                    notifyItemRemoved(holder.getAdapterPosition());
                    //   notifyItemChanged(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), dataL.size());
                    int i = adapter.getQntyT();
                    String mm = "Total : " + adapter.getAmtT();
                    CombinationActivity.totalAmt.setText(mm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    SQLiteAdapter adapter = new SQLiteAdapter(con);
                    try {
                        adapter.delete(String.valueOf(data.getItemID()), String.valueOf(data.getDbid()));

                        //  holder.cartCard.setVisibility(View.GONE);

                        dataL.remove(holder.getAdapterPosition());

                    notifyItemRemoved(holder.getAdapterPosition());
                    //   notifyItemChanged(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), dataL.size());
                    int i = adapter.getQntyT();
                    String mm = "Total : " + adapter.getAmtT();
                    CombinationActivity.totalAmt.setText(mm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //   ListDishesActivity.menuTextView.setText(String.valueOf(i));
                }
            }
        });

        holder.addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteAdapter adapter = new SQLiteAdapter(con);
                try{
                adapter.updateQtyAuto(String.valueOf(data.getItemID()),
                        String.valueOf(data.getDbid()));
                int qt = adapter.getQnty(String.valueOf(data.getItemID()));
                holder.qty.setText(String.valueOf(qt));

                double amt = adapter.getAmt(String.valueOf(data.getItemID()));
                holder.totalAmt.setText(String.valueOf(amt));

                String mm = "Total : " + adapter.getAmtT();
                CombinationActivity.totalAmt.setText(mm);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //           ListDishesActivity.menuTextView.setText(String.valueOf(i));

            }
        });
        holder.delQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteAdapter adapter = new SQLiteAdapter(con);
                try {
                    adapter.qtyAutoDedud(String.valueOf(data.getItemID()),
                            String.valueOf(data.getDbid()));


                int qt = adapter.getQnty(String.valueOf(data.getItemID()));

                double amt = adapter.getAmt(String.valueOf(data.getItemID()));


                holder.qty.setText(String.valueOf(qt));
                holder.totalAmt.setText(String.valueOf(amt));

                int ii = adapter.getQntyT();
                String mm1 = "Total : " + adapter.getAmtT();
                CombinationActivity.totalAmt.setText(mm1);

                //          ListDishesActivity.menuTextView.setText(String.valueOf(ii));

                if (qt < 1) {
                    adapter.delete(String.valueOf(data.getItemID()), String.valueOf(data.getDbid()));

                    holder.qty.setText(String.valueOf(qt));
                    holder.totalAmt.setText(String.valueOf(amt));


                    //    holder.cartCard.setVisibility(View.GONE);
                    int i = adapter.getQntyT();
                    //        ListDishesActivity.menuTextView.setText(String.valueOf(i));
                    Toast.makeText(con, "Item Deleted due to 0 qty", Toast.LENGTH_SHORT).show();
                    try {
                        dataL.remove(holder.getAdapterPosition());

                    notifyItemRemoved(holder.getAdapterPosition());
                    //  notifyItemChanged(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), dataL.size());
                    String mm = "Total : " + adapter.getAmtT();
                    CombinationActivity.totalAmt.setText(mm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (data.getIsComboItem().matches("True")) {
            holder.comboList.setVisibility(View.VISIBLE);
            holder.comboList.hasFixedSize();
            holder.comboList.setLayoutManager(new GridLayoutManager(con, 1));
            SQLiteAdapter aa = new SQLiteAdapter(con);
            ComboItemViewer adapters = new ComboItemViewer(con,
                    aa.getProdcutData(String.valueOf(dataL.get(holder.getAdapterPosition()).getItemID()),
                            data.getSno()));
            holder.comboList.setAdapter(adapters);
        } else
            holder.comboList.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return dataL.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        CardView cartCard;
        //  ImageView itemImage, addQty, delQty;
        TextView itemImage, addQty, delQty;
        // Button addComment, deleteData;
        TextView addComment, deleteData;
        TextView totalAmt, qty, itemName, itemPrice;
        public RecyclerView comboList;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
           /* cartCard = itemView.findViewById(R.id.cardCart);
            itemPrice = itemView.findViewById(R.id.priceAmt);
            addQty = itemView.findViewById(R.id.addQty);
            delQty = itemView.findViewById(R.id.minusQty);
            addComment = itemView.findViewById(R.id.addInsttu);
            deleteData = itemView.findViewById(R.id.cancelOrderCart);
            totalAmt = itemView.findViewById(R.id.itemAmountCart);
            qty = itemView.findViewById(R.id.itemQtyCart);
            itemName = itemView.findViewById(R.id.itemNameCart);*/

            cartCard = itemView.findViewById(R.id.card_qtny);
            itemPrice = itemView.findViewById(R.id.cart_item_price);
            addQty = itemView.findViewById(R.id.add_qnty);
            delQty = itemView.findViewById(R.id.minus_qnty);
            addComment = itemView.findViewById(R.id.add_cart);
            deleteData = itemView.findViewById(R.id.delete_cart);
            totalAmt = itemView.findViewById(R.id.cart_amount);
            qty = itemView.findViewById(R.id.cart_qnty);
            itemName = itemView.findViewById(R.id.cart_item_name);
            comboList = itemView.findViewById(R.id.comboItemView);


        }
    }
}

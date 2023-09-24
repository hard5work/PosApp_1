package bonfire.apps.pos.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.R;
import bonfire.apps.pos.activities.CombinationActivity;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.modules.DataModule;

import static java.lang.Integer.parseInt;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuAdapterHolder> {

    private Context context;
    private List<DataModule> dataModules;
    CartViewAdapter cAdapter;
    SQLiteAdapter adapter;
    List<DataModule> cList = new ArrayList<>();

    public MenuAdapter(Context context, List<DataModule> dataModules) {
        this.context = context;
        this.dataModules = dataModules;
    }

    @NonNull
    @Override
    public MenuAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  return new MenuAdapterHolder(LayoutInflater.from(context).inflate(R.layout.menu_items_cart_list, parent, false));
        return new MenuAdapterHolder(LayoutInflater.from(context).inflate(R.layout.new_menu_list_card_design, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuAdapterHolder holder, int position) {
        final DataModule data = dataModules.get(position);

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.noimagecolor);
        Glide.with(context)
                .load(data.getItemImage())
                .apply(options)
                .into(holder.itemImage);

        String price = "Price " + data.getItemPric();
        holder.itemPrice.setText(price);
        holder.itemName.setText(data.getItemName());

        adapter = new SQLiteAdapter(context);
      /*  holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteAdapter adapter = new SQLiteAdapter(context);
                if (!adapter.checkExists(String.valueOf(data.getItemID()))) {
                    adapter.insert(data.getItemName(), (data.getItemImag()), data.getItemPric(), String.valueOf(data.getItemID()), data.getIsComboItem());
                    }

                else
                    adapter.updateQtyAuto(String.valueOf(data.getItemID()));
                int i = adapter.getQntyT();
                //  ListDishesActivity.menuTextView.setText(String.valueOf(i));
            }
        });*/

        holder.menuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getIsComboItem().matches("True")){


                    View v = LayoutInflater.from(context).inflate(R.layout.combo_cat_list,null);
                    final RecyclerView comboList = v.findViewById(R.id.combo_cat_list);
                    final Button save, cancel;
                    save = v.findViewById(R.id.combocatsaved);
                    cancel = v.findViewById(R.id.combocatcancel);

                    comboList.setHasFixedSize(true);
                    comboList.setLayoutManager(new LinearLayoutManager(context));/*
                    SQLiteAdapter db = new SQLiteAdapter(context);
                    db.insertComboCatList(dataModules.get(holder.getAdapterPosition()));*/
                    ComboCatAdapter adapter = new ComboCatAdapter(context,dataModules.get(holder.getAdapterPosition()).getCombos(), dataModules.get(holder.getAdapterPosition()).getItemID());
                    comboList.setAdapter(adapter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(v);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setCancelable(true);

                    SQLiteAdapter sq = new SQLiteAdapter(context);
                    String sno = sq.checksno(String.valueOf(dataModules.get(holder.getAdapterPosition()).getItemID()));
                    //Toast.makeText(context, sno, Toast.LENGTH_SHORT).show();
                    if(sno == null){
                        sno = "0";
                    }
                        final int sn = parseInt(sno) + 1;


                    sq.insert(data.getItemName(),
                            (data.getItemImag()),
                            (data.getItemPric()),
                            String.valueOf(data.getItemID()),
                            data.getIsComboItem(),String.valueOf(sn));

                    cList = sq.getAllDatas();
                    cAdapter = new CartViewAdapter(context, cList);
                    cAdapter.notifyDataSetChanged();
                    CombinationActivity.cartView.setAdapter(cAdapter);
                    String mm = "Total : " + new SQLiteAdapter(context).getAmtT();
                    CombinationActivity.totalAmt.setText(mm);
                    CombinationActivity.saSize = cList.size();

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                dialog.dismiss();

                            cAdapter = new CartViewAdapter(context, new SQLiteAdapter(context).getAllDatas());
                            cAdapter.notifyDataSetChanged();
                            CombinationActivity.cartView.setAdapter(cAdapter);
                            String mm = "Total : " + new SQLiteAdapter(context).getAmtT();
                            CombinationActivity.totalAmt.setText(mm);
                            CombinationActivity.saSize = cList.size();


                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SQLiteAdapter db = new SQLiteAdapter(context);
                            try {
                                String sno = db.checksno(String.valueOf(dataModules.get(holder.getAdapterPosition()).getItemID()));

                              //  Toast.makeText(context, "can " +  sno, Toast.LENGTH_SHORT).show();
                                db.dedudeComboQty(String.valueOf(dataModules.get(holder.getAdapterPosition()).getItemID())
                                , sno);

                                // db.delete(String.valueOf(dataModules.get(holder.getAdapterPosition()).getItemID()));
                            }catch (Exception e){
                                Log.e("Error Act", "Error activity");
                            }

                            cList = db.getAllDatas();
                            cAdapter = new CartViewAdapter(context, cList);
                            cAdapter.notifyDataSetChanged();
                            CombinationActivity.cartView.setAdapter(cAdapter);
                            //   notifyItemChanged(holder.getAdapterPosition());
                            int i = db.getQntyT();
                            String mm = "Total : " + db.getAmtT();
                            CombinationActivity.totalAmt.setText(mm);
                            dialog.dismiss();



                        }
                    });


                    
                }
                else {
                    if (!adapter.checkExists(String.valueOf(data.getItemID()))) {
                        adapter.insert(data.getItemName(), (data.getItemImag()),
                                (data.getItemPric()), String.valueOf(data.getItemID()), data.getIsComboItem());
                        cList = adapter.getAllDatas();
                        cAdapter = new CartViewAdapter(context, cList);
                        cAdapter.notifyDataSetChanged();
                        CombinationActivity.cartView.setAdapter(cAdapter);
                        SQLiteAdapter adapter1 = new SQLiteAdapter(context);
                        String mm = "Total : " + adapter1.getAmtT();
                        CombinationActivity.totalAmt.setText(mm);
                        CombinationActivity.saSize = cList.size();
                   /* Toast.makeText(context, "ad " + CombinationActivity.saSize, Toast.LENGTH_SHORT).show();
                    if (CombinationActivity.saSize > 0){
                        CombinationActivity.add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CombinationActivity  act = new CombinationActivity();
                                act.onClicker();
                            }
                        });
                    }*/


                    } else {
                        adapter.updateQtyAuto(String.valueOf(data.getItemID()));
                        // int i = adapter.getQntyT();
                        cList = adapter.getAllDatas();
                        cAdapter = new CartViewAdapter(context, cList);
                        cAdapter.notifyDataSetChanged();
                        CombinationActivity.cartView.setAdapter(cAdapter);
                        String mm = "Total : " + new SQLiteAdapter(context).getAmtT();
                        CombinationActivity.totalAmt.setText(mm);

                        CombinationActivity.saSize = cList.size();
                    }
                }
//                ListDishesActivity.menuTextView.setText(String.valueOf(i));
                //   Toast.makeText(context, data.getIsComboItem(), Toast.LENGTH_SHORT).show();
//                if (data.getIsComboItem().matches("True")) {
//
//                    List<DataModule> p = data.getCombos();
//                    DataModule dm = p.get(data.getItemID());
//                    Toast.makeText(context, data.getCombos().size(), Toast.LENGTH_SHORT).show();

                //  Toast.makeText(context, data.getCombos().toString(), Toast.LENGTH_SHORT).show();

//                   Toast.makeText(context, p.get(holder.getAdapterPosition()).getCCatId(), Toast.LENGTH_SHORT).show();
                // final List<DataModule> cd = p.get(holder.getAdapterPosition()).getCombo();
//                    final DataModule d = p.get(holder.getAdapterPosition());
                //    final DataModule cp = cd.get(holder.getAdapterPosition());
//
//                    adapter.insertComboCat(d.getCCatId(),d.getCCategoryName(), String.valueOf(data.getItemID()));
//                    adapter.insertComboProd(cp.getCombo_product_id(),cp.getCombo_product_name(), d.getCCatId());
                //     Toast.makeText(context, "Table created", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context,
//                            //   d.getCCatId() + " "+ d.getCCategoryName() + " " +
//                            String.valueOf(data.getItemID())
//                            //      + cp.getCombo_product_id()+ " " +cp.getCombo_product_name() + " "
//                            //        + d.getCCatId()
//                            , Toast.LENGTH_SHORT).show();

                //            }

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataModules.size();
    }

    public class MenuAdapterHolder extends RecyclerView.ViewHolder {
        CardView menuCard;
        TextView itemPrice, itemName;
        CardView add;
        ImageView itemImage;

        public MenuAdapterHolder(@NonNull View itemView) {
            super(itemView);

            /*itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            add = itemView.findViewById(R.id.addCart);
            menuCard = itemView.findViewById(R.id.menuItemCard);*/
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            add = itemView.findViewById(R.id.add_items_to_cart);
            menuCard = itemView.findViewById(R.id.menu_card);

        }
    }
}

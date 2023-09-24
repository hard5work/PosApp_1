package bonfire.apps.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bonfire.apps.pos.R;
import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.modules.DataModule;

public class ComboListAdapter extends RecyclerView.Adapter<ComboListAdapter.ComboListHolder> {

    Context context;
    List<DataModule> dm;
    String ccatId, itemid;

    public ComboListAdapter(Context context, List<DataModule> dm, String ccatId, String itemid) {
        this.context = context;
        this.dm = dm;
        this.ccatId = ccatId;
        this.itemid = itemid;
    }

    @NonNull
    @Override
    public ComboListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComboListHolder(LayoutInflater.from(context).inflate(R.layout.card_combo_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final ComboListHolder holder, int position) {
        holder.tv.setText(dm.get(holder.getAdapterPosition()).getCombo_product_name());

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteAdapter db= new SQLiteAdapter(context);
             /*   db.insertComboProd(String.valueOf(dm.get(holder.getAdapterPosition()).getCombo_product_id()),
                        dm.get(holder.getAdapterPosition()).getCombo_product_name(),
                        ccatId);*/
                String sno = db.checksno(itemid);

                String msg = db.checkComboItemEntry(itemid,
                        String.valueOf(dm.get(holder.getAdapterPosition()).getCombo_product_id()),
                        dm.get(holder.getAdapterPosition()).getCombo_product_name(),
                        ccatId, sno);
                ComboCatAdapter.dialog.dismiss();
                Toast.makeText(context, dm.get(holder.getAdapterPosition()).getCombo_product_name() + msg, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dm.size();
    }

    public class ComboListHolder extends RecyclerView.ViewHolder{
        TextView tv;

        public ComboListHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.comboitemname);
        }
    }
}

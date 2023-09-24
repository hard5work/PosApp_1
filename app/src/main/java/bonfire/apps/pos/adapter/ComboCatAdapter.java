package bonfire.apps.pos.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bonfire.apps.pos.R;

import java.util.List;

import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.modules.DataModule;


public class ComboCatAdapter extends RecyclerView.Adapter<ComboCatAdapter.ComboCatHolder> {

    Context context;
    List<DataModule> dm;
    int itemId;
    public static  AlertDialog dialog;


    public ComboCatAdapter(Context context, List<DataModule> dm, int itemId) {
        this.context = context;
        this.dm = dm;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public ComboCatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComboCatHolder(LayoutInflater.from(context).inflate(R.layout.card_combo_cat, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final ComboCatHolder holder, int position) {
        final DataModule d = dm.get(position);
        holder.names.setText(d.getCCategoryName());
        holder.names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(context).inflate(R.layout.combo_lists, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(v);
               dialog = builder.create();
                dialog.setCancelable(true);
                dialog.show();

                final RecyclerView rv = v.findViewById(R.id.combolist);
                rv.hasFixedSize();
                rv.setLayoutManager(new GridLayoutManager(context,3));
                SQLiteAdapter db = new SQLiteAdapter(context);
                db.insertComboCat(dm.get(holder.getAdapterPosition()).getCCatId(),dm.get(holder.getAdapterPosition()).getCCategoryName(),String.valueOf(itemId));
                ComboListAdapter adapter = new ComboListAdapter(context, dm.get(holder.getAdapterPosition()).getCombo(), dm.get(holder.getAdapterPosition()).getCCatId(), String.valueOf(itemId));
                rv.setAdapter(adapter);


            }
        });

    }

    @Override
    public int getItemCount() {
        return dm.size();
    }

    public class ComboCatHolder extends RecyclerView.ViewHolder{

        TextView names;
        public ComboCatHolder(@NonNull View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.combocatName);
        }
    }
}

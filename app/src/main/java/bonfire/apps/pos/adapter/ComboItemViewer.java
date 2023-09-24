package bonfire.apps.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bonfire.apps.pos.R;
import bonfire.apps.pos.modules.DataModule;

public class ComboItemViewer extends RecyclerView.Adapter<ComboItemViewer.ComboItemHolder> {
    Context context;
    List<DataModule> dm;

    public ComboItemViewer(Context context, List<DataModule> dm) {
        this.context = context;
        this.dm = dm;
    }

    @NonNull
    @Override
    public ComboItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComboItemHolder(LayoutInflater.from(context).inflate(R.layout.card_combo_name_cart,
                null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComboItemHolder holder, int position) {
        holder.itemName.setText(dm.get(holder.getAdapterPosition()).getCombo_product_name());

    }

    @Override
    public int getItemCount() {
        return dm.size();
    }

    public class ComboItemHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        public ComboItemHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemnamecombo);

        }
    }
}

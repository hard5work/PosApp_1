package bonfire.apps.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import bonfire.apps.pos.R;

import java.util.List;

import bonfire.apps.pos.modules.DataModule;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListHolder> {
    Context context;
    List<DataModule> orderList;
    String tableNo;

    public OrderListAdapter(Context context, List<DataModule> orderList, String tableNo) {
        this.context = context;
        this.orderList = orderList;
        this.tableNo = tableNo;
    }

    public OrderListAdapter(Context context, List<DataModule> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderListHolder(LayoutInflater.from(context).inflate(R.layout.card_order_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull OrderListHolder holder, int position) {
        DataModule dm = orderList.get(position);

      //  if (tableNo.matches(dm.getTableno())) {
            holder.itemPrice.setText(String.valueOf(dm.getItemPric()));
            String qntty = " X " + dm.getItemQty() + " = ";
            holder.itemQty.setText(qntty);
            holder.itemName.setText(dm.getItemName());
           // double total = dm.getItemPric() * dm.getItemQty();

            holder.itemAmount.setText(String.valueOf(dm.getItemAmount()));
      //  }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderListHolder extends RecyclerView.ViewHolder{
        TextView itemName, itemPrice, itemQty, itemAmount;

        public OrderListHolder(@NonNull View itemView) {
            super(itemView);
            itemName =itemView.findViewById(R.id.oitemName);
            itemPrice =itemView.findViewById(R.id.oitemPrice);
            itemQty =itemView.findViewById(R.id.oQnty);
            itemAmount =itemView.findViewById(R.id.oAmount);

        }
    }
}

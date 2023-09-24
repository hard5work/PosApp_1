package bonfire.apps.pos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.R;
import bonfire.apps.pos.activities.CombinationActivity;
import bonfire.apps.pos.modules.DataModule;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemHolder>
//        implements Filterable
{
    private Context context;
    private List<DataModule> dataModuleList;
    private List<DataModule> arrays;
   // CustomFilter filter;
    private RecyclerTouchLister lister;



    public interface RecyclerTouchLister {
        void onClick(View v, int position);
    }

    public MenuItemAdapter(Context context, List<DataModule> dataModuleList) {
        this.context = context;
        this.dataModuleList = dataModuleList;
        arrays = new ArrayList<>();
        arrays = dataModuleList;
    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuItemHolder(LayoutInflater.from(context).inflate(R.layout.menu_cart_list, parent, false));
    }

    public void setClickListerner(RecyclerTouchLister listerner) {
        this.lister = listerner;
    }


    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {


        final DataModule module = dataModuleList.get(position);
        String itemName = module.getItemName().toUpperCase();
        holder.name.setText(itemName);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(context, ListDishesActivity.class);
                Intent intent = new Intent(context, CombinationActivity.class);
                intent.putExtra("itemID", String.valueOf(module.getItemID()));
                intent.putExtra("itemName", module.getItemName());
                context.startActivity(intent);
            }
        });

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.noimagecolor);

        //rToast.makeText(context, "image " + module.getItemImage(), Toast.LENGTH_SHORT).show();
       // Log.e("image name" , module.getItemImage());
        if (!module.getItemImage().contains(".bmp")){
            holder.imagename.setVisibility(View.VISIBLE);
            holder.imagename.setText(itemName);
        }
        else{
            holder.imagename.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(module.getItemImage())

                .apply(options)
                .into(holder.image);


    }


    @Override
    public int getItemCount() {
        return dataModuleList.size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        LinearLayout card;
        ImageView image;
        TextView name,imagename;

        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.menuCard);
            image = itemView.findViewById(R.id.menuImage);
            name = itemView.findViewById(R.id.menuName);
            imagename =itemView.findViewById(R.id.menuNameImage);
        }
    }





}

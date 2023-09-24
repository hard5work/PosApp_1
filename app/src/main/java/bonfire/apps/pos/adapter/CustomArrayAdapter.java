package bonfire.apps.pos.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import bonfire.apps.pos.R;

class CustomArrayAdapter extends BaseAdapter implements Filterable {
    Context context;
    List<String> items;

    public CustomArrayAdapter(Context context, List<String> items){
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_view,parent,false);
        }
        TextView itemse = convertView.findViewById(R.id.simpleText);
        itemse.setText(items.get(position));

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return null;
    }
}

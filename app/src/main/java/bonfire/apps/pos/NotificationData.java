package bonfire.apps.pos;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.database.SQLiteAdapter;
import bonfire.apps.pos.modules.DataModule;

public class NotificationData extends AppCompatActivity {
    public static RecyclerView sqView;
    SQLiteAdapter sq;
    public static List<DataModule> dms = new ArrayList<>();
    boolean check = false;
    Handler mHandler;
    NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_data);
        sqView = findViewById(R.id.readyItems);
        sqView.hasFixedSize();
        sqView.setLayoutManager(new LinearLayoutManager(NotificationData.this));
        dms = new SQLiteAdapter(NotificationData.this).getNotificationReady();
        adapter =
                new NotificationAdapter(
                        NotificationData.this,
                        new SQLiteAdapter(NotificationData.this).getNotificationReady());
        //  adapter.notifyDataSetChanged();
        sqView.setAdapter(adapter);
        //recur();
        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable, 5000);


    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            if (dms.size() !=  new SQLiteAdapter(NotificationData.this).getNotificationReady().size()) {
                recur();
                dms =  new SQLiteAdapter(NotificationData.this).getNotificationReady();
                Toast.makeText(NotificationData.this, "Data Refreshed", Toast.LENGTH_SHORT).show();

            }

            NotificationData.this.mHandler.postDelayed(m_Runnable, 3000);
        }

    };//runnable

    public void recur() {
        adapter =
                new NotificationAdapter(
                        NotificationData.this,
                        new SQLiteAdapter(NotificationData.this).getNotificationReady());
        adapter.notifyDataSetChanged();
        sqView.setAdapter(adapter);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();
    }

    public static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
        Context context;
        List<DataModule> dm;

        public NotificationAdapter(Context context, List<DataModule> dm) {
            this.context = context;
            this.dm = dm;
        }

        @NonNull
        @Override
        public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NotificationHolder(LayoutInflater.from(context).inflate(R.layout.card_ready_items, null));
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {

            DataModule dms = dm.get(holder.getAdapterPosition());
            holder.id.setText(String.valueOf(dms.getDbid()));
            String ready = dms.getItemName() + " Ready";
            holder.name.setText(ready);
            String tbls = "Table No- " + dms.getTableno() + " Waiter Name- " + dms.getWaitername();
            holder.table.setText(tbls);

        }

        @Override
        public int getItemCount() {
            return dm.size();
        }

        public class NotificationHolder extends RecyclerView.ViewHolder {
            TextView id, name, table;

            public NotificationHolder(@NonNull View itemView) {
                super(itemView);
                id = itemView.findViewById(R.id.sqid);
                name = itemView.findViewById(R.id.sqitemname);
                table = itemView.findViewById(R.id.sqtbl);
            }
        }

    }
}


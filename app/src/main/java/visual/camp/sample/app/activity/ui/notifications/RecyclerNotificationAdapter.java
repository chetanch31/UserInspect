package visual.camp.sample.app.activity.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.AppModel;
import visual.camp.sample.app.activity.NotificationModel;
import visual.camp.sample.app.activity.ui.home.AnalysisActivity;
import visual.camp.sample.app.activity.ui.home.DetailActivity;
import visual.camp.sample.app.activity.ui.home.TaskActivity;

public class RecyclerNotificationAdapter extends RecyclerView.Adapter<RecyclerNotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<NotificationModel> arrNotifications;

    RecyclerNotificationAdapter(Context context, ArrayList<NotificationModel> arrNotifications){
        this.context = context;
        this.arrNotifications = arrNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final boolean done=arrNotifications.get(position).isDone();
        String appName=arrNotifications.get(position).getAppName();
        String title="";
        String noti="";
        if(done){
            title="Task Result";
            noti="Got Task result in website: "+appName+". Tap to view.";

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent2 = new Intent(context, AnalysisActivity.class);
//                    intent2.putExtra("taskId", taskId);
//                    intent2.putExtra("taskName", taskName);
//                    intent2.putExtra("taskDesc", taskDescription);
//                    intent2.putExtra("link", link);
//                    intent2.putExtra("appName", appName);
                    context.startActivity(intent2);
                }
            });
        }
        else{
            title="Website Blacklisted";
            noti="Your website "+appName+" is blacklisted based on user feedback. Check the website URL and again add the website.";
        }
        holder.notiTitleTxt.setText(title);
        holder.notiTxt.setText(noti);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, TaskActivity.class);
//                intent.putExtra("appName", appname);
//                intent.putExtra("role", type);
//                intent.putExtra("link", applink);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return arrNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView notiTitleTxt, notiTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notiTitleTxt=itemView.findViewById(R.id.notification_title_textview);
            notiTxt=itemView.findViewById(R.id.notification_textview);


        }
    }
}

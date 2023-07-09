package visual.camp.sample.app.activity.ui.home;

import android.content.Context;
import android.content.Intent;
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
import visual.camp.sample.app.activity.ui.home.TaskActivity;

public class RecyclerAppAdapter extends RecyclerView.Adapter<RecyclerAppAdapter.ViewHolder> {
    Context context;
    ArrayList<AppModel> arrApps;
    String type;

    RecyclerAppAdapter(Context context, ArrayList<AppModel> arrApps, String type){
        this.context = context;
        this.arrApps = arrApps;
        this.type=type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String appname=arrApps.get(position).getName();
        holder.appNameTxt.setText(appname);
        final int verificationStatus=arrApps.get(position).getVerified();
        final String applink=arrApps.get(position).getLink();


        if(type.equals("dev")){
            if (verificationStatus==0) {
                holder.verificationTxt.setText("Website is blacklisted for now. Review in process.");
                holder.verificationTxt.setTextColor(ContextCompat.getColor(context, R.color.blacklisted));
            } else if (verificationStatus==1) {
                holder.verificationTxt.setText("Website URL is not verified.");
                holder.verificationTxt.setTextColor(ContextCompat.getColor(context, R.color.not_verified));
            } else if (verificationStatus==2) {
                holder.verificationTxt.setText("Website is verified.");
                holder.verificationTxt.setTextColor(ContextCompat.getColor(context, R.color.verified));
            }
            holder.editTaskBtn.setVisibility(View.VISIBLE);
        }
        else if(type.equals("test")){
            if (verificationStatus==0) {
                holder.verificationTxt.setText("Website is blacklisted for now. Review in process.");
                holder.verificationTxt.setTextColor(ContextCompat.getColor(context, R.color.blacklisted));
            } else if (verificationStatus==1) {
                holder.verificationTxt.setText("Website URL is not verified (Unsafe to test)");
                holder.verificationTxt.setTextColor(ContextCompat.getColor(context, R.color.not_verified));
            } else if (verificationStatus==2) {
                holder.verificationTxt.setText("Website is verified (Safe to test)");
                holder.verificationTxt.setTextColor(ContextCompat.getColor(context, R.color.verified));
            }
            holder.editTaskBtn.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtra("appName", appname);
                intent.putExtra("role", type);
                intent.putExtra("link", applink);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView appNameTxt, verificationTxt;
        Button editTaskBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appNameTxt=itemView.findViewById(R.id.app_name_textview);
            verificationTxt=itemView.findViewById(R.id.verification_status_textview);
            editTaskBtn=itemView.findViewById(R.id.edit_button);

            editTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get a reference to the Firebase Realtime Database instance
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference appsRef = database.getReference("apps");

                        // Get the app that needs to be deleted
                        AppModel app = arrApps.get(position);

                        // Remove the app from the Firebase Realtime Database
                        appsRef.child(app.getName()).removeValue();

                        // Remove the app from the ArrayList used in the adapter
//                        arrApps.remove(position);

                        // Notify the adapter that the item has been removed
//                        notifyItemRemoved(position);
                    }
                }
            });
        }
    }
}

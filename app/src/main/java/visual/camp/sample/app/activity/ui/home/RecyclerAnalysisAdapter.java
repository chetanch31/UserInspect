package visual.camp.sample.app.activity.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.AnalysisModel;
import visual.camp.sample.app.activity.AoiActivity;
import visual.camp.sample.app.activity.AppModel;
import visual.camp.sample.app.activity.EyeActivity;
import visual.camp.sample.app.activity.PathActivity;
import visual.camp.sample.app.activity.VideoActivity;

public class RecyclerAnalysisAdapter extends RecyclerView.Adapter<RecyclerAnalysisAdapter.ViewHolder> {
    Context context;
    ArrayList<AnalysisModel> arrAnalysis;
    String type;
    String taskId;

    RecyclerAnalysisAdapter(Context context, ArrayList<AnalysisModel> arrAnalysis, String taskId){
        this.context = context;
        this.arrAnalysis = arrAnalysis;
        this.taskId=taskId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.analysis_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String uname=arrAnalysis.get(position).getName();
        final String uemail=arrAnalysis.get(position).getEmail();
        final Map<String, Map<String, Float>> eyeData=arrAnalysis.get(position).getEyeData();
        holder.emailTxt.setText(uname);
        final String feedback=arrAnalysis.get(position).getUserFeedback();
        holder.feedbackTxt.setText("TestUser Says: '" +feedback+"'");
        final String time="Task Completion time: "+arrAnalysis.get(position).getTime()+" ms";
        holder.timeTxt.setText(time);

        holder.eyeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EyeActivity.class);
                intent.putExtra("eyeData", (Serializable) eyeData);
                context.startActivity(intent);
            }
        });
        holder.pathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("uemail", uemail);
                context.startActivity(intent);
            }
        });
        holder.aoiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AoiActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("uemail", uemail);
                context.startActivity(intent);
            }
        });

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
        return arrAnalysis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView emailTxt, timeTxt, feedbackTxt;
        Button eyeBtn, pathBtn, aoiBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTxt=itemView.findViewById(R.id.email_textview);
            timeTxt=itemView.findViewById(R.id.time_textview);
            feedbackTxt=itemView.findViewById(R.id.feedback_textview);
            eyeBtn=itemView.findViewById(R.id.eye_button);
            pathBtn=itemView.findViewById(R.id.path_button);
            aoiBtn=itemView.findViewById(R.id.aoi_button);
        }
    }
}

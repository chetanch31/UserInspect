package visual.camp.sample.app.activity.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.MainActivity;
import visual.camp.sample.app.activity.MainActivity2;
import visual.camp.sample.app.activity.TaskModel;

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.ViewHolder> {
    Context context;
    ArrayList<TaskModel> arrTasks;
    String type;
    String link;
    DatabaseReference tasksRef;
    String appName, userEmail;
    Boolean comp=false;

    RecyclerTaskAdapter(Context context, ArrayList<TaskModel> arrTasks, String type, String link, DatabaseReference tasksRef, String appName, String userEmail){
        this.context = context;
        this.arrTasks = arrTasks;
        this.type=type;
        this.link=link;
        this.tasksRef = tasksRef;
        this.appName=appName;
        this.userEmail=userEmail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String taskId = arrTasks.get(position).getId();
        final String taskName = arrTasks.get(position).getTitle();
        holder.taskNameTxt.setText(taskName);
        final String taskDescription = arrTasks.get(position).getDescription();
        holder.taskDescriptionTxt.setText(taskDescription);

        if(type.equals("dev")){
            holder.editTaskBtn.setVisibility(View.VISIBLE);
            holder.editTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete the task from the database
                    tasksRef.child(taskId).removeValue();
                    // Remove the task from the recycler view
//                    arrTasks.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, arrTasks.size());
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent2 = new Intent(context, AnalysisActivity.class);
                        intent2.putExtra("taskId", taskId);
                        intent2.putExtra("taskName", taskName);
                        intent2.putExtra("taskDesc", taskDescription);
                        intent2.putExtra("link", link);
                        intent2.putExtra("appName", appName);
                        context.startActivity(intent2);



                }
            });
        }
        else if(type.equals("test")){
            holder.editTaskBtn.setVisibility(View.GONE);
            // Get a reference to the analysis/taskId/emailId node in the Firebase database
            DatabaseReference analysisRef = FirebaseDatabase.getInstance().getReference("Analysis").child(arrTasks.get(position).getId()).child(userEmail.replace('.',','));

// Check if the emailId node exists or not
            analysisRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        holder.taskNameTxt.setText(taskName+" (Completed)");
                        holder.taskNameTxt.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        comp=true;
                        // ...
                    } else {

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    Intent intent = new Intent(context, DetailActivity.class);
                                    intent.putExtra("taskId", taskId);
                                    intent.putExtra("taskName", taskName);
                                    intent.putExtra("taskDesc", taskDescription);
                                    intent.putExtra("link", link);
                                    intent.putExtra("appName", appName);
                                    context.startActivity(intent);

                            }
                        });
                        // The emailId node does not exist in the database
                        // ...
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return arrTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView taskNameTxt, taskDescriptionTxt;
        Button editTaskBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameTxt = itemView.findViewById(R.id.task_title_textview);
            taskDescriptionTxt = itemView.findViewById(R.id.task_description_textview);
            editTaskBtn=itemView.findViewById(R.id.edit_button);

        }
    }
}


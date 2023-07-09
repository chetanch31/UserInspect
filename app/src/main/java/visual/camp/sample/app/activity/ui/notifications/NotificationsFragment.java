package visual.camp.sample.app.activity.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.NotificationModel;
import visual.camp.sample.app.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private String userEmail;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        userEmail = auth.getCurrentUser().getEmail();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = userEmail.replace(".", ",");


        final RecyclerView recyclerView = root.findViewById(R.id.notification_list_recycler_view);
        // Set up the RecyclerView with the LinearLayout
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("notifications").child(key);

        final ArrayList<NotificationModel> notificationList = new ArrayList<>();

        // Attach a ValueEventListener to the notificationRef reference
        ValueEventListener notificationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the notificationList before adding new notifications to avoid duplication
                notificationList.clear();

                // Loop through all the children of the "notifications" node
                for (DataSnapshot notificationSnapshot : dataSnapshot.getChildren()) {
                    // Get the Notification object from the notificationSnapshot and add it to the notificationList
                    NotificationModel notification = notificationSnapshot.getValue(NotificationModel.class);
                    notificationList.add(notification);
                }
                RecyclerNotificationAdapter notificationAdapter = new RecyclerNotificationAdapter(getContext(), notificationList);
                recyclerView.setAdapter(notificationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        };

        // Attach the ValueEventListener to the notificationRef reference
        notificationRef.addValueEventListener(notificationListener);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

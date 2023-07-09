package visual.camp.sample.app.activity.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import java.util.List;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.AppModel;
import visual.camp.sample.app.activity.UserModel;
import visual.camp.sample.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
//    private RecyclerView appListRecyclerView;
//    private AppListAdapter appListAdapter;

    private String userEmail;



    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        final View root = binding.getRoot();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        userEmail = auth.getCurrentUser().getEmail();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = userEmail.replace(".", ",");
        DatabaseReference userRef = database.getReference("users").child(key);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                final String type = userModel.getType();
                String name=userModel.getName();


                if(type.equals("dev")){
                    Button addAppButton = root.findViewById(R.id.add_app_button);
                    addAppButton.setVisibility(View.VISIBLE);
                    addAppButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openAddAppDialog();
                        }
                    });

                    final RecyclerView recyclerView=root.findViewById(R.id.app_list_recycler_view);
                    // Set up the RecyclerView with the LinearLayout
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);


                    DatabaseReference appsRef = FirebaseDatabase.getInstance().getReference("apps");

                    final ArrayList<AppModel> appList = new ArrayList<>();

                    // Attach a ValueEventListener to the appsRef reference
                    ValueEventListener appsListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Clear the appList before adding new apps to avoid duplication
                            appList.clear();

                            // Loop through all the children of the "apps" node
                            for (DataSnapshot appSnapshot : dataSnapshot.getChildren()) {
                                // Get the App object from the appSnapshot and add it to the appList
                                AppModel app = appSnapshot.getValue(AppModel.class);
                                if(app.getEmail().equals(userEmail)){
                                    appList.add(app);
                                }

                            }
                            Log.i("appslist", appList.toString());
                            RecyclerAppAdapter appAdapter=new RecyclerAppAdapter(getContext(), appList, type);
                            recyclerView.setAdapter(appAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                        }
                    };

// Attach the ValueEventListener to the appsRef reference
                    appsRef.addValueEventListener(appsListener);

                }
                else if(type.equals("test")){
                    Button addAppButton = root.findViewById(R.id.add_app_button);
                    addAppButton.setVisibility(View.GONE);

                    final RecyclerView recyclerView=root.findViewById(R.id.app_list_recycler_view);
                    // Set up the RecyclerView with the LinearLayout
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);


                    DatabaseReference appsRef = FirebaseDatabase.getInstance().getReference("apps");

                    final ArrayList<AppModel> appList = new ArrayList<>();

                    // Attach a ValueEventListener to the appsRef reference
                    ValueEventListener appsListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Clear the appList before adding new apps to avoid duplication
                            appList.clear();

                            // Loop through all the children of the "apps" node
                            for (DataSnapshot appSnapshot : dataSnapshot.getChildren()) {
                                // Get the App object from the appSnapshot and add it to the appList
                                AppModel app = appSnapshot.getValue(AppModel.class);
                                if(app.getVerified()>0){
                                    appList.add(app);
                                }

                            }
                            Log.i("appslist", appList.toString());
                            RecyclerAppAdapter appAdapter=new RecyclerAppAdapter(getContext(), appList, type);
                            recyclerView.setAdapter(appAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                        }
                    };

// Attach the ValueEventListener to the appsRef reference
                    appsRef.addValueEventListener(appsListener);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DBError", "getUser:onCancelled", databaseError.toException());
            }
        });












//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                textView.setText(s);
//            }
//        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openAddAppDialog() {
        // Create a new instance of the dialog fragment
        AddAppDialogFragment dialogFragment = new AddAppDialogFragment();
        // Show the dialog
        dialogFragment.show(getChildFragmentManager(), "AddAppDialogFragment");
    }
}
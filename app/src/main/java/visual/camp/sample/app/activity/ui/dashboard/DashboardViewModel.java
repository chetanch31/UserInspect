package visual.camp.sample.app.activity.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private final MutableLiveData<String> mText;

    public DashboardViewModel() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mText = new MutableLiveData<>();
        mText.setValue(currentUser.getEmail());
    }

    public LiveData<String> getText() {
        return mText;
    }
}
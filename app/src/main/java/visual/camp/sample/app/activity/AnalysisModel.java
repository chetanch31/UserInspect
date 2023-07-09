package visual.camp.sample.app.activity;

import java.util.Map;

public class AnalysisModel {
    private String email;
    private String name;
    private String time;
    private String userFeedback;
    private Map<String, Map<String, Float>> eyeData;

    public AnalysisModel() {
        // Default constructor required for calls to DataSnapshot.getValue(AnalysisModel.class)
    }

    public AnalysisModel(String email, String name, String time, String userFeedback, Map<String, Map<String, Float>> eyeData) {
        this.email = email;
        this.name = name;
        this.time = time;
        this.userFeedback = userFeedback;
        this.eyeData = eyeData;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public Map<String, Map<String, Float>> getEyeData() {
        return eyeData;
    }

    public void setEyeData(Map<String, Map<String, Float>> eyeData) {
        this.eyeData = eyeData;
    }
}

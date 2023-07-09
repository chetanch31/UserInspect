package visual.camp.sample.app.activity;

import java.util.Map;

public class AppModel {
    private String email;
    private String name;
    private String link;
    private int verified;
    private boolean status;
    private Map<String, TaskModel> tasks;

    public AppModel() {
        // Default constructor required for calls to DataSnapshot.getValue(AppModel.class)
    }

    public AppModel(String email, String name, String link, int verified, boolean status, Map<String, TaskModel> tasks) {
        this.email = email;
        this.name = name;
        this.link = link;
        this.verified = verified;
        this.status = status;
        this.tasks = tasks;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Map<String, TaskModel> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, TaskModel> tasks) {
        this.tasks = tasks;
    }
}

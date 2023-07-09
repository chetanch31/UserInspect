package visual.camp.sample.app.activity;

public class NotificationModel {
    private String appName;
    private boolean done;
    private String taskId;
    private String currentUser;
    private String devUser;
    private long timestamp;

    public NotificationModel() {
        // Default constructor required for calls to DataSnapshot.getValue(NotificationModel.class)
    }

    public NotificationModel(String appName, boolean done, String taskId, String currentUser, String devUser, long timestamp) {
        this.appName = appName;
        this.done = done;
        this.taskId = taskId;
        this.currentUser = currentUser;
        this.devUser = devUser;
        this.timestamp = timestamp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getDevUser() {
        return devUser;
    }

    public void setDevUser(String devUser) {
        this.devUser = devUser;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

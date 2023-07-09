package visual.camp.sample.app.activity;

public class FeedbackModel {
    private String emailId;
    private String taskId;
    private boolean isCompletionTimeLess;
    private boolean isWebsiteUrlBroken;
    private String otherIssue;

    public FeedbackModel() {
        // Empty constructor needed for Firebase
    }

    public FeedbackModel(String emailId, String taskId, boolean isCompletionTimeLess, boolean isWebsiteUrlBroken, String otherIssue) {
        this.emailId = emailId;
        this.taskId = taskId;
        this.isCompletionTimeLess = isCompletionTimeLess;
        this.isWebsiteUrlBroken = isWebsiteUrlBroken;
        this.otherIssue = otherIssue;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isCompletionTimeLess() {
        return isCompletionTimeLess;
    }

    public void setCompletionTimeLess(boolean completionTimeLess) {
        isCompletionTimeLess = completionTimeLess;
    }

    public boolean isWebsiteUrlBroken() {
        return isWebsiteUrlBroken;
    }

    public void setWebsiteUrlBroken(boolean websiteUrlBroken) {
        isWebsiteUrlBroken = websiteUrlBroken;
    }

    public String getOtherIssue() {
        return otherIssue;
    }

    public void setOtherIssue(String otherIssue) {
        this.otherIssue = otherIssue;
    }
}

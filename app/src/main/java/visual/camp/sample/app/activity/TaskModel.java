package visual.camp.sample.app.activity;

import java.util.Map;

public class TaskModel {
    private String id;
    private String title;
    private String description;
    private Map<String, ResultModel> result;

    public TaskModel() {}

    public TaskModel(String id, String title, String description, Map<String, ResultModel> result) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, ResultModel> getResult() {
        return result;
    }

    public void setResult(Map<String, ResultModel> result) {
        this.result = result;
    }
}

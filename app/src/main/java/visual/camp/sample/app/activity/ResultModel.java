package visual.camp.sample.app.activity;

import android.graphics.Point;

import java.util.Map;

public class ResultModel {
    private String id;
    private String email;
    private String link;
    private Map<Long, Point> eyetrack;

    public ResultModel() {
        // Empty constructor required for Firebase
    }

    public ResultModel(String id, String email, String link, Map<Long, Point> eyetrack) {
        this.id = id;
        this.email = email;
        this.link = link;
        this.eyetrack = eyetrack;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLink() {
        return link;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Map<Long, Point> getEyetrack() {
        return eyetrack;
    }

    public void setEyetrack(Map<Long, Point> eyetrack) {
        this.eyetrack = eyetrack;
    }
}

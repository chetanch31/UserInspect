package visual.camp.sample.app.activity;

public class UserModel {
    private String email;
    private String name;
    private String type;
    private boolean verified;

    public UserModel() {
        // empty constructor required for Firebase
    }

    public UserModel(String email, String name, String type, boolean verified) {
        this.email = email;
        this.name = name;
        this.type = type;
        this.verified = verified;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}


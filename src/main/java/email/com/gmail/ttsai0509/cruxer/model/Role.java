package email.com.gmail.ttsai0509.cruxer.model;

public enum Role {

    USER,
    ADMIN;

    @Override
    public String toString() {
        return "ROLE_" + this.name();
    }


}

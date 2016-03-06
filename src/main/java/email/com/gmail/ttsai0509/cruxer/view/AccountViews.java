package email.com.gmail.ttsai0509.cruxer.view;

public interface AccountViews {

    interface Id {}
    interface Date {}
    interface Info {}
    interface Email {}
    interface Username {}
    interface Roles {}

    interface Standard extends Info, Username, Date {}

}

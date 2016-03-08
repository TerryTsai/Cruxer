package email.com.gmail.ttsai0509.cruxer.view;

public interface HoldViews {

    interface Id {}
    interface Date {}
    interface Model {}
    interface Account {}

    interface Debug extends Id, Date, Model, Account, AccountViews.Standard {}

    interface Standard extends Id, Date, Model {}
}

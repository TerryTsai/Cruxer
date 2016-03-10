package email.com.gmail.ttsai0509.cruxer.view;

public interface WallViews {

    interface Id {}
    interface Date {}
    interface Model {}
    interface Thumbnail {}
    interface Account {}

    interface Debug extends Id, Date, Model, Thumbnail, Account, AccountViews.Standard {}

    interface Standard extends Id, Date, Model {}
}

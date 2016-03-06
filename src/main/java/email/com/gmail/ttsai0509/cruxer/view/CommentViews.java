package email.com.gmail.ttsai0509.cruxer.view;

public interface CommentViews {

    interface Id {}
    interface Account {}
    interface Date {}
    interface Route{}
    interface Hold{}
    interface Wall{}
    interface Text {}
    
    interface Standard extends Id, Date, Text {}

}

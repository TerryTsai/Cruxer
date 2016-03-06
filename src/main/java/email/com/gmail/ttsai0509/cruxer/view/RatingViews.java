package email.com.gmail.ttsai0509.cruxer.view;

public interface RatingViews {
    interface Id {}

    interface Account {}

    interface Route{}

    interface Scale {}

    interface Grade {}

    interface Standard extends Id, Scale, Grade {}
}

package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.view.RatingViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
public class Rating {

    public Rating() {}

    public static Rating createRating(Account account, Route route, Scale scale, int grade) {
        Rating rating = new Rating();
        rating.account = account;
        rating.route = route;
        rating.scale = scale;
        rating.grade = grade;
        return rating;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonView(RatingViews.Id.class)
    private String id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(RatingViews.Account.class)
    private Account account;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(RatingViews.Route.class)
    private Route route;

    @JoinColumn(nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    @JsonView(RatingViews.Scale.class)
    private Scale scale;

    @JsonView(RatingViews.Grade.class)
    private int grade;

}

package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.exception.InvalidFieldException;
import email.com.gmail.ttsai0509.cruxer.view.CommentViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.regex.Pattern;

@Data
@Entity
public class Comment {

    public Comment() {}

    public static Comment createRouteComment(Account account, Route route, String text) {
        Comment comment = new Comment();
        comment.setAccount(account);
        comment.setRoute(route);
        comment.setText(text);
        comment.setDate(new Date());
        return comment;
    }

    public static Comment createHoldComment(Account account, Hold hold, String text) {
        Comment comment = new Comment();
        comment.setAccount(account);
        comment.setHold(hold);
        comment.setText(text);
        comment.setDate(new Date());
        return comment;
    }

    public static Comment createWallComment(Account account, Wall wall, String text) {
        Comment comment = new Comment();
        comment.setAccount(account);
        comment.setWall(wall);
        comment.setText(text);
        comment.setDate(new Date());
        return comment;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonView(CommentViews.Id.class)
    private String id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(CommentViews.Account.class)
    private Account account;

    @Column(nullable = false)
    @JsonView(CommentViews.Date.class)
    private Date date;

    @Column(nullable = false)
    @JsonView(CommentViews.Text.class)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(CommentViews.Route.class)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(CommentViews.Hold.class)
    private Hold hold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(CommentViews.Wall.class)
    private Wall wall;

}

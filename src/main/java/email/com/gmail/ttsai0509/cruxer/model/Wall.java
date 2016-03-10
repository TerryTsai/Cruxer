package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Objects;
import email.com.gmail.ttsai0509.cruxer.view.WallViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Wall {

    public Wall() {}

    public Wall(Account account, String model, String thumbnail) {
        this.setAccount(account);
        this.setModel(model);
        this.setThumbnail(thumbnail);
        this.setDate(new Date());
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonView(WallViews.Id.class)
    private String id;

    @Column(nullable = false)
    @JsonView(WallViews.Date.class)
    private Date date;

    @Column(nullable = false)
    @JsonView(WallViews.Model.class)
    private String model;

    @Column(nullable = false)
    @JsonView(WallViews.Thumbnail.class)
    private String thumbnail;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(WallViews.Account.class)
    private Account account;

    public boolean matches(Wall wall) {
        return Objects.equal(getId(), wall.getId())
                && Objects.equal(getModel(), wall.getModel())
                && Objects.equal(getThumbnail(), wall.getThumbnail());
    }
}
package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.view.WallInstanceViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class WallInstance {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @JsonView(WallInstanceViews.Id.class)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(WallInstanceViews.Wall.class)
    private Wall wall;

    @NotNull
    @JsonView(WallInstanceViews.Location.class)
    private float px;

    @NotNull
    @JsonView(WallInstanceViews.Location.class)
    private float py;

    @NotNull
    @JsonView(WallInstanceViews.Location.class)
    private float pz;

    @NotNull
    @JsonView(WallInstanceViews.Location.class)
    private float qw;

    @NotNull
    @JsonView(WallInstanceViews.Location.class)
    private float qx;

    @NotNull
    @JsonView(WallInstanceViews.Location.class)
    private float qy;

    @NotNull
    @JsonView(WallInstanceViews.Location.class)
    private float qz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(WallInstanceViews.Route.class)
    private Route route;

}

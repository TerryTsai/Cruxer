package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.view.HoldInstanceViews;
import email.com.gmail.ttsai0509.cruxer.view.WallInstanceViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class WallInstance {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonView(WallInstanceViews.Id.class)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(WallInstanceViews.Wall.class)
    private Wall wall;

    @NotNull
    @Embedded
    @JsonView(WallInstanceViews.Pose.class)
    private Pose pose;

    @NotNull
    @Embedded
    @JsonView(WallInstanceViews.Material.class)
    private Material material;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wallInstance")
    @JsonView(WallInstanceViews.HoldInstances.class)
    private List<HoldInstance> holdInstances;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(WallInstanceViews.Route.class)
    private Route route;

}

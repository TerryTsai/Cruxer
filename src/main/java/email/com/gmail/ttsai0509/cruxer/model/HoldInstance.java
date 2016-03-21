package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.view.HoldInstanceViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class HoldInstance {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonView(HoldInstanceViews.Id.class)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(HoldInstanceViews.Hold.class)
    private Hold hold;

    @NotNull
    @Embedded
    @JsonView(HoldInstanceViews.Pose.class)
    private Pose pose;

    @NotNull
    @Embedded
    @JsonView(HoldInstanceViews.Material.class)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(HoldInstanceViews.WallInstance.class)
    private WallInstance wallInstance;

}

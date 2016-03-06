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
    @JsonView(HoldInstanceViews.Location.class)
    private float px;

    @NotNull
    @JsonView(HoldInstanceViews.Location.class)
    private float py;

    @NotNull
    @JsonView(HoldInstanceViews.Location.class)
    private float pz;

    @NotNull
    @JsonView(HoldInstanceViews.Location.class)
    private float qw;

    @NotNull
    @JsonView(HoldInstanceViews.Location.class)
    private float qx;

    @NotNull
    @JsonView(HoldInstanceViews.Location.class)
    private float qy;

    @NotNull
    @JsonView(HoldInstanceViews.Location.class)
    private float qz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(HoldInstanceViews.Route.class)
    private Route route;

}

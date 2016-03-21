package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.view.PoseViews;
import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Pose {

    @JsonView(PoseViews.Pose.class)
    private float px, py, pz, rx, ry, rz, rotAngle, spin;

    public Pose() {}

    public Pose(float px, float py, float pz, float rx, float ry, float rz, float rotAngle, float spin) {
        this.px = px;
        this.py = py;
        this.pz = pz;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.rotAngle = rotAngle;
        this.spin = spin;
    }
}

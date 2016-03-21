package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.view.MaterialViews;
import email.com.gmail.ttsai0509.cruxer.view.PoseViews;
import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Material {

    @JsonView(MaterialViews.Material.class)
    private float dr, dg, db;

    public Material() {}

    public Material(float dr, float dg, float db) {
        this.dr = dr;
        this.dg = dg;
        this.db = db;
    }

}

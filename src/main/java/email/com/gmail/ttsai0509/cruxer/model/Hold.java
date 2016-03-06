package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Objects;
import email.com.gmail.ttsai0509.cruxer.view.HoldViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Hold {

    public Hold() {}

    public Hold(Account account, String model) {
        this.setAccount(account);
        this.setModel(model);
        this.setDate(new Date());
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonView(HoldViews.Id.class)
    private String id;

    @Column(nullable = false)
    @JsonView(HoldViews.Date.class)
    private Date date;

    @Column(nullable = false)
    @JsonView(HoldViews.Model.class)
    private String model;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(HoldViews.Account.class)
    private Account account;

    public boolean matches(Hold hold) {
        return Objects.equal(getId(), hold.getId())
                && Objects.equal(getModel(), hold.getModel());
    }
}

package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.cruxer.view.RouteViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Route {

    public Route() {}

    public static Route createRoute(String name, String thumbnail, Account account,
                                    List<HoldInstance> holdInstances, List<WallInstance> wallInstances
    ) {
        Route route = new Route();
        route.setName(name);
        route.setThumbnail(thumbnail);
        route.setAccount(account);
        route.setHoldInstances(holdInstances);
        route.setWallInstances(wallInstances);
        route.setDate(new Date());
        return route;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonView(RouteViews.Id.class)
    private String id;

    @Column(nullable = false)
    @JsonView(RouteViews.Date.class)
    private Date date;

    @Column(unique = true, nullable = false)
    @JsonView(RouteViews.Name.class)
    private String name;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(RouteViews.Account.class)
    private Account account;

    @Lob
    @JsonView(RouteViews.Thumbnail.class)
    private String thumbnail;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "route")
    @JsonView(RouteViews.WallInstances.class)
    private List<WallInstance> wallInstances;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "route")
    @JsonView(RouteViews.HoldInstances.class)
    private List<HoldInstance> holdInstances;

}

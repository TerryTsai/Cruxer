package email.com.gmail.ttsai0509.cruxer.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Objects;
import email.com.gmail.ttsai0509.cruxer.view.AccountViews;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class Account {
    
    public Account() {}

    public static Account createAccount(String username, String hashword,
                                        String firstname, String lastname,
                                        String email, Role... roles) {
        Account account = new Account();
        account.setUsername(username);
        account.setHashword(hashword);
        account.setFirstname(firstname);
        account.setLastname(lastname);
        account.setEmail(email);
        account.setRoles(Arrays.asList(roles));
        account.setDate(new Date());
        return account;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonView(AccountViews.Id.class)
    private String id;

    @Column(nullable = false)
    @JsonView(AccountViews.Date.class)
    private Date date;

    @Column(nullable = false)
    @JsonView(AccountViews.Info.class)
    private String firstname;

    @Column(nullable = false)
    @JsonView(AccountViews.Info.class)
    private String lastname;

    @Column(nullable = false)
    @JsonView(AccountViews.Email.class)
    private String email;

    @Column(unique = true, nullable = false)
    @JsonView(AccountViews.Username.class)
    private String username;

    @Column(nullable = false)
    private String hashword;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonView(AccountViews.Roles.class)
    private List<Role> roles;

    @Column(nullable = true)
    private String resetToken;

    @Column(nullable = true)
    private Date resetExpire;

    public boolean matches(Account account) {
        return Objects.equal(getId(), account.getId())
                && Objects.equal(getUsername(), account.getUsername())
                && Objects.equal(getFirstname(), account.getFirstname())
                && Objects.equal(getLastname(), account.getLastname())
                && Objects.equal(getEmail(), account.getEmail());
    }

    public boolean isOwnerOf(Route route) {
        return getId().equals(route.getAccount().getId());
    }

    public boolean isOwnerOf(Hold hold) {
        return getId().equals(hold.getAccount().getId());
    }

    public boolean isOwnerOf(Wall wall) {
        return getId().equals(wall.getAccount().getId());
    }

    public boolean isOwnerOf(Comment comment) {
        return getId().equals(comment.getAccount().getId());
    }

    public boolean isOwnerOf(Rating rating) {
        return getId().equals(rating.getAccount().getId());
    }

    public User toUser() {
        return new User(username, hashword, roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList()));
    }

}

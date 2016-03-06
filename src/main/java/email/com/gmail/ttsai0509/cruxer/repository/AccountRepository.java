package email.com.gmail.ttsai0509.cruxer.repository;


import email.com.gmail.ttsai0509.cruxer.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@RestResource(exported = false)
public interface AccountRepository extends JpaRepository<Account, String> {

    public Account findByUsername(String username);

}

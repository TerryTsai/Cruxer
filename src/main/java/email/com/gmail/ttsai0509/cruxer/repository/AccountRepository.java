package email.com.gmail.ttsai0509.cruxer.repository;


import email.com.gmail.ttsai0509.cruxer.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface AccountRepository extends JpaRepository<Account, String> {

    public Account findByUsername(String username);

}

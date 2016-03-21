package email.com.gmail.ttsai0509.cruxer.repository;

import email.com.gmail.ttsai0509.cruxer.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


@Repository
@RepositoryRestResource(exported = false)
public interface RatingRepository extends JpaRepository<Rating, String>{

    public Page<Rating> findByAccount(Account account, Pageable pageable);

    public Page<Rating> findByRoute(Route route, Pageable pageable);

}

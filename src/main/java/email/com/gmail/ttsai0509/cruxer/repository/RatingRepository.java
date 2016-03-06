package email.com.gmail.ttsai0509.cruxer.repository;

import email.com.gmail.ttsai0509.cruxer.model.Account;
import email.com.gmail.ttsai0509.cruxer.model.Comment;
import email.com.gmail.ttsai0509.cruxer.model.Rating;
import email.com.gmail.ttsai0509.cruxer.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


@Repository
@RestResource(exported = false)
public interface RatingRepository extends JpaRepository<Rating, String>{

    public Page<Rating> findByAccount(Account account, Pageable pageable);

    public Page<Rating> findByRoute(Route route, Pageable pageable);

}

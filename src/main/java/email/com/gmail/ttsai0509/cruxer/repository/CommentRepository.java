package email.com.gmail.ttsai0509.cruxer.repository;

import email.com.gmail.ttsai0509.cruxer.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@RestResource(exported = false)
public interface CommentRepository extends JpaRepository<Comment, String> {

    public Page<Comment> findByAccount(Account account, Pageable pageable);

    public Page<Comment> findByRoute(Route route, Pageable pageable);

    public Page<Comment> findByHold(Hold hold, Pageable pageable);

    public Page<Comment> findByWall(Wall wall, Pageable pageable);

}

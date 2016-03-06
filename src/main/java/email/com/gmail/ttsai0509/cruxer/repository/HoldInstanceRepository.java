package email.com.gmail.ttsai0509.cruxer.repository;

import email.com.gmail.ttsai0509.cruxer.model.HoldInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@RestResource(exported = false)
public interface HoldInstanceRepository extends JpaRepository<HoldInstance, String> {}

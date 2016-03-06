package email.com.gmail.ttsai0509.cruxer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class Cruxer {

    private static final Logger log = LoggerFactory.getLogger(Cruxer.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Cruxer.class);
    }

}

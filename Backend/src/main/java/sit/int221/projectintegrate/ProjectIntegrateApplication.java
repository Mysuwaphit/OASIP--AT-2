package sit.int221.projectintegrate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;

@SpringBootApplication
@ImportResource("classpath:app-config.xml")
public class ProjectIntegrateApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ProjectIntegrateApplication.class, args);

    }


}

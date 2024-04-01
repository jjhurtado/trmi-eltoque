package dev.root101.trmi_eltoque;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class App {

    public static final String TIMEZONE = "GMT-4";//havana, cuba

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
            .withZone(ZoneId.of(TIMEZONE));

    public static void main(String[] args) throws Exception {
        //local
//        System.setProperty("DB_URL", "localhost");
//        System.setProperty("DB_PORT", "5432");
//        System.setProperty("DB_NAME", "trmi-eltoque");
//        System.setProperty("DB_USERNAME", "postgres");
//        System.setProperty("DB_PASSWORD", "QWErty123**");
        SpringApplication.run(App.class, args);
    }
}

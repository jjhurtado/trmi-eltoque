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
        System.setProperty("user.timezone", TIMEZONE);

        System.setProperty("ELTOQUE_AUTH_TOKEN", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTcwMTQ0MTQwOSwianRpIjoiNWI0MWNjYzEtY2E4Yi00NmQ4LTkwNWQtNThhYzdkY2MzYzZkIiwidHlwZSI6ImFjY2VzcyIsInN1YiI6IjYzODgyYzkyZTNhYzlkNWM2NDcwOWM5YyIsIm5iZiI6MTcwMTQ0MTQwOSwiZXhwIjoxNzMyOTc3NDA5fQ.w9Cc0fLZk5txO9f8lorgP7zWJOWJUkRQ4g3WLLBnaJI");

        System.setProperty("DB_URL", "monorail.proxy.rlwy.net");
        System.setProperty("DB_PORT", "33493");
        System.setProperty("DB_NAME", "trmi-eltoque-dev");
        System.setProperty("DB_USERNAME", "postgres");
        System.setProperty("DB_PASSWORD", "g31C1G3DaeFd5eFAFEAdfABeB2cAefA4");

        //local
//        System.setProperty("DB_URL", "localhost");
//        System.setProperty("DB_PORT", "5432");
//        System.setProperty("DB_NAME", "trmi-eltoque");
//        System.setProperty("DB_USERNAME", "postgres");
//        System.setProperty("DB_PASSWORD", "QWErty123**");
        SpringApplication.run(App.class, args);
    }
}

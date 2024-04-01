package dev.root101.trmi_eltoque.feature.el_toque;

import static dev.root101.trmi_eltoque.App.DATE_FORMATTER;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Para consumir la api de 'el toque'.
 *
 * Es tipo 'protected' para que no la puedan llamar fuera del paquete, que se
 * trabaje por la interfaz.
 *
 * @author Yo
 */
@Service
class ElToqueClientImpl implements ElToqueClient {

    private final static String USD_KEY = "USD";
    private final static String EUR_KEY = "ECU";
    private final static String MLC_KEY = "MLC";

    /**
     * Toquen con el que se hacen las peticiones a la api.
     */
    @Value("${trmi.eltoque.auth_token}")
    private String elToqueToken;

    /**
     * Url de la api.
     *
     * Como tiene un solo endpoint se trabaja una unica url.
     */
    @Value("${trmi.eltoque.url}")
    private String url;

    /**
     * Key del header de la fecha inicial
     */
    @Value("${trmi.eltoque.url.header.date_from}")
    private String header_dateFrom;

    /**
     * Key del header de la fecha final
     */
    @Value("${trmi.eltoque.url.header.date_to}")
    private String header_dateTo;

    /**
     * Template para hacer las peticiones.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Hace la peticion de obtener la Tasa Representativa del Mercado Informal
     * en un rango de tiempo determinado.
     *
     * @param fromTime
     * @param toTime
     * @return
     */
    @Override
    public ElToqueDomain trmi(ZonedDateTime fromTime, ZonedDateTime toTime) {
        //inicializa los headers y la autenticacion
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(elToqueToken);

        //crea el entity de la peticion, como es un get no tiene body, solo headers
        HttpEntity entity = new HttpEntity(headers);

        //formateo las fechas de Instant a String
        String from = DATE_FORMATTER.format(fromTime);
        String to = DATE_FORMATTER.format(toTime);

        System.out.println("ElToque: consultando de %s - %s".formatted(from, to));

        //Hago la peticion y capturo la data cruda
        ResponseEntity<ElToqueResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ElToqueResponse.class,
                Map.of(
                        header_dateFrom, from,
                        header_dateTo, to
                )
        );

        //Convierto la data cruda en un objeto responsable
        return convert(response.getBody(), fromTime, toTime);
    }

    /**
     * Convierte la data cruda que viene de la api a un objeto mas manejable,
     * con tipos de datos en lugar de strings
     *
     * @param response
     * @param from
     * @param to
     * @return
     */
    private ElToqueDomain convert(ElToqueResponse response, ZonedDateTime from, ZonedDateTime to) {
        BigDecimal USD = response.getTasas().containsKey(USD_KEY)
                ? new BigDecimal(response.getTasas().get(USD_KEY))
                : null;

        BigDecimal EUR = response.getTasas().containsKey(EUR_KEY)
                ? new BigDecimal(response.getTasas().get(EUR_KEY))
                : null;

        BigDecimal MLC = response.getTasas().containsKey(MLC_KEY)
                ? new BigDecimal(response.getTasas().get(MLC_KEY))
                : null;

        return new ElToqueDomain(
                USD,
                EUR,
                MLC,
                from,
                to,
                ZonedDateTime.from(
                        DATE_FORMATTER.parse(
                                "%s %s:%s:%s".formatted(
                                        response.getDate(),
                                        String.format("%02d", response.getHour()),//agrega ceros a la izquierda en caso...
                                        String.format("%02d", response.getMinutes()),
                                        String.format("%02d", response.getSeconds())
                                )
                        )
                )
        );
    }
}

package io.microsamples.latency.client;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

}

@RestController
class RemoteChachkiesGetter {

    private RestTemplate restTemplate;
    private MeterRegistry registry;
    private AtomicLong chachkiesCount;

    @Value("${service.url:http://localhost:8081/chachkies}")
    private String serviceUrl;

    RemoteChachkiesGetter(RestTemplate restTemplate
            , MeterRegistry meterRegistry) {
        this.restTemplate = restTemplate;
        this.registry = meterRegistry;
        this.chachkiesCount = new AtomicLong(0L);
    }

    @GetMapping("/remote-chachkies")
    private ResponseEntity<List<Chachkie>> remoteChachkies() {
        final ResponseEntity<Chachkie[]> forEntity = restTemplate.getForEntity(serviceUrl
                , Chachkie[].class);
        final List<Chachkie> chachkies = Arrays.asList(forEntity.getBody());

        trackChachkies(chachkies);

        return ResponseEntity.ok(chachkies);

    }

    private void trackChachkies(List<Chachkie> chachkies) {
        final AtomicLong chachkiesGauge = registry.gauge("chachkiesServed", this.chachkiesCount);
        chachkiesGauge.addAndGet(chachkies.size());
    }
}

@Data
class Chachkie {
    private UUID id;
    private String name, description;
    private Instant when;
}
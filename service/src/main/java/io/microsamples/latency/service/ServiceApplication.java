package io.microsamples.latency.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication
public class ServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
}

@RestController
class ChachkieServer{
	private EasyRandom easyRandom = new EasyRandom();

	@GetMapping("/chachkies")
	private List<Chachkie> someChachkies(){
		return easyRandom.objects(Chachkie.class, 13).collect(Collectors.toList());
	}
}

@Value
class Chachkie {
	private UUID id;
	private String name, description;
	private Instant when;
}

@Component
@Slf4j
class ChachkiesConsumer{
	@StreamListener(Sink.INPUT)
	public void handleMessage(Message<Chachkie> message) {
		log.info("Received: {}.", message.getPayload());
	}
}
package io.microsamples.latency.client;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(Channels.class)
public class MessagingConfiguration {
}

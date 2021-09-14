package com.dcasique.reservationclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/reservations")
public class ReservationsApiGatewayRestController {

	private final static Logger logger = Logger.getLogger(ReservationsApiGatewayRestController.class.getName());

	@Autowired
	private RestTemplate restTemplate;

	public Collection<String> getReservationNameFallback() {
		logger.warning("Service /names is unavailable....");
		return new ArrayList<>();
	}
	
	public void postWriteReservation(Reservation r) {
		logger.warning("Error while trying to save reservation " + r.getReservationName());
	}

	@Autowired
	private Source source;

	@HystrixCommand(fallbackMethod = "postWriteReservation")
	@PostMapping
	public void writeReservation(@RequestBody Reservation r) {
		logger.warning("saving new Reservation Name");
		Message<String> msg = MessageBuilder.withPayload(r.getReservationName())
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.TEXT_PLAIN).build();
		this.source.output().send(msg);
	}

	@HystrixCommand(fallbackMethod = "getReservationNameFallback")
	@GetMapping(path = "/names")
	public Collection<String> getReservationNames() {
		logger.warning("Starting service /names ....");
		ParameterizedTypeReference<Resources<Reservation>> ptr = new ParameterizedTypeReference<Resources<Reservation>>() {
		};
		ResponseEntity<Resources<Reservation>> entity = this.restTemplate
				.exchange("http://localhost:9999/api/reservation-service/reservations", HttpMethod.GET, null, ptr);
		logger.warning("Done service /names ....");
		return entity.getBody().getContent().stream().map(Reservation::getReservationName).collect(Collectors.toList());
	}

}

package com.dcasique.reservationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ReservationProcessor{
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@StreamListener(Sink.INPUT)
    public void inputConsumer(@Payload final String rn) {
    
       // String rn = message.getPayload();
        this.reservationRepository.save(new Reservation(rn));
	}
        
}

/*
@MessageEndpoint
public class ReservationProcessor {
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@ServiceActivator(inputChannel = Sink.INPUT)
	public void acceptNewReservations(String rn) {
		this.reservationRepository.save(new Reservation(rn));
	}
}
*/

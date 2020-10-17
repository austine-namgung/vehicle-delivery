package com.example.delivery.messaging;


import com.example.delivery.model.ResultMessage;
import com.example.delivery.model.VehicleDelivery;
import com.example.delivery.repository.DeliveryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessagingManager {
	private final DeliveryRepository repository;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper mapper;

	private static final String DELIVERY_TOPIC = "delivery-topic";
	private static final String SAGA_RREPLY_TOPIC = "saga-reply-topic";

	@KafkaListener(topics = DELIVERY_TOPIC, groupId = "delivery")
	public void listen(String message) throws JsonMappingException, JsonProcessingException {
		log.info("===== Delivery Topic에 - Consumer 도착 ========= message: {}", message);  

		VehicleDelivery delivery  = mapper.readValue(message,new TypeReference<VehicleDelivery>(){});
		
		try{
			//////////// 장애발생 /////////
			if(true){

				log.info("==[에러발생]=== Delivery 서비스 장애 발생  ");

				throw new Exception("error 발생");
			}else{
				repository.save(delivery);
				log.info("===== Delivery 서비스  delivery_product 테이블에 저장 완료 ");
				sendToReplyTopic(message);
			}
		}catch(Exception e){			
			faileSendToReplyTopic(message);
		}
		
        
	}
    public void sendToReplyTopic(String message) throws JsonProcessingException {
		
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.setSuccessYn("Y");
		resultMessage.setTxCode("S");
		resultMessage.setServiceCode("delivery");
		resultMessage.setJasonTx(message);
		resultMessage.setMessage("정상적으로 저장되었습니다");
		String resultString = mapper.writeValueAsString(resultMessage);

		log.info("===== delivery 처리완료후  saga-reply-topic에 정상 완료 결과 전송  전송메세지: {}" , resultString);
        kafkaTemplate.send(SAGA_RREPLY_TOPIC, resultString);  

	}
	
	public void faileSendToReplyTopic(String message) throws JsonProcessingException {
		
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.setSuccessYn("N");
		resultMessage.setTxCode("S");
		resultMessage.setServiceCode("delivery");
		resultMessage.setJasonTx(message);
		resultMessage.setMessage("저장중 에러가 발생하였습니다");
		String resultString = mapper.writeValueAsString(resultMessage);

		log.info("==[에러 전송]=== SAGA_RREPLY_TOPIC 에   서비스 장애 상황 메세지 전달  전송메세지: {}", resultString);
        kafkaTemplate.send(SAGA_RREPLY_TOPIC, resultString);  

    }

    
	
	
    
}

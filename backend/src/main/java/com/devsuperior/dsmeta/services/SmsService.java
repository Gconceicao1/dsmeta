package com.devsuperior.dsmeta.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repository.SaleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

	@Autowired
	SaleRepository saleRepository;
	
	@Value("${twilio.sid}")
	private String twilioSid;

	@Value("${twilio.key}")
	private String twilioKey;

	@Value("${twilio.phone.from}")
	private String twilioPhoneFrom;

	@Value("${twilio.phone.to}")
	private String twilioPhoneTo;

	public void sendSms(Long saleId) {

		Sale sale = saleRepository.findById(saleId).get();
		
		Twilio.init(twilioSid, twilioKey);

		PhoneNumber to = new PhoneNumber(twilioPhoneTo);
		PhoneNumber from = new PhoneNumber(twilioPhoneFrom);

		StringBuilder msg = new StringBuilder();
		String date = sale.getDate().getMonthValue() + "/" + sale.getDate().getYear();
		msg.append("O Vendedor "+ sale.getSallerName() + " foi destaque em " + date);
		msg.append(" com um total de R$ " + String.format("%.2f", sale.getAmount())) ;
		
		
		Message message = Message.creator(to, from, msg.toString()).create();

		System.out.println(message.getSid());
	}
}
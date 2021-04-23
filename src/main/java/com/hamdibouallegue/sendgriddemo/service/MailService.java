package com.hamdibouallegue.sendgriddemo.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class MailService {
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	public String sendTextEmail() throws IOException {
		/*
		 * The sender email should be the same as we used to Create a Single Sender
		 * Verification
		 */
		Email from = new Email("sender email");
		String subject = "The subject";
		Email to = new Email("reciver");
		Content content = new Content("text/plain", "This is a test email");
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid("SENDGRID_API_KEY");
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			logger.info(response.getBody());
			return response.getBody();
		} catch (IOException ex) {
			throw ex;
		}
	}

	public String send() throws IOException {
		/*
		 * The sender email should be the same as we used to Create a Single Sender
		 * Verification
		 */
		Email from = new Email("sender email");
		Email to = new Email("reciver email");
		Mail mail = new Mail();
		DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
		personalization.addTo(to);
		mail.setFrom(from);
		mail.setSubject("The subject");
		// this is the dynamic value of first_name variable on our template
		//feel free to create a variable firstName  passed with the send method
		personalization.addDynamicTemplateData("first_name", "hamdi");
		mail.addPersonalization(personalization);
		mail.setTemplateId("TEMPLATE_ID");
		// feel free to save this varible on the env
		SendGrid sg = new SendGrid("SENDGRID_API_KEY");
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			logger.info(response.getBody());
			return response.getBody();
		} catch (IOException ex) {
			throw ex;
		}
	}

	// This class handel the dynamic data for the template
	// Feel free to customise this class our to putted on other file 
	private static class DynamicTemplatePersonalization extends Personalization {

		@JsonProperty(value = "dynamic_template_data")
		private Map<String, String> dynamic_template_data;

		@JsonProperty("dynamic_template_data")
		public Map<String, String> getDynamicTemplateData() {
			if (dynamic_template_data == null) {
				return Collections.<String, String>emptyMap();
			}
			return dynamic_template_data;
		}

		public void addDynamicTemplateData(String key, String value) {
			if (dynamic_template_data == null) {
				dynamic_template_data = new HashMap<String, String>();
				dynamic_template_data.put(key, value);
			} else {
				dynamic_template_data.put(key, value);
			}
		}

	}

}

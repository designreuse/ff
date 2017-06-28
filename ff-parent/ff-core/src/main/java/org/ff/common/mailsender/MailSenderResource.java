package org.ff.common.mailsender;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class MailSenderResource {

	@JsonProperty("to")
	private String to;

	@JsonProperty("cc")
	private String cc;

	@JsonProperty("subject")
	private String subject;

	@JsonProperty("text")
	private String text;

}

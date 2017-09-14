package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "config_param")
@NoArgsConstructor @Getter @Setter @ToString
public class ConfigParam {

	public enum ConfigParamName {
		mail_sender,
		gfi_sync_email_subject,
		gfi_sync_report_email_subject,
		gfi_sync_report_email_to,
		contact_email_subject,
		contact_email_to,
		sendgrid_enabled,
		sendgrid_apikey,
		test_mode
	};

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Nationalized
	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Lob
	@Nationalized
	@Column(name = "value", nullable = true)
	private String value;

	@Lob
	@Nationalized
	@Column(name = "description", nullable = true)
	private String description;

}

package org.ff.jpa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "gfi_sync_error")
@NoArgsConstructor @Getter @Setter @ToString
public class GfiSyncError {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "gfi_sync")
	private GfiSync gfiSync;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	@Lob
	@Nationalized
	@Column(name = "company_data", nullable = true)
	private String companyData;

	@Lob
	@Nationalized
	@Column(name = "error", nullable = true)
	private String error;

}

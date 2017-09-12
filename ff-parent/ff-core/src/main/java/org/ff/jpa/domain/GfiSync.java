package org.ff.jpa.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "gfi_sync")
@NoArgsConstructor @Getter @Setter @ToString
public class GfiSync {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "cnt_total")
	private Integer cntTotal;

	@Column(name = "cnt_ok")
	private Integer cntOk;

	@Column(name = "cnt_nok")
	private Integer cntNok;

	@Column(name = "start_time", nullable = false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime startTime;

	@Column(name = "end_time", nullable = false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime endTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "gfiSync", orphanRemoval = true)
	@OrderBy("id")
	private Set<GfiSyncError> errors;

}

package org.ff.jpa.envers;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@RevisionEntity(RevisionListener.class)
@Getter @Setter @ToString
public class Revision {

	@Id
	@GeneratedValue
	@RevisionNumber
	private Integer id;

	@RevisionTimestamp
	private Date creationDate;

	private String createdBy;

}

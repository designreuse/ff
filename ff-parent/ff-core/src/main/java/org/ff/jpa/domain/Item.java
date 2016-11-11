package org.ff.jpa.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.ff.jpa.AbstractEntity;
import org.hibernate.annotations.Nationalized;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@NoArgsConstructor @Getter @Setter @ToString
public class Item extends AbstractEntity {

	public enum ItemEntityType { TENDER, COMPANY };
	public enum ItemStatus { ACTIVE, INACTIVE };
	public enum ItemType { TEXT, TEXT_AREA, TEXT_EDITOR, NUMBER, DATE, DATE_TIME, RADIO, CHECKBOX, SELECT, MULTISELECT, HYPERLINK, CITY, COUNTIES, NKD, NKDS, INVESTMENTS };
	public enum ItemMetaTag { TENDER_START_DATE, TENDER_END_DATE, COMPANY_SECTOR, COMPANY_LOCATION, COMPANY_INVESTMENT, COMPANY_REVENUE };

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "code", nullable = false, length = 8)
	private String code;

	@Enumerated(EnumType.STRING)
	@Column(name = "entity_type", nullable = false, length = 16)
	private ItemEntityType entityType;

	@Column(name = "entity_id", nullable = true)
	private Integer entityId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private ItemStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 32)
	private ItemType type;

	@Enumerated(EnumType.STRING)
	@Column(name = "meta_tag", nullable = true, length = 64)
	private ItemMetaTag metaTag;

	@Column(name = "widget_item", nullable = true)
	private Boolean widgetItem;

	@Column(name = "mandatory", nullable = false)
	private Boolean mandatory;

	@Column(name = "position", nullable = false)
	private Integer position;

	@Lob
	@Nationalized
	@Column(name = "text", nullable = false)
	private String text;

	@Lob
	@Nationalized
	@Column(name = "help", nullable = true)
	private String help;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true)
	@OrderBy("position")
	private Set<ItemOption> options;

}

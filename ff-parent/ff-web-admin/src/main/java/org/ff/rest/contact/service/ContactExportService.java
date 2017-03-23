package org.ff.rest.contact.service;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.ff.base.properties.BaseProperties;
import org.ff.jpa.repository.ContactRepository;
import org.ff.rest.contact.resource.ContactResource;
import org.ff.rest.contact.resource.ContactResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

@Service
public class ContactExportService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private ContactRepository repository;

	@Autowired
	private ContactResourceAssembler resourceAssembler;

	private Locale locale;
	private DateFormat dateFormat;
	private StyleBuilder rootStyle;
	private StyleBuilder sectionStyle, labelStyle, valueStyle;
	private Integer labelWidth, verticalGap;

	@PostConstruct
	public void init() {
		locale = new Locale(baseProperties.getLocale());
		dateFormat = new SimpleDateFormat(baseProperties.getDateTimeFormat());

		PenBuilder borderPen = stl.pen().setLineWidth(new Float(0.1)).setLineColor(new Color(150, 150, 150));

		rootStyle = stl.style().setPadding(5).setFont(stl.font().setFontName("Arial"));

		sectionStyle = stl.style(rootStyle).setFontSize(10);
		labelStyle = stl.style(rootStyle).setFontSize(8).setBold(false).setBackgroundColor(new Color(240, 240, 240))
				.setBorder(borderPen).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
		valueStyle = stl.style(rootStyle).setFontSize(8).setBold(false).setBorder(borderPen);

		labelWidth = 150;
		verticalGap = 2;
	}

	public File exportPdf(Integer id) {
		ContactResource contact = resourceAssembler.toResource(repository.findOne(id), false);

		// create report
		JasperReportBuilder report = report();
		report.setTemplate(template());
		report.setPageMargin(DynamicReports.margin(30));
		report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

		// title
		MultiPageListBuilder multiPageList = cmp.multiPageList();
		report.title(multiPageList);

		processData(contact, multiPageList);

		// save report to file
		File file = null;
		try {
			file = new File("contact_" + id + ".pdf");
			report.toPdf(new FileOutputStream(file));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		return file;
	}

	private void processData(ContactResource contact, MultiPageListBuilder multiPageList) {
		HorizontalListBuilder horizontalList = cmp.horizontalList();

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.submitted", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((contact.getCreationDate() != null) ? dateFormat.format(contact.getCreationDate()) : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.companyName", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((contact.getCompanyName() != null) ? contact.getCompanyName() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.companyCode", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((contact.getCompanyCode() != null) ? contact.getCompanyCode() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.name", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((contact.getName() != null) ? contact.getName() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.email", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((contact.getEmail() != null) ? contact.getEmail() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.phone", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((contact.getPhone() != null) ? contact.getPhone() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.contactLocation", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((contact.getLocation() != null) ? contact.getLocation() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.message", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((contact.getText() != null) ? contact.getText() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		multiPageList.add(cmp.verticalGap(5),
				cmp.verticalList(cmp.text(messageSource.getMessage("report.hdr.contact", null, locale).toUpperCase()).setStyle(sectionStyle), cmp.verticalGap(0), horizontalList));
	}

}

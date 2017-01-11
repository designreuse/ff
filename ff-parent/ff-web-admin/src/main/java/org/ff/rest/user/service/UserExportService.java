package org.ff.rest.user.service;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.investment.resource.InvestmentResource;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.project.resource.ProjectItemResource;
import org.ff.rest.project.resource.ProjectResource;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.resource.UserResourceAssembler;
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
public class UserExportService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserResourceAssembler resourceAssembler;

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
		UserResource user = resourceAssembler.toResource(repository.findOne(id), false);

		// create report
		JasperReportBuilder report = report();
		report.setTemplate(template());
		report.setPageMargin(DynamicReports.margin(30));
		report.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);

		// title
		MultiPageListBuilder multiPageList = cmp.multiPageList();
		report.title(multiPageList);

		processUserData(user, multiPageList);
		processCompanyData(user.getCompany(), multiPageList);
		processProjectData(user, multiPageList);

		// save report to file
		File file = null;
		try {
			file = new File("user_" + id + ".pdf");
			report.toPdf(new FileOutputStream(file));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		return file;
	}

	private void processUserData(UserResource user, MultiPageListBuilder multiPageList) {
		HorizontalListBuilder horizontalList = cmp.horizontalList();

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.firstName", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((user.getFirstName() != null) ? user.getFirstName() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.lastName", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((user.getLastName() != null) ? user.getLastName() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.email", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((user.getEmail() != null) ? user.getEmail() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.registrationType", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((user.getRegistrationType() == UserRegistrationType.INTERNAL) ? messageSource.getMessage("report.lbl.registrationTypeInternal", null, locale) : messageSource.getMessage("report.lbl.registrationTypeExternal", null, locale)).setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.registered", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((user.getCreationDate() != null) ? dateFormat.format(user.getCreationDate()) : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.lastLogin", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((user.getLastLoginDate() != null) ? dateFormat.format(user.getLastLoginDate()) : "").setStyle(valueStyle))
		.newRow(verticalGap);

		multiPageList.add(cmp.verticalGap(5),
				cmp.verticalList(cmp.text(messageSource.getMessage("report.hdr.userData", null, locale).toUpperCase()).setStyle(sectionStyle), cmp.verticalGap(0), horizontalList));
	}

	private void processCompanyData(CompanyResource company, MultiPageListBuilder multiPageList) {
		HorizontalListBuilder horizontalList = cmp.horizontalList();

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.companyName", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((company.getName() != null) ? company.getName() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		horizontalList.add(
				cmp.text(messageSource.getMessage("report.lbl.companyCode", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((company.getCode() != null) ? company.getCode() : "").setStyle(valueStyle))
		.newRow(verticalGap);

		for (ItemResource item : company.getItems()) {
			if (item.getMetaTag() != null && ProjectResourceAssembler.getCompanyInvestmentMetaTags().contains(item.getMetaTag())) {
				continue;
			}

			horizontalList.add(
					cmp.text(item.getText()).setFixedWidth(labelWidth).setStyle(labelStyle),
					cmp.text((item.getValueMapped() != null) ? item.getValueMapped() : "").setStyle(valueStyle))
			.newRow(verticalGap);
		}

		multiPageList.add(cmp.verticalGap(5),
				cmp.verticalList(cmp.text(messageSource.getMessage("report.hdr.companyData", null, locale).toUpperCase()).setStyle(sectionStyle), cmp.verticalGap(0), horizontalList));
	}

	private void processProjectData(UserResource user, MultiPageListBuilder multiPageList) {
		HorizontalListBuilder horizontalList = cmp.horizontalList();

		for (ProjectResource project : user.getProjects()) {
			horizontalList.add(
					cmp.text(project.getName()).setStyle(stl.style(labelStyle).setBackgroundColor(new Color(220, 220, 220)).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)))
			.newRow(0);

			List<String> lst = new ArrayList<>();
			for (InvestmentResource investment : project.getInvestments()) {
				lst.add(investment.getName());
			}

			horizontalList.add(
					cmp.text(messageSource.getMessage("report.lbl.investment", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
					cmp.text(StringUtils.join(lst, ", ")).setStyle(valueStyle))
			.newRow(0);

			horizontalList.add(
					cmp.text(messageSource.getMessage("report.lbl.description", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
					cmp.text((project.getDescription() != null) ? project.getDescription() : "").setStyle(valueStyle))
			.newRow(0);

			for (ProjectItemResource projectItem : project.getItems()) {
				horizontalList.add(
						cmp.text(projectItem.getItem().getText()).setFixedWidth(labelWidth).setStyle(labelStyle),
						cmp.text((projectItem.getValueMapped() != null) ? projectItem.getValueMapped() : "").setStyle(valueStyle))
				.newRow(0);
			}

			horizontalList.newRow(verticalGap * 3);
		}

		multiPageList.add(cmp.verticalGap(5),
				cmp.verticalList(cmp.text(messageSource.getMessage("report.hdr.projects", null, locale).toUpperCase()).setStyle(sectionStyle), cmp.verticalGap(0), horizontalList));
	}

}

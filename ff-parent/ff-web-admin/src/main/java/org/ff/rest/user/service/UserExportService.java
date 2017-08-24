package org.ff.rest.user.service;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.jpa.domain.Item.ItemMetaTag;
import org.ff.jpa.domain.Subdivision2;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.Subdivision2Repository;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.company.resource.CompanyResource;
import org.ff.rest.investment.resource.InvestmentResource;
import org.ff.rest.item.resource.ItemResource;
import org.ff.rest.project.resource.ProjectItemResource;
import org.ff.rest.project.resource.ProjectResource;
import org.ff.rest.project.resource.ProjectResourceAssembler;
import org.ff.rest.tender.resource.TenderResource;
import org.ff.rest.user.resource.UserResource;
import org.ff.rest.user.resource.UserResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.style.PenBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

@Slf4j
@Service
public class UserExportService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserResourceAssembler resourceAssembler;

	@Autowired
	private Subdivision2Repository subdivision2Repository;

	private DecimalFormat currencyFormatter;
	private Locale locale;
	private DateFormat dateFormat;
	private StyleBuilder rootStyle;
	private StyleBuilder sectionStyle, labelStyle, valueStyle;
	private Integer labelWidth, verticalGap;

	@PostConstruct
	public void init() {
		String pattern = "###,###.##";
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale(baseProperties.getLocale()));
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');

		currencyFormatter = new DecimalFormat(pattern, dfs);
		currencyFormatter.setMinimumFractionDigits(2);
		currencyFormatter.setMaximumFractionDigits(2);

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
				cmp.text(messageSource.getMessage("report.lbl.email2", null, locale)).setFixedWidth(labelWidth).setStyle(labelStyle),
				cmp.text((user.getEmail2() != null) ? user.getEmail2() : "").setStyle(valueStyle))
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

	public File exportCompanyData2Csv(Locale locale) {
		long start = System.currentTimeMillis();

		File file = null;
		CSVWriter writer = null;
		try {
			file = new File(String.format("company_data_%s.csv", System.currentTimeMillis()));
			writer = new CSVWriter(new FileWriter(file), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER);

			List<String[]> entries = new ArrayList<>();
			String[] headers = new String[] { "ID korisnika", "Status korisnika", "Tip registracije", "Naziv", "OIB", "Županija sjedišta", "Općina sjedišta", "Veličina", "Visina prihoda", "Broj projekata", "Vrijednost projekata (HRK)", "Zadnja prijava", "VPO", "VPO (zamjena)" };
			entries.add(headers);

			for (User user : repository.findAll()) {
				if (user.getDemoUser() == Boolean.TRUE) {
					continue;
				}

				UserResource userResource = resourceAssembler.toResource(user, false);
				CompanyResource companyResource = userResource.getCompany();

				String[] entry = new String[15];

				entry[0] = user.getId().toString();
				entry[1] = getUserStatus(user.getStatus(), locale);
				entry[2] = getRegistrationType(user.getRegistrationType(), locale);
				entry[3] = StringUtils.isNotBlank(companyResource.getName()) ? companyResource.getName() : "";
				entry[4] = StringUtils.isNotBlank(companyResource.getCode()) ? companyResource.getCode() : "";

				Subdivision2 subdivision2 = subdivision2Repository.findByName(getCompanyItemValue(companyResource, ItemMetaTag.COMPANY_LOCATION));
				entry[5] = (subdivision2 != null) ? subdivision2.getSubdivision1().getName() : "";
				entry[6] = (subdivision2 != null) ? subdivision2.getName() : "";

				entry[7] = getCompanyItemValue(companyResource, ItemMetaTag.COMPANY_SIZE);
				entry[8] = getCompanyItemValue(companyResource, ItemMetaTag.COMPANY_REVENUE);

				int cntProjects = 0;
				double totalInvestmentAmount = 0;
				for (ProjectResource projectResource : userResource.getProjects()) {
					cntProjects++;
					for (ProjectItemResource projectItemResource : projectResource.getItems()) {
						if (projectItemResource.getItem().getMetaTag() == ItemMetaTag.COMPANY_INVESTMENT_AMOUNT) {
							if (projectItemResource.getValue() != null) {
								totalInvestmentAmount = totalInvestmentAmount + (double) projectItemResource.getValue();
							}
						}
					}
				}

				entry[9] = Integer.toString(cntProjects);
				entry[10] = currencyFormatter.format(totalInvestmentAmount);

				entry[11] = (user.getLastLoginDate() != null) ? dateFormat.format(user.getLastLoginDate().toDate()) : "";
				entry[12] = (user.getBusinessRelationshipManager() != null) ? user.getBusinessRelationshipManager().getLastName() + " " + user.getBusinessRelationshipManager().getFirstName() : "";
				entry[13] = (user.getBusinessRelationshipManagerSubstitute() != null) ? user.getBusinessRelationshipManagerSubstitute().getLastName() + " " + user.getBusinessRelationshipManagerSubstitute().getFirstName() : "";

				entries.add(entry);
			}

			writer.writeAll(entries);

			return file;
		} catch (Exception e) {
			FileUtils.deleteQuietly(file);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(writer);
			log.debug("Exporting company data to CSV finished in {} ms.", System.currentTimeMillis() - start);
		}
	}

	public File exportProjectData2Csv(Locale locale) {
		long start = System.currentTimeMillis();

		File file = null;
		CSVWriter writer = null;
		try {
			file = new File(String.format("project_data_%s.csv", System.currentTimeMillis()));
			writer = new CSVWriter(new FileWriter(file), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER);

			List<String[]> entries = new ArrayList<>();
			String[] headers = new String[] { "ID korisnika", "Status korisnika", "Tip registracije", "Naziv poduzeća", "OIB poduzeća", "Naziv", "Djelatnost", "Investicije", "Županija projekta", "Općina projekta", "Iznos (HRK)", "Način financiranja", "Prihvatljivi natječaji" };
			entries.add(headers);

			for (User user : repository.findAll()) {
				if (user.getDemoUser() == Boolean.TRUE) {
					continue;
				}

				UserResource userResource = userService.find(user.getId(), locale);

				for (ProjectResource projectResource : userResource.getProjects()) {
					String[] entry = new String[14];

					entry[0] = user.getId().toString();
					entry[1] = getUserStatus(user.getStatus(), locale);
					entry[2] = getRegistrationType(user.getRegistrationType(), locale);
					entry[3] = StringUtils.isNotBlank(userResource.getCompany().getName()) ? userResource.getCompany().getName() : "";
					entry[4] = StringUtils.isNotBlank(userResource.getCompany().getCode()) ? userResource.getCompany().getCode() : "";

					entry[5] = StringUtils.isNotBlank(projectResource.getName()) ? projectResource.getName() : "";
					entry[6] = getProjectItemValue(projectResource, ItemMetaTag.COMPANY_INVESTMENT_ACTIVITY);

					List<String> investments = new ArrayList<>();
					for (InvestmentResource investmentResource : projectResource.getInvestments()) {
						investments.add(investmentResource.getName());
					}
					entry[7] = StringUtils.join(investments, "|");

					entry[8] = getProjectItemValue(projectResource, ItemMetaTag.COMPANY_INVESTMENT_SUBDIVISION1);
					entry[9] = getProjectItemValue(projectResource, ItemMetaTag.COMPANY_INVESTMENT_SUBDIVISION2);

					String investmentAmount = getProjectItemValue(projectResource, ItemMetaTag.COMPANY_INVESTMENT_AMOUNT);
					entry[10] = (StringUtils.isNotBlank(investmentAmount)) ? currencyFormatter.format(Double.parseDouble(investmentAmount)) : "";

					entry[11] = getProjectItemValue(projectResource, ItemMetaTag.COMPANY_INVESTMENT_FINANCING_TYPE);

					List<String> mathcingTenders = new ArrayList<>();
					if (projectResource.getMatchingTenders() != null) {
						for (TenderResource tenderResource : projectResource.getMatchingTenders()) {
							mathcingTenders.add(tenderResource.getName());
						}
					}
					entry[12] = StringUtils.join(mathcingTenders, "|");

					entries.add(entry);
				}
			}

			writer.writeAll(entries);

			return file;
		} catch (Exception e) {
			FileUtils.deleteQuietly(file);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(writer);
			log.debug("Exporting project data to CSV finished in {} ms.", System.currentTimeMillis() - start);
		}
	}

	private String getCompanyItemValue(CompanyResource companyResource, ItemMetaTag metaTag) {
		String result = "";
		for (ItemResource itemResource : companyResource.getItems()) {
			if (itemResource.getMetaTag() == metaTag) {
				result = itemResource.getValueMapped();
			}

		}
		return result;
	}

	private String getProjectItemValue(ProjectResource projectResource, ItemMetaTag metaTag) {
		String result = "";
		for (ProjectItemResource projectItemResource : projectResource.getItems()) {
			if (projectItemResource.getItem().getMetaTag() == metaTag) {
				if (metaTag == ItemMetaTag.COMPANY_INVESTMENT_AMOUNT) {
					result = (projectItemResource.getValue() != null) ? projectItemResource.getValue().toString() : "";
				} else {
					result = projectItemResource.getValueMapped();
				}
			}

		}
		return result;
	}

	private String getUserStatus(UserStatus status, Locale locale) {
		if (status == UserStatus.ACTIVE) {
			return messageSource.getMessage("export.userStatusActive", null, locale);
		} else if (status == UserStatus.INACTIVE) {
			return messageSource.getMessage("export.userStatusInactive", null, locale);
		} else if (status == UserStatus.WAITING_CONFIRMATION) {
			return messageSource.getMessage("export.userStatusWaitingConfirmation", null, locale);
		} else {
			return "";
		}
	}

	private String getRegistrationType(UserRegistrationType registrationType, Locale locale) {
		if (registrationType == UserRegistrationType.INTERNAL) {
			return messageSource.getMessage("export.registrationTypeInternal", null, locale);
		} else if (registrationType == UserRegistrationType.EXTERNAL) {
			return messageSource.getMessage("export.registrationTypeExternal", null, locale);
		} else {
			return "";
		}
	}

}
package org.ff.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.ff.base.properties.BaseProperties;
import org.ff.common.security.AppUser.AppUserRole;
import org.ff.jpa.domain.Company;
import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.domain.User.UserStatus;
import org.ff.jpa.repository.CompanyRepository;
import org.ff.jpa.repository.UserRepository;
import org.ff.zaba.session.CompanyDataResource;
import org.ff.zaba.session.SessionClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityFilter implements Filter {

	@Value("${base.url}")
	private String baseUrl;

	@Autowired
	private SessionClient sessionClient;

	@Autowired
	private BaseProperties baseProperties;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	private RestTemplate restTemplate;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
			log.trace("User [{}] already authenticated", context.getAuthentication().getName());
		} else {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			// check if request contains 'authId' parameter - indicates that external flow (e.g. e-ZaBa) is used
			String authId = httpRequest.getParameter("authId");

			try {
				if (StringUtils.isNotBlank(authId)) {
					log.debug("Initiating external flow...");

					// get external user authorization data from SESSION TRANSFER SERVICE
					Map<String, String> data = sessionClient.checkAuthId(authId);
					if (data.containsKey("matbr")) {
						String companyNumber = null;
						String branchOfficeNumber = null;

						String matbr = data.get("matbr");
						if (matbr.contains("-")) {
							String[] array = matbr.split("\\-");
							companyNumber = array[0];
							branchOfficeNumber = array[1];
						} else {
							companyNumber = matbr;
							branchOfficeNumber = "";
						}

						// get company data from external source (e.g. from ZaBa) via ReST API
						log.debug("Getting company data from external source...");
						ResponseEntity<CompanyDataResource> responseEntity = restTemplate.exchange(baseProperties.getZabaApiUrl(),
								HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), CompanyDataResource.class, getUriVariables(companyNumber, branchOfficeNumber));

						CompanyDataResource companyData = responseEntity.getBody();
						log.debug("Company data: {}", companyData);

						// check if company is already registered in FundFinder
						Company company = companyRepository.findByCode(companyData.getOibNumber());
						User user = null;

						if (company == null) {
							user = new User();
							user.setStatus(UserStatus.ACTIVE);
							user.setFirstName("unknown");
							user.setLastName("unknown");
							user.setEmail(companyData.getOibNumber() + "@fundfinder.hr");
							user.setPassword(new MessageDigestPasswordEncoder("SHA-1").encodePassword("unknown", null));
							user.setLastLoginDate(new DateTime());
							user.setDemoUser(Boolean.FALSE);
							user.setRegistrationType(UserRegistrationType.EXTERNAL);
							userRepository.save(user);

							log.debug("New user [{}] created", user.getId());

							company = new Company();
							company.setUser(user);
							company.setName(companyData.getSubjectName());
							company.setCode(companyData.getOibNumber());
							companyRepository.save(company);

							log.debug("New company [{}] created", company.getId());
						} else {
							user = company.getUser();
						}

						SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), null, getGrantedAuthority()));
					}
				}
			} catch (Exception e) {
				log.error("External flow failed", e);
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.trace("Initializing [{}]", this.getClass().getSimpleName());
	}

	@Override
	public void destroy() {
		log.trace("Destroying [{}]", this.getClass().getSimpleName());
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private Map<String, String> getUriVariables(String companyNumber, String branchOfficeNumber) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("companyNumber", companyNumber);
		uriVariables.put("branchOfficeNumber", branchOfficeNumber);
		return uriVariables;
	}

	private List<GrantedAuthority> getGrantedAuthority() {
		return AuthorityUtils.createAuthorityList(AppUserRole.ROLE_USER.name());
	}

}

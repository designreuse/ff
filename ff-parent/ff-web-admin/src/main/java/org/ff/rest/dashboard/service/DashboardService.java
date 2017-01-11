package org.ff.rest.dashboard.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.ff.jpa.domain.User;
import org.ff.jpa.domain.User.UserRegistrationType;
import org.ff.jpa.repository.UserRepository;
import org.ff.rest.dashboard.resource.DashboardResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

	@Autowired
	private UserRepository userRepository;

	public DashboardResource getData() {
		DashboardResource resource = new DashboardResource();

		int usersRegistered = 0;
		int usersRegisteredInternal = 0;
		int usersRegisteredExternal = 0;

		for (User user : userRepository.findAll()) {
			if (Boolean.TRUE == user.getDemoUser()) {
				continue;
			}
			if (user.getRegistrationType() != null) {
				usersRegistered++;
				if (user.getRegistrationType() == UserRegistrationType.INTERNAL) {
					usersRegisteredInternal++;
				} else if (user.getRegistrationType() == UserRegistrationType.EXTERNAL) {
					usersRegisteredExternal++;
				}
			}
		}

		resource.setUsersRegistered(usersRegistered);
		resource.setUsersRegisteredInternal(usersRegisteredInternal);
		resource.setUsersRegisteredExternal(usersRegisteredExternal);

		double usersRegisteredInternalPercentage = (new Double(usersRegisteredInternal) / new Double(usersRegistered)) * 100;
		double usersRegisteredExternalPercentage = (new Double(usersRegisteredExternal) / new Double(usersRegistered)) * 100;

		if (usersRegisteredInternalPercentage > 0) {
			resource.setUsersRegisteredInternalPercentage(new BigDecimal(usersRegisteredInternalPercentage).setScale(2, RoundingMode.HALF_UP).toString() + "%");
		}
		if (usersRegisteredExternalPercentage > 0) {
			resource.setUsersRegisteredExternalPercentage(new BigDecimal(usersRegisteredExternalPercentage).setScale(2, RoundingMode.HALF_UP).toString() + "%");
		}

		return resource;
	}

}

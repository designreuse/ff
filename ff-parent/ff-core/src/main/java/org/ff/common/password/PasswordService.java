package org.ff.common.password;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

	private List<CharacterRule> rules;

	private PasswordGenerator generator;

	@PostConstruct
	public void init() {
		rules = Arrays.asList(
				// at least one upper-case character
				new CharacterRule(EnglishCharacterData.UpperCase, 2),

				// at least one lower-case character
				new CharacterRule(EnglishCharacterData.LowerCase, 2),

				// at least one digit character
				new CharacterRule(EnglishCharacterData.Digit, 2));

		generator = new PasswordGenerator();
	}

	public String generate() {
		return generator.generatePassword(12, rules);
	}

	public static String encodePassword(String password) {
		//		return new MessageDigestPasswordEncoder("SHA-1").encodePassword(password, null);
		return password;
	}

}

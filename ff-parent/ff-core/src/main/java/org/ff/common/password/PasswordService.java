package org.ff.common.password;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

	private final static String ENCRYPTION_PASSWORD = "#!dJ_bOb0_tRiBuTe!#";

	private List<CharacterRule> rules;

	private PasswordGenerator generator;

	private static BasicTextEncryptor encryptor;

	static {
		encryptor = new BasicTextEncryptor();
		encryptor.setPassword(ENCRYPTION_PASSWORD);
	}

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

	public static String encryptPassword(String password) {
		return (StringUtils.isNotBlank(password)) ? encryptor.encrypt(password) : password;
	}

	public static String decryptPassword(String password) {
		return (StringUtils.isNotBlank(password)) ? encryptor.decrypt(password) : password;
	}

}

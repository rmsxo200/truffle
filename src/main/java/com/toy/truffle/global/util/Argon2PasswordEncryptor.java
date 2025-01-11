package com.toy.truffle.global.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Component;

@Component
public class Argon2PasswordEncryptor {

	private final Argon2 argon2 = Argon2Factory.create();

	// 비밀번호 암호화
	public String encrypt(String password) {
		char[] passwordChars = password.toCharArray();
		try {
			return argon2.hash(10, 65536, 1, passwordChars);
		} finally {
			argon2.wipeArray(passwordChars);
		}
	}

	// 비밀번호 검증
	public boolean verify(String hashedPassword, String password) {
		char[] passwordChars = password.toCharArray();
		try {
			return argon2.verify(hashedPassword, passwordChars);
		} finally {
			argon2.wipeArray(passwordChars);
		}
	}
}
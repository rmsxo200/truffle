package com.toy.truffle.user.dto;

import com.toy.truffle.user.entity.User;
import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(toBuilder = true)
public class SignUpDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -2459221140206834804L;

	private String email;
	private String userName;
	private String password;

	public SignUpDTO(String email, String userName, String password) {
		this.email = email;
		this.userName = userName;
		this.password = password;
	}

	public SignUpDTO() {

	}

	public User toEntity() {
		return User.builder()
			.email(email)
			.userName(userName)
			.password(password)
			.build();
	}
}

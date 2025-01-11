package com.toy.truffle.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "user_table")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userSeq;

	@Column(unique = true, length = 100, nullable = false)
	private String email;

	@Column(length = 8, nullable = false)
	private String userName;

	@Column(nullable = false)
	private String password;

	// TODO : 가입일
	
	// TODO : 정보 수정일

	// TODO : 비밀번호 실패 횟수

	// TODO : 계정 잠금 시간

	@Builder
	public User(Long userSeq, String email, String userName, String password) {
		this.userSeq = userSeq;
		this.email = email;
		this.userName = userName;
		this.password = password;
	}
}


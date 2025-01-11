package com.toy.truffle.user.repository;

import com.toy.truffle.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	// 사용자 중복 이메일 조회
	Optional<User> findByEmail(String email);
}

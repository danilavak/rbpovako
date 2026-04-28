package ru.vako.rbpovako.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vako.rbpovako.model.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByRefreshTokenId(String refreshTokenId);

    List<UserSession> findAllByUserAccountUsernameOrderByIdAsc(String username);
}

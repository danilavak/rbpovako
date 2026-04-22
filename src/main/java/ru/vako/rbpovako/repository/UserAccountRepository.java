package ru.vako.rbpovako.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.vako.rbpovako.model.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

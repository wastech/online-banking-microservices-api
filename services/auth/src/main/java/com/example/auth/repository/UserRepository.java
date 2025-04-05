package com.example.auth.repository;

import com.example.auth.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable(value = "users", key = "#username")
    Optional<User> findByUserName(String username);

    @Cacheable(value = "users", key = "'exists_' + #username")
    Boolean existsByUserName(String username);

    @Cacheable(value = "users", key = "'email_' + #email")
    Boolean existsByEmail(String email);

    @Override
    @Cacheable(value = "users", key = "#id")
    Optional<User> findById(Long id);

    @Override
    @CacheEvict(value = {"users", "userDetails"}, allEntries = true)
    <S extends User> S save(S entity);
}
package com.loanpick.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loanpick.profile.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}

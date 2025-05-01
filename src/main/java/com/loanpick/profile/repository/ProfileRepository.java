package com.loanpick.profile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loanpick.profile.entity.Profile;
import com.loanpick.user.entity.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    List<Profile> findByUser(User user);
}

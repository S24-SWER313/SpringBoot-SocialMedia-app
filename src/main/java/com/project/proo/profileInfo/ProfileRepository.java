package com.project.proo.profileInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    // Add custom query methods if needed, such as finding a profile by user ID
    // For example:
    // Optional<Profile> findByUserId(Integer userId);
}


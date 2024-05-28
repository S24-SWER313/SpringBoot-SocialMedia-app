package com.project.proo.profileInfo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
   
    List<Profile> findAll();
    
    Optional <Profile> findByUserId(Integer userId);
}

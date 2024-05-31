package com.project.proo.Hashtagee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.proo.postInfo.Post;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
    Optional<Hashtag> findByName(String hashtagName);
    List<Hashtag> findByNameContaining(String hashtag);
    List<Hashtag> findByNameContainingIgnoreCase(String hashtag);
}


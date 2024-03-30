package com.project.proo.postInfo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyFileRepository extends JpaRepository<MyFile, Integer> {

      Optional<List<MyFile>> findByPostId(Integer postId);

}

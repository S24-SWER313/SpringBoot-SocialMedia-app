package com.project.proo.usreInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.project.proo.postInfo.Post;


public interface UserRepository  extends JpaRepository<User, Integer> {

  
}

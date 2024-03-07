package com.project.proo.postInfo;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserModelAssembler;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userid}")
public class PostController {
    
  private final PostRepository postRepository;
  private final PostModelAssembler assembler;
  private final UserRepository UserRepository;
  private final UserModelAssembler UserModelAssembler;
public PostController(PostRepository postRepository,PostModelAssembler assembler,UserRepository UserRepository,UserModelAssembler UserModelAssembler) {
    this.postRepository = postRepository;
      this.assembler=assembler;
      this.UserRepository=UserRepository;
      this.UserModelAssembler=UserModelAssembler;
}

@GetMapping("/posts/{postid}")
@Transactional
public
EntityModel<Post> one(@PathVariable("userid") Integer userid, @PathVariable("postid") Integer postid) {

    Post post = postRepository.findById(postid) //
        .orElseThrow(() -> new PostNotFoundException(postid));
  
    return assembler.toModel(post);
}



@GetMapping("/posts")
public CollectionModel<EntityModel<Post>> all(@PathVariable("userid") Integer userId) {
    List<EntityModel<Post>> posts = postRepository.findByUserId(userId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(posts, linkTo(methodOn(PostController.class).all(userId)).withSelfRel());
}



@PostMapping("/posts")
   public ResponseEntity<?> addPosts(@PathVariable Integer userid, @RequestBody Post newPost) {
        User user = UserRepository.findById(userid)
                .orElseThrow(() -> new UserNotFoundException(userid));
    
                newPost.setUser(user);
    
        Post savedPost = postRepository.save(newPost);
        EntityModel<Post> entityModel = assembler.toModel(savedPost);
    
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


@PutMapping("/posts/{postId}") // Specify the postId in the mapping
public ResponseEntity<?> editPost(@PathVariable Integer postId, @RequestBody Post updatedPost) {
    Post post = postRepository.findById(postId) // Find post by postId, not userId
            .orElseThrow(() -> new PostNotFoundException(postId));

    post.setCaption(updatedPost.getCaption());
    post.setAudiance(updatedPost.getAudiance()); 

    Post savedPost = postRepository.save(post);
    EntityModel<Post> entityModel = assembler.toModel(savedPost);

    return ResponseEntity.ok(entityModel);
}
@DeleteMapping("/posts/{postId}")
public ResponseEntity<?> deletePost(@PathVariable("userid") Integer userid, @PathVariable("postId") Integer postid) {
    // Check if the post exists
    if (!postRepository.existsById(postid)) {
        throw new PostNotFoundException(postid);
    }
    // Delete the post
    postRepository.deleteById(postid);
    // Return a response indicating successful deletion
    return ResponseEntity.noContent().build();
}

@PostMapping("/shared-posts")
public ResponseEntity<?> addsharePost(@PathVariable Integer userid, @RequestParam Integer postId) {
    User user = UserRepository.findById(userid)
            .orElseThrow(() -> new UserNotFoundException(userid));

    Post post =postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException(postId) );

    if (!user.getSharedPosts().contains(post)) {
        user.getSharedPosts().add(post);
      
}
    UserRepository.save(user);

   URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{postId}")
            .buildAndExpand(post.getId())
            .toUri();

    return ResponseEntity.created(location).build();
}


@GetMapping("/shared-posts")
public CollectionModel<EntityModel<Post>> getSharedPosts(@PathVariable("userid") Integer userid) {
    
    List<EntityModel<Post>> sharedPosts = postRepository.findByUser_SharedPosts_User_Id(userid).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(sharedPosts,
            linkTo(methodOn(PostController.class).getSharedPosts(userid)).withSelfRel());
}
    

}

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
import jakarta.validation.Valid;

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
   public ResponseEntity<?> addPosts(@PathVariable Integer userid,@Valid @RequestBody Post newPost) {
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
public ResponseEntity<?> editPost(@PathVariable Integer postId,@Valid @RequestBody Post updatedPost) {
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
    Post originalPost = postRepository.findById(postid)
    .orElseThrow(() -> new PostNotFoundException(postid));

   List<Post> sharedPosts = originalPost.getSharedPosts();
    for (Post sharedPost : sharedPosts) {
        postRepository.deleteById(sharedPost.getId());
    }
    // Delete the post
    postRepository.deleteById(postid);
    // Return a response indicating successful deletion
    return ResponseEntity.noContent().build();
}




@PostMapping("/shared-posts")
public ResponseEntity<?> addsharePost(@PathVariable Integer userid, @RequestParam Integer postId) {
    // Retrieve the user who is sharing the post
    User sharingUser = UserRepository.findById(userid)
            .orElseThrow(() -> new UserNotFoundException(userid));

    // Retrieve the post to be shared
    Post originalPost = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

    // Create a new post instance for sharing
    Post sharedPost = new Post(originalPost.getAudiance(), originalPost.getDate(), originalPost.getCaption());
    sharedPost.setUser(sharingUser);
    sharedPost.setOriginalPost(originalPost);
    sharedPost.setShared(true);

    
    // Save the shared post
    Post savedSharedPost = postRepository.save(sharedPost);

    // Add the shared post to the original user's shared posts list
  // originalPost.getSharedPosts().add(savedSharedPost);
    UserRepository.save(sharingUser);

    // Return the URI of the shared post
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{postId}")
            .buildAndExpand(savedSharedPost.getId())
            .toUri();

    return ResponseEntity.created(location).build();
}



@GetMapping("/shared-posts")
public CollectionModel<EntityModel<Post>> getSharedPosts(@PathVariable("userid") Integer userid) {
    User User = UserRepository.findById(userid)
    .orElseThrow(() -> new UserNotFoundException(userid));
   
    
    List<EntityModel<Post>> sharedPosts = User.getPosts().stream() .filter(Post::isShared)
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(sharedPosts,
            linkTo(methodOn(PostController.class).getSharedPosts(userid)).withSelfRel());
}
    

}
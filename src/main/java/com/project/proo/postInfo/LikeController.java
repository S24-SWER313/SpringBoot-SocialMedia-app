package com.project.proo.postInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin
@RequestMapping("/posts/{postId}")
public class LikeController {
    @Autowired
   private CommentRepository commentRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
   private CommentLikeAssembler clikeAssembler;
   @Autowired
   private PostLikeAssembler plikeAssembler;
   @Autowired
   private PostRepository PostRepository;
   @Autowired
   private UserRepository userRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;

@GetMapping("/comments/{commentId}/likes")
public CollectionModel<EntityModel<CommentLike>> getAllLikesForComment(@PathVariable Integer postId,@PathVariable Integer commentId) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));
            Post post = PostRepository.findById(postId)
            .orElseThrow(() -> new CommentNotFoundException(postId));
    comment.setPost(post);
    List<CommentLike> commentLikes = comment.getLikes();
    List<EntityModel<CommentLike>> likeEntities = commentLikes.stream()
            .map(clikeAssembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(likeEntities,
            linkTo(methodOn(LikeController.class).getAllLikesForComment(postId,commentId)).withSelfRel());
}

@GetMapping("/likes")
public CollectionModel<EntityModel<PostLike>> getAllLikesForPost(@PathVariable Integer postId) {
    Post post = PostRepository.findById(postId)
            .orElseThrow(() -> new CommentNotFoundException(postId));

    List<PostLike> postLikes = post.getLikers();
    List<EntityModel<PostLike>> likeEntities = postLikes.stream()
            .map(plikeAssembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(likeEntities,
            linkTo(methodOn(LikeController.class).getAllLikesForPost(postId)).withSelfRel());
}

@PostMapping("/comments/{commentId}/users/{userid}/likes")
public ResponseEntity<?> addLikeForComment(@PathVariable Integer userid,@PathVariable Integer commentId) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));
     User user = userRepository.findById(userid)
                .orElseThrow(() -> new UserNotFoundException(userid));
    // Create a new CommentLike
    CommentLike newCommentLike = new CommentLike();
    newCommentLike.setLiked(true);
    newCommentLike.setComment(comment);
    newCommentLike.setUser(user);

    // Save the CommentLike
    CommentLike savedCommentLike = commentLikeRepository.save(newCommentLike);

    // Convert to EntityModel
    EntityModel<CommentLike> entityModel = clikeAssembler.toModel(savedCommentLike);

    // Respond with the created Like and a link to the self endpoint
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
}

@PostMapping("/users/{userid}/likes")
public ResponseEntity<?> addLikeForPosts(@PathVariable Integer userid,@PathVariable Integer postId) {
    Post post = PostRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
     User user = userRepository.findById(userid)
                .orElseThrow(() -> new UserNotFoundException(userid));
    // Create a new CommentLike
    PostLike newPostLike = new PostLike();
    newPostLike.setLiked(true);
    newPostLike.setPost(post);
    newPostLike.setUser(user);

    // Save the CommentLike
    PostLike savedPostLike = postLikeRepository.save(newPostLike);

    // Convert to EntityModel
    EntityModel<PostLike> entityModel = plikeAssembler.toModel(savedPostLike);

    // Respond with the created Like and a link to the self endpoint
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
}
// @PostMapping("/users/{userId}/likes")
// public ResponseEntity<?> addLikeForPosts(@PathVariable Integer userId, @PathVariable Integer postId) {
//     Post post = PostRepository.findById(postId)
//             .orElseThrow(() -> new PostNotFoundException(postId));
//     User user = userRepository.findById(userId)
//             .orElseThrow(() -> new UserNotFoundException(userId));

//     // Check if the user has already liked the post
//     boolean userAlreadyLiked = post.getLikers().stream()
//             .anyMatch(like -> like.getUser().getId().equals(userId));

//     if (userAlreadyLiked) {
//         // If the user has already liked the post, find the like and remove it
//         post.getLikers().removeIf(like -> like.getUser().getId().equals(userId));
//         postLikeRepository.deleteByUserIdAndPostId(userId, postId); // Assuming you have a custom delete method in your repository

//         return ResponseEntity.noContent().build(); // Respond with no content (successful deletion)
//     }

//     // Create a new PostLike
//     PostLike newPostLike = new PostLike();
//     newPostLike.setLiked(true);
//     newPostLike.setPost(post);
//     newPostLike.setUser(user);

//     // Save the PostLike
//     PostLike savedPostLike = postLikeRepository.save(newPostLike);

//     // Convert to EntityModel
//     EntityModel<PostLike> entityModel = plikeAssembler.toModel(savedPostLike);

//     // Respond with the created Like and a link to the self endpoint
//     return ResponseEntity
//             .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
//             .body(entityModel);
// }


@GetMapping("/comments/{commentId}/likes/{likeId}")
public EntityModel<CommentLike> getCommentLike(@PathVariable Integer postId, @PathVariable Integer commentId, @PathVariable Integer likeId) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));
    Post post = PostRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
    comment.setPost(post);

    CommentLike commentLike = comment.getLikes().stream()
            .filter(like -> like.getId().equals(likeId))
            .findFirst()
            .orElseThrow(() -> new CommentLikeNotFoundException(likeId));

    return clikeAssembler.toModel(commentLike);
}


@GetMapping("/likes/{likeId}")
public EntityModel<PostLike> getPostLike(@PathVariable Integer postId, @PathVariable Integer likeId) {
    Post post = PostRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

    PostLike postLike = post.getLikers().stream()
            .filter(like -> like.getId().equals(likeId))
            .findFirst()
            .orElseThrow(() -> new PostLikeNotFoundException(likeId));

    return plikeAssembler.toModel(postLike);
}

///////////////////neeed to be checked///////////////////////////////////////
@DeleteMapping("/comments/{commentId}/likes/{likeId}")
public ResponseEntity<?> deleteCommentLike(@PathVariable Integer commentId, @PathVariable Integer likeId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    
        CommentLike commentLike = comment.getLikes().stream()
                .filter(like -> like.getId().equals(likeId))
                .findFirst()
                .orElseThrow(() -> new CommentLikeNotFoundException(likeId));
    
        comment.getLikes().remove(commentLike);
       // Delete the comment like using the custom query method
     //  commentLikeRepository.deleteByIdAndComment_Id(likeId, commentId);
     commentLikeRepository.deleteByUserIdAndCommentId(commentLike.getUser().getId(), commentId);
     

       return ResponseEntity.noContent().build();
   }

@DeleteMapping("/likes/{likeId}")
public ResponseEntity<?> deletePostLike(@PathVariable Integer postId, @PathVariable Integer likeId) {
    // Log the incoming request details
    //System.out.println("Attempting to delete like with ID: " + likeId + " for post with ID: " + postId);
    
    // Find the post
    Post post = PostRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

    // Find the like to be deleted
    PostLike postLike = post.getLikers().stream()
            .filter(like -> like.getId().equals(likeId))
            .findFirst()
            .orElseThrow(() -> new PostLikeNotFoundException(likeId));

    // Log the details of the like being deleted
   // System.out.println("Found like to delete: " + postLike);

    // Remove the like from the post's likers
    post.getLikers().remove(postLike);
    
    // Delete the like from the repository
    postLikeRepository.deleteByUserIdAndPostId(postLike.getUser().getId(), postId);


    // Log the successful deletion
   // System.out.println("Successfully deleted like with ID: " + likeId);

    return ResponseEntity.noContent().build();
}


}


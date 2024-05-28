package com.project.proo.postInfo;

import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;

import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentModelAssembler assembler;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentController(CommentRepository commentRepository, CommentModelAssembler assembler,
                             PostRepository postRepository, UserRepository userRepository,
                             CommentLikeRepository commentLikeRepository) {
        this.commentRepository = commentRepository;
        this.assembler = assembler;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    @GetMapping("/{commentId}")
    public EntityModel<Comment> getComment(@PathVariable Integer postId,@PathVariable Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        return assembler.toModel(comment);
    }

    @GetMapping
    public CollectionModel<EntityModel<Comment>> getCommentsByPost(@PathVariable Integer postId) {
        List<EntityModel<Comment>> comments = commentRepository.findByPost_Id(postId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(comments);
    }

    @PostMapping("/users/{commentUserId}")
    public ResponseEntity<?> addComment(@PathVariable("postId") Integer postId,@Valid @PathVariable("commentUserId")  Integer commentUserId,
                                        @RequestBody Comment newComment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        User user = userRepository.findById(commentUserId)
                .orElseThrow(() -> new UserNotFoundException(commentUserId));

        newComment.setDate(LocalDateTime.now());
        newComment.setPost(post);
        newComment.setUser(user);

        Comment savedComment = commentRepository.save(newComment);
        EntityModel<Comment> entityModel = assembler.toModel(savedComment);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable Integer commentId,@Valid @RequestBody Comment updatedComment) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        comment.setCommentContent(updatedComment.getCommentContent());

        Comment savedComment = commentRepository.save(comment);
        EntityModel<Comment> entityModel = assembler.toModel(savedComment);

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException(commentId);
        }

        // Delete related CommentLikes
        commentLikeRepository.deleteByComment_Id(commentId);

        commentRepository.deleteById(commentId);

        return ResponseEntity.noContent().build();
    }

    


}



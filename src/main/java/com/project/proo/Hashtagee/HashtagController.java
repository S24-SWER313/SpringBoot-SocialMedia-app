package com.project.proo.Hashtagee;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import com.project.proo.postInfo.Post;
import com.project.proo.postInfo.PostModelAssembler;
import com.project.proo.postInfo.PostRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/hashtags")
public class HashtagController {

  private final PostModelAssembler assembler;
 
    private final HashtagRepository hashtagRepository;
    private final PostRepository postRepository;
    public HashtagController(HashtagRepository hashtagRepository, PostRepository postRepository, PostModelAssembler assembler) {
        this.assembler = assembler;
        this.hashtagRepository = hashtagRepository;
        this.postRepository = postRepository;
    }


    @GetMapping("/all")
    public List<Hashtag> getAllHashtags() {
        return hashtagRepository.findAll();
    }


    @GetMapping("/{hashtag}")
public CollectionModel<EntityModel<Post>> getPostsByHashtag(@PathVariable String hashtag) {
    List<Post> posts = postRepository.findByCaptionContaining(hashtag);
           
    List<EntityModel<Post>> postModels = posts.stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    Link selfLink = linkTo(methodOn(HashtagController.class).getPostsByHashtag(hashtag)).withSelfRel();

    return CollectionModel.of(postModels, selfLink);
}
}

package com.project.proo.postInfo;

import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/posts/{postId}")
public class MyFileController {

    private final MyFileRepository fileRepository;
    private final MyFileModelAssembler fileModelAssembler;
    private final PostRepository postRepository;

    @Autowired
    public MyFileController(MyFileRepository fileRepository, MyFileModelAssembler fileModelAssembler,
                           PostRepository postRepository) {
        this.fileRepository = fileRepository;
        this.fileModelAssembler = fileModelAssembler;
        this.postRepository = postRepository;
    }

    @PostMapping("/files/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@PathVariable Integer postId, @RequestParam("file") MultipartFile file) throws IOException {
        String message = "";
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    
        try {
            MyFile myFile = new MyFile();
            myFile.setPost(post);
            myFile.setType(file.getContentType());
            myFile.setName(file.getOriginalFilename());
    
            MyFile savedFile = fileRepository.save(myFile);
    
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    


    @GetMapping("/files/{fileId}")
    public EntityModel<MyFile> one(@PathVariable Integer postId, @PathVariable Integer fileId) {
        MyFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException(fileId));
        return fileModelAssembler.toModel(file);
    }

   
    // @GetMapping("/files")
    // public CollectionModel<EntityModel<MyFile>> getAllFilesForPost(@PathVariable Integer postId) {
    //     List<MyFile> filesForPost = fileRepository.findByPostId(postId);
    //     System.out.println("Files for Post ID " + postId + ": " + filesForPost); // Add logging
        
    //     List<EntityModel<MyFile>> files = filesForPost.stream()
    //             .map(fileModelAssembler::toModel)
    //             .collect(Collectors.toList());
    
    //     return CollectionModel.of(files);
    // }

    @GetMapping("/files")
    public CollectionModel<EntityModel<MyFile>> getAllFiles(@PathVariable Integer postId) {
        Optional<List<MyFile>> optionalFiles = fileRepository.findByPostId(postId);
        List<MyFile> files = optionalFiles.orElseThrow(() -> new MyFileNotFoundException("No files found for post with ID: " + postId));
        return CollectionModel.of(files.stream()
                .map(fileModelAssembler::toModel)
                .collect(Collectors.toList()));
    }


    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<ResponseMessage> deleteFileById(@PathVariable Integer postId, @PathVariable Integer fileId) {
    try {
        // Check if the file exists
        MyFile fileToDelete = fileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException(fileId));

        // Check if the file belongs to the specified post
        if (!fileToDelete.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("File with ID " + fileId + " does not belong to post with ID " + postId);
        }

        // Delete the file
        fileRepository.delete(fileToDelete);

        String message = "Deleted file with ID " + fileId + " for post ID " + postId;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (MyFileNotFoundException e) {
        String errorMessage = "File with ID " + fileId + " not found.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(errorMessage));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
    } catch (Exception e) {
        String errorMessage = "Could not delete file with ID " + fileId + ". Error: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(errorMessage));
    }
}

    


}


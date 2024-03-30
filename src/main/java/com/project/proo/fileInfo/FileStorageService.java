// package com.project.proo.fileInfo;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.util.StringUtils;
// import org.springframework.web.multipart.MultipartFile;

// import com.project.proo.postInfo.Post;
// import com.project.proo.postInfo.PostRepository;

// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.util.Optional;
// import java.util.stream.Stream;

// @Service
// public class FileStorageService {

//     @Autowired
//     private FileDBRepository fileDBRepository;

//     @Autowired
//     private PostRepository postRepository; // Assuming you have a PostRepository to access posts

//     public void store(MultipartFile file, Integer postId) throws IOException {
//         Optional<Post> optionalPost = postRepository.findById(postId);
//         if (optionalPost.isPresent()) {
//             Post post = optionalPost.get();
//             String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//             FileDB fileDB = new FileDB(fileName, post, file.getContentType(), file.getBytes());
//             fileDBRepository.save(fileDB);
//         } else {
//             throw new FileNotFoundException("Post not found with ID: " + postId);
//         }
//     }

//     public FileDB getFile(String id) {
//         try {
//           return fileDBRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found with ID: " + id));
//         } catch (FileNotFoundException e) {
//                 e.printStackTrace();
//         }
//         return null ;
//     }

//     public Stream<FileDB> getAllFiles() {
//         return fileDBRepository.findAll().stream();
//     }
// }


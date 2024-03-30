// package com.project.proo.fileInfo;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.hateoas.CollectionModel;
// import org.springframework.hateoas.EntityModel;
// import org.springframework.hateoas.server.RepresentationModelAssembler;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.multipart.MultipartFile;

// import com.project.proo.postInfo.ResponseMessage;

// import java.io.IOException;
// import java.util.List;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/posts/{postId}/files")
// public class FileDBController {

//     private final FileDBRepository fileDBRepository;
//     private final RepresentationModelAssembler<FileDB, EntityModel<FileDB>> fileDBModelAssembler;
//     private final FileStorageService fileStorageService;

//     @Autowired
//     public FileDBController(FileDBRepository fileDBRepository,
//                              RepresentationModelAssembler<FileDB, EntityModel<FileDB>> fileDBModelAssembler,
//                              FileStorageService fileStorageService) {
//         this.fileDBRepository = fileDBRepository;
//         this.fileDBModelAssembler = fileDBModelAssembler;
//         this.fileStorageService = fileStorageService;
//     }

//     @PostMapping("/upload")
//     public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,@PathVariable Integer postId) {
//         String message = "";
//         try {
//             fileStorageService.store(file, postId);
//             message = "Uploaded the file successfully: " + file.getOriginalFilename();
//             return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//         } catch (IOException e) {
//             message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
//             return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//         }
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<EntityModel<FileDB>> getFile(@PathVariable String id) {
//         FileDB fileDB = fileStorageService.getFile(id);
//         EntityModel<FileDB> fileDBEntityModel = fileDBModelAssembler.toModel(fileDB);
//         return ResponseEntity.ok(fileDBEntityModel);
//     }

//     @GetMapping("/all")
//     public ResponseEntity<CollectionModel<EntityModel<FileDB>>> getAllFiles() {
//         List<FileDB> files = fileStorageService.getAllFiles().collect(Collectors.toList());
//         List<EntityModel<FileDB>> fileModels = files.stream()
//                 .map(fileDBModelAssembler::toModel)
//                 .collect(Collectors.toList());
//         return ResponseEntity.ok(CollectionModel.of(fileModels));
//     }
// }

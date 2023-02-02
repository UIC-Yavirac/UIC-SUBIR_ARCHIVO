package subir.archivos.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import jakarta.servlet.annotation.MultipartConfig;
import subir.archivos.message.FileMessage;
import subir.archivos.model.FileModel;
import subir.archivos.service.FileService;

@Controller
@CrossOrigin({"*"})
@RestController
@MultipartConfig

public class FileController {
    
    @Autowired
    FileService fileService;

        
    @PostMapping("/upload")
        //  @PostMapping(value="/upload",params={"file"})
    public ResponseEntity<FileMessage> uploadFiles(@RequestParam("files")MultipartFile[] files){
        String message = "";
        try{
            List<String> fileNames = new ArrayList<>();
    
            Arrays.asList(files).stream().forEach(file -> {
                fileService.save(file);
                fileNames.add(file.getOriginalFilename());
            });
    
            message = "se subieron los archivos correspondiente" + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(new FileMessage(message));

        }catch(Exception e){
            message = "fallo al subir los archivos";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileMessage(message));

        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileModel>> getFiles(){
        List<FileModel> fileInfos = fileService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url= MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile",
                path.getFileName().toString()).build().toString(); 
            return new FileModel(filename, url);
        }).collect(Collectors.toList());
            
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        Resource file = fileService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename() + "\"").body(file);
    }

    @GetMapping("/delete/{filename:.+}")
        public ResponseEntity<FileMessage> deleteFile(@PathVariable String filename){
            String message = "";
                try{
                    message = fileService.deleteFile(filename);
                    return ResponseEntity.status(HttpStatus.OK).body(new FileMessage(message));
                }catch (Exception e){
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileMessage(message));
                }
        }
        
        
}

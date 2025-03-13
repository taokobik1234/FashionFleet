package com.example.FashionFleet.controller;



import com.example.FashionFleet.domain.Image;
import com.example.FashionFleet.domain.dto.response.image.ImageDTO;
import com.example.FashionFleet.service.ImageService;
import com.example.FashionFleet.util.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;


    @PostMapping("/upload")
    public ResponseEntity<Object> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDTO> imageDtos = imageService.saveImages(productId, files);
            return ResponseEntity.ok( imageDtos);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Upload failed!");
        }

    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException ,ResourceNotFoundException{
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return  ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                .body(resource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<Object> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(null);
            }
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body( null);
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Update failed!");
    }


    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<Object> deleteImage(@PathVariable Long imageId) {
        try {
            imageService.deleteImageById(imageId);  // Directly call deleteImageById
            return ResponseEntity.ok("Delete success!");
        } catch (ResourceNotFoundException e) {
            // Return 404 with the exception message if the image is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Return 500 with a generic failure message if an unexpected error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed! Please try again later.");
        }
    }
}


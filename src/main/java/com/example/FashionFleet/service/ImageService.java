package com.example.FashionFleet.service;

import com.example.FashionFleet.domain.Image;
import com.example.FashionFleet.domain.Product;
import com.example.FashionFleet.domain.dto.response.image.ImageDTO;
import com.example.FashionFleet.repository.ImageRepository;
import com.example.FashionFleet.util.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductService productService;


    public Image getImageById(Long id) throws ResourceNotFoundException {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    public void deleteImageById(Long id) throws ResourceNotFoundException {
        imageRepository.findById(id)
                .map(image -> {
                    imageRepository.delete(image);
                    return image;  // Return the image for further processing if needed
                })
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }


    public List<ImageDTO> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.fetchProductById(productId);

        List<ImageDTO> savedImageDTO = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDTO imageDto = new ImageDTO();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDTO.add(imageDto);

            }   catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDTO;
    }



    public void updateImage(MultipartFile file, Long imageId) throws ResourceNotFoundException {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

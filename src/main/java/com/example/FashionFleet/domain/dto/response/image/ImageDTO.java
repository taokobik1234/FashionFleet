package com.example.FashionFleet.domain.dto.response.image;

import lombok.Data;

@Data
public class ImageDTO {
    private Long id;
    private String fileName;
    private String downloadUrl;

}
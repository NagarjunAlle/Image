package com.example.image.service;

import com.example.image.Model.ImageData;
import com.example.image.Repo.ImageRepo;
import com.example.image.response.ImageUploadResponse;
import com.example.image.util.ImageUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepo imageRepo;

    public ImageUploadResponse uploadImage(MultipartFile file) throws IOException {
        imageRepo.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes())).build());
        return new ImageUploadResponse("Image uploaded successfully: " +
                file.getOriginalFilename());
    }
    @Transactional
    public ImageData getInfoByImageByName(String name) {
        Optional<ImageData> dbImage = imageRepo.findByName(name);

        return ImageData.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .imageData(ImageUtil.decompressImage(dbImage.get().getImageData())).build();

}

    @Transactional
    public byte[] getImage(String name) {
        Optional<ImageData> dbImage = imageRepo.findByName(name);
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
        return image;
    }



}

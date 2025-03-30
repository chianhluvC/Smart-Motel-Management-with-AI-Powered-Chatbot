package com.dacn.Nhom8QLPhongTro.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


public interface FilesStorageService {
    void init();

    String save(MultipartFile file);

    Resource load(String filename);

    boolean delete(String filename);

    byte[] imageToByteArray(String imagePath) throws IOException;

}

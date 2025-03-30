package com.dacn.Nhom8QLPhongTro.services;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Objects;


@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Không thể khởi tạo thư mục để tải lên!");
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
            return FileSystems.getDefault()
                    .getPath(root.resolve(file.getOriginalFilename()).toString())
                    .toAbsolutePath()
                    .toString();
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("Một tập tin có tên đó đã tồn tại.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Không thể đọc tập tin!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage());
        }
    }


    @Override
    public boolean delete(String filename) {
        try {
            Path file = Paths.get(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage());
        }
    }


    @Override
    public byte[] imageToByteArray(String imagePath) throws IOException {
        Path path = Path.of(imagePath);
        return Files.readAllBytes(path);
    }


}

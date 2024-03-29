package ru.kataaas.kaflent.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.kataaas.kaflent.entity.FileEntity;
import ru.kataaas.kaflent.utils.FileNameGenerator;
import ru.kataaas.kaflent.utils.FileTypeEnum;
import ru.kataaas.kaflent.utils.StaticVariable;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
public class StorageService {

    private final FileService fileService;

    private final FileNameGenerator fileNameGenerator;

    @Autowired
    public StorageService(FileService fileService, FileNameGenerator fileNameGenerator) {
        this.fileService = fileService;
        this.fileNameGenerator = fileNameGenerator;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(StaticVariable.FILE_STORAGE_PATH));
        } catch (IOException e) {
            log.error("Cannot initialize directory : {}", e.getMessage());
        }
    }

    public FileEntity store(MultipartFile file, FileTypeEnum type) {
        String completeName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String[] array = completeName.split("\\.");
        String fileExtension = array[array.length - 1];
        String fileName = fileNameGenerator.getRandomString();
        String newName = fileName + "." + fileExtension;

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(newName);
        fileEntity.setType(type);

        try {
            if (file.isEmpty()) {
                log.warn("Cannot save empty file with name : {}", newName);
                return null;
            }
            if (fileName.contains("..")) {
                log.warn("Cannot store file with relative path outside current directory {}", newName);
                return null;
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(StaticVariable.FILE_STORAGE_PATH).resolve(newName),
                        StandardCopyOption.REPLACE_EXISTING);
                return fileService.save(fileEntity);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public Resource load(String filename) {
        try {
            Path file = Paths.get(StaticVariable.FILE_STORAGE_PATH).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}

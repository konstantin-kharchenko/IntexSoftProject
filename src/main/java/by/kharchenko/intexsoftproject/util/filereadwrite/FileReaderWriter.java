package by.kharchenko.intexsoftproject.util.filereadwrite;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileReaderWriter {
    private static final Logger logger = LogManager.getLogger(FileReaderWriter.class);

    public Resource readFile(String path, String fileName) throws ServiceException {
        try {
            Path file = Paths.get(path + fileName);
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

    public boolean saveFile(MultipartFile multipartFile, String path, String fileName) throws ServiceException {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        try {
            multipartFile.transferTo(new File(path + fileName));
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        return true;
    }
}

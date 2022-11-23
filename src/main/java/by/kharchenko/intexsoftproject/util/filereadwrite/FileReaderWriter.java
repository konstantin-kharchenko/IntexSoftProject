package by.kharchenko.intexsoftproject.util.filereadwrite;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileReaderWriter {
    private static final Logger logger = LogManager.getLogger(FileReaderWriter.class);

    public byte[] readFile(String path) throws ServiceException {
        try {
            File result = new File(path);
            if (result.exists()) {
                InputStream inputStream = new FileInputStream(path);
                return IOUtils.toByteArray(inputStream);
            }
            throw new ServiceException("file not found");
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    public boolean saveFile(MultipartFile multipartFile, String path) throws ServiceException {
        String fileName = multipartFile.getOriginalFilename();
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        pathFile = new File(path + fileName);
        try {
            multipartFile.transferTo(pathFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

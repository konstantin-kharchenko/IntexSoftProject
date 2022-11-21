package by.kharchenko.intexsoftproject.util.filereadwrite;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileReaderWriter {
    private static final Logger logger = LogManager.getLogger(FileReaderWriter.class);

    public String readPhoto(String path) throws ServiceException {
        String data = "";
        if (path != null) {
            try {
                File file = new File(path);
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                data = stringBuilder.toString();
            } catch (FileNotFoundException e) {
                logger.log(Level.INFO, e.getMessage());
            } catch (IOException e) {
                throw new ServiceException(e);
            }
        }
        return data;
    }

    public boolean writePhoto(String data, String path) throws ServiceException {
        try (FileWriter nFile = new FileWriter(path)) {
            nFile.write(data);
            return true;
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
}

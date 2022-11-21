package by.kharchenko.intexsoftproject.util.validator;

import org.springframework.stereotype.Component;

@Component
public class PhotoValidator {
    private static final String PHOTO_REGEX = "^.+\\.(jpg|jpeg|raw|png)$";

    public boolean isCorrect(String fileName) {
        if (fileName == null) {
            return false;
        }
        return fileName.matches(PHOTO_REGEX);
    }
}

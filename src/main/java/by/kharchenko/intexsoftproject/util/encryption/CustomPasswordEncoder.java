package by.kharchenko.intexsoftproject.util.encryption;

import by.kharchenko.intexsoftproject.exception.ServiceException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomPasswordEncoder {

    private static final String ENCRYPTION_TYPE = "MD5";

    public static String encode(String password) throws ServiceException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance(ENCRYPTION_TYPE);
            byte[] bytes = md5.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                stringBuilder.append(b);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }
}

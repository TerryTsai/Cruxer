package email.com.gmail.ttsai0509.cruxer.service;

import email.com.gmail.ttsai0509.cruxer.exception.InternalException;
import email.com.gmail.ttsai0509.cruxer.exception.InvalidInputException;
import email.com.gmail.ttsai0509.cruxer.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
@Secured({"ROLE_USER"})
public class FileService {

    @Value("${upload.file.url}")
    private String url;

    @Value("${upload.file.path}")
    private String path;

    @Value("${upload.file.charset}")
    private String charset;

    @Value("${upload.file.length}")
    private int length;

    @PostConstruct
    public void init() {
        File dir = new File(path);

        if (!dir.exists())
            if (!dir.mkdir())
                throw new RuntimeException(path + " could not be created.");

        if (!dir.isDirectory())
            throw new RuntimeException(path + " is not a directory.");
    }

    public String saveFile(MultipartFile multifile, String fileType) {
        File file;
        String filename;

        do {
            filename = StringUtils.randomString(length, charset) + fileType;
            file = new File(path + filename);
        } while (file.exists());

        try {
            FileCopyUtils.copy(multifile.getBytes(), file);
        } catch (Exception e) {
            throw new InternalException("Error saving " + filename);
        }

        return url + filename;
    }

    public String saveBase64Png(String base64Png) {
        File file;
        String filename;

        do {
            filename = StringUtils.randomString(length, charset) + ".png";
            file = new File(path + filename);
        } while (file.exists());

        try {
            FileCopyUtils.copy(
                    Base64.getDecoder().decode(base64Png.replace("data:image/png;base64,", "").getBytes()),
                    file
            );
        } catch (Exception e) {
            throw new InternalException("Error saving " + filename);
        }

        return url + filename;

    }

}

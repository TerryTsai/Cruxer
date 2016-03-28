package email.com.gmail.ttsai0509.cruxer.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void init();

    String saveFile(MultipartFile multifile, String fileType);

    String saveBase64(String base64Data, Base64DataType dataType);

    public static enum Base64DataType {

        PNG(".png", "data:image/png;base64,"),
        JPEG(".jpeg", "data:image/jpeg;base64,");

        public final String filetype;
        public final String prefix;

        Base64DataType(String filetype, String prefix) {
            this.filetype = filetype;
            this.prefix = prefix;
        }
    }

}

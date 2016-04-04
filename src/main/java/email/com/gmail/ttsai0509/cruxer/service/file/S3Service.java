package email.com.gmail.ttsai0509.cruxer.service.file;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import email.com.gmail.ttsai0509.cruxer.exception.InternalException;
import email.com.gmail.ttsai0509.cruxer.service.FileService;
import email.com.gmail.ttsai0509.cruxer.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
@Secured({"ROLE_USER"})
@Profile("production")
public class S3Service implements FileService {

    @Value("${upload.file.url}")
    private String url;

    @Value("${upload.file.bucket}")
    private String bucket;

    @Value("${upload.file.accessKey}")
    private String accessKey;

    @Value("${upload.file.secretKey}")
    private String secretKey;

    @Value("${upload.file.charset}")
    private String charset;

    @Value("${upload.file.length}")
    private int length;

    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {

        s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));

    }

    @Override
    public String saveFile(MultipartFile multifile, String fileType) {
        String filename;

        do {
            filename = StringUtils.randomString(length, charset) + fileType;
        } while (fileExists(filename));

        try {
            s3Client.putObject(bucket, filename, multifile.getInputStream(), new ObjectMetadata());
        } catch (Exception e) {
            throw new InternalException("Error saving " + filename);
        }

        return url + filename;
    }

    @Override
    public String saveBase64(String base64Data, Base64DataType dataType) {
        ByteArrayInputStream bis = null;
        String filename;

        do {
            filename = StringUtils.randomString(length, charset) + dataType.filetype;
        } while (fileExists(filename));

        try {
            byte[] content = Base64.getDecoder().decode(base64Data.replace(dataType.prefix, "").getBytes());
            bis = new ByteArrayInputStream(content);
            s3Client.putObject(
                    bucket,
                    filename,
                    bis,
                    new ObjectMetadata()
            );

        } catch (Exception e) {
            throw new InternalException("Error saving " + filename);

        } finally {
            if (bis != null) {
                //noinspection EmptyCatchBlock
                try { bis.close(); } catch (Exception e) {}
            }
        }

        return url + filename;
    }

    private boolean fileExists(String file) {

        boolean exists = true;

        try {
            s3Client.getObjectMetadata(bucket, file);
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404)
                exists = false;
            else
                throw e;
        }

        return exists;

    }

}

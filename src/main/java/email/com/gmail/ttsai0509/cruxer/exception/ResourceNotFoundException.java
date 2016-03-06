package email.com.gmail.ttsai0509.cruxer.exception;

public class ResourceNotFoundException extends CruxerException {

    public ResourceNotFoundException(Class type, String id) {
        super("Unable to find " + type.getSimpleName() + " resource - " + id);
    }

}

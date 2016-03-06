package email.com.gmail.ttsai0509.cruxer.exception;


public class InvalidInputException extends CruxerException {

    public InvalidInputException(String field, String actual, String pattern) {
        super(String.format("Invalid %s %s : %s", field, actual, pattern));
    }

}

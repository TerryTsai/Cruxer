package email.com.gmail.ttsai0509.cruxer.exception;

public class InvalidFieldException extends CruxerException {

    public InvalidFieldException(Class type, String field, String actual, String expected) {
        super(String.format("%s field %s invalid (actual = %s, expected = %s)", type.getName(), field, actual, expected));
    }

}

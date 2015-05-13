package uk.co.informaticslab.exceptions;

/**
 * Created by tom on 08/05/2015.
 */
public class FileConverterException extends TokenizedRuntimeException {

    /**
     * Constructor
     *
     * @param format  A tokenised message format (e.g. "There are {} failures.")
     * @param varargs The arguments to use to replace the tokens ("{}") one per token
     */
    public FileConverterException(String format, Object... varargs) {
        super(format, varargs);
    }
}

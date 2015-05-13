package uk.co.informaticslab.exceptions;

import org.slf4j.helpers.MessageFormatter;

import static java.lang.System.arraycopy;

/**
 * Convenience class for constructing useful exception messages.
 */
public abstract class TokenizedRuntimeException extends RuntimeException {

    /**
     * Constructor
     *
     * @param format  A tokenised message format (e.g. "There are {} failures.")
     * @param varargs The arguments to use to replace the tokens ("{}") one per token
     */
    public TokenizedRuntimeException(String format, Object... varargs) {
        super(buildMessage(format, varargs));
        if (isThrowableAtEnd(varargs)) {
            initCause((Throwable) varargs[varargs.length - 1]);
        }
    }

    private static String buildMessage(String format, Object... varargs) {

        if (isThrowableAtEnd(varargs)) {
            // Have a Throwable at the end - this will cause formatting problems
            Object[] objects = new Object[varargs.length - 1];
            arraycopy(varargs, 0, objects, 0, objects.length);
            return MessageFormatter.arrayFormat(format, varargs).getMessage();
        } else {
            // No Throwable at the end
            return MessageFormatter.arrayFormat(format, varargs).getMessage();
        }
    }

    private static boolean isThrowableAtEnd(Object... varargs) {
        return varargs != null && varargs.length > 0 && varargs[varargs.length - 1] instanceof Throwable;
    }
}

package email.com.gmail.ttsai0509.cruxer.utils;

import org.junit.Test;

/**
 * Simple utility class for exception assertions.
 * <pre>
 * {@code Assert.exception(
 *      AuthenticationCredentialsNotFoundException.class,
 *      () -> holdRestController.getHold(hold.getId()),
 *      () -> holdRestController.deleteHold(hold.getId()),
 *      () -> holdRestController.postHold(file),
 *      () -> holdRestController.putHold(hold.getId(), file));
 * }
 * </pre>
 */

// @formatter:off
public class AssertEx {

    public static class AssertException extends RuntimeException {
        public AssertException(String message) {
            super(message);
        }
    }

    public static <T extends Exception> void exception(Class<T> type, Runnable... preds) {
        for (Runnable pred : preds) {
            try {
                pred.run();
                throw new AssertException("Expected " + type.getName() + " but nothing was thrown.");
            } catch (Exception e) {
                if (type == null)
                    throw new AssertException("Expected null but " + e.getClass().getName() + " was thrown.");
                else if (!type.isAssignableFrom(e.getClass()) && !(e instanceof AssertException))
                    throw new AssertException("Expected " + type.getName() + " but " + e.getClass().getName() + " was" +
                            " thrown.");
            }
        }
    }

    /* NULL TESTS */

    @Test
    public void nullAll() {
        exception(null);
    }

    @Test
    public void nullPred() {
        exception(NullPointerException.class);
    }

    @Test(expected = AssertException.class)
    public void nullType() {
        exception(null, () -> { throw new RuntimeException(); });
    }

    /* RELATIONAL TESTS */

    private class Larry extends RuntimeException {}
    private class Timmy extends Larry {}
    private class Emily extends RuntimeException {}
    private class Jenny extends Emily {}

    @Test
    public void root() {
        exception(Exception.class,
                () -> { throw new Larry(); },
                () -> { throw new Timmy(); },
                () -> { throw new Emily(); },
                () -> { throw new Jenny(); }
        );

        exception(RuntimeException.class,
                () -> { throw new Larry(); },
                () -> { throw new Timmy(); },
                () -> { throw new Emily(); },
                () -> { throw new Jenny(); }
        );
    }

    @Test
    public void identity() {
        exception(Larry.class, () -> { throw new Larry(); });
        exception(Timmy.class, () -> { throw new Timmy(); });
        exception(Emily.class, () -> { throw new Emily(); });
        exception(Jenny.class, () -> { throw new Jenny(); });
    }

    @Test
    public void child() {
        exception(Larry.class, () -> { throw new Timmy(); });
        exception(Emily.class, () -> { throw new Jenny(); });
    }

    @Test(expected = AssertException.class)
    public void father() {
        exception(Timmy.class, () -> { throw new Larry(); });
    }

    @Test(expected = AssertException.class)
    public void mother() {
        exception(Jenny.class, () -> { throw new Emily(); });
    }

    @Test(expected = AssertException.class)
    public void uncle() {
        exception(Jenny.class, () -> { throw new Larry(); });
    }

    @Test(expected = AssertException.class)
    public void aunt() {
        exception(Timmy.class, () -> { throw new Emily(); });
    }

    @Test(expected = AssertException.class)
    public void niece() {
        exception(Larry.class, () -> { throw new Jenny(); });
    }

    @Test(expected = AssertException.class)
    public void nephew() {
        exception(Emily.class, () -> { throw new Timmy(); });
    }

    @Test(expected = AssertException.class)
    public void siblings() {
        exception(Larry.class, () -> { throw new Emily(); });
    }

    @Test(expected = AssertException.class)
    public void cousins() {
        exception(Timmy.class, () -> { throw new Jenny(); });
    }

}
// @formatter:on
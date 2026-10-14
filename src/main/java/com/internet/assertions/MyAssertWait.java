package com.internet.assertions;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.internet.elements.MyElement;
import com.internet.waits.MyWait;

public class MyAssertWait extends MyWait {

    /**
     * Creates a wait helper bound to the supplied element.
     *
     * @param element the element whose wait conditions are being evaluated
     */
    public MyAssertWait(MyElement element) {
        super(element);
    }

    /**
     * Converts a timed-out wait into the project's custom runtime exception.
     *
     * @param message details of the wait failure
     * @param lastException the underlying cause, if one exists
     * @return never returns normally; always throws a custom wait exception
     */
    @Override
    protected @NonNull RuntimeException timeoutException(@NonNull String message,@Nullable Throwable lastException)    
    {
        throw new MyWaitException(message, lastException);
    }
    
}

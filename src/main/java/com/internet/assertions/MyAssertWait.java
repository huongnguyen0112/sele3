package com.internet.assertions;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.internet.elements.MyElement;
import com.internet.waits.MyWait;

public class MyAssertWait extends MyWait {

    public MyAssertWait(MyElement element) {
        super(element);
    }

    @Override
    protected @NonNull RuntimeException timeoutException(@NonNull String message,@Nullable Throwable lastException)    
    {
        throw new MyWaitException(message, lastException);
    }
    
}

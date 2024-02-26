package org.sessionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.userService.userAdd;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class sessionCleanup {
    @Autowired
    userAdd userUtil;
    private Logger logger = LoggerFactory.getLogger(sessionCleanup.class);

    public  sessionCleanup()
    {
        // do nothing
    }
    public void removeSession(String id)
    {
        synchronized (userUtil.getSessions()) {
            userUtil.removeSession(id);
        }
    }
}

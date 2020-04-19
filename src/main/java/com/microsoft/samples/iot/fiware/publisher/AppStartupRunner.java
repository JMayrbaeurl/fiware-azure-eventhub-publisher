package com.microsoft.samples.iot.fiware.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static final Log logger = LogFactory.getLog(AppStartupRunner.class);

    @Autowired
    private FiwareCtxBrokerService ctxService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        logger.info("Your application started with option names : " + args.getOptionNames());

        if (this.ctxService != null)
            this.ctxService.subscribeToAllChangesIfNotAlready();
    }
}
package com.microsoft.samples.iot.fiware.publisher;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.samples.iot.fiware.ngsiv2.Subscription;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    /**
     *
     */
    private static final String OPTIONNAME_SUBSCRIPTIONFILENAME = "subconfigfile";

    private static final Log logger = LogFactory.getLog(AppStartupRunner.class);

    @Value("${fiware.publisher.subscriptions.configFilePath:}")
    private String configFilePath;

    @Value("${fiware.publisher.subscriptions.all:true}")
    private boolean subscribeToAllChanges;

    @Autowired
    private FiwareCtxBrokerService ctxService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        logger.info("Your application started with option names : " + args.getOptionNames());

        if (this.ctxService != null) {

            if (this.configFilePath != null && this.configFilePath.length() > 0) {
                this.createSubscriptionsForConfiguration();
            } else if (args.containsOption(OPTIONNAME_SUBSCRIPTIONFILENAME)) {
                List<String> configFilePaths = args.getOptionValues(OPTIONNAME_SUBSCRIPTIONFILENAME);
                for(String path : configFilePaths) {
                    this.createSubscriptionsForConfigurationInFile(new File(path));
                }
            } else if (this.subscribeToAllChanges)
                this.ctxService.subscribeToAllChangesIfNotAlready();
        }
    }

    private void createSubscriptionsForConfiguration() {

        File configFile = new File(this.configFilePath);
        this.createSubscriptionsForConfigurationInFile(configFile);
    }

    private void createSubscriptionsForConfigurationInFile(File configFile) {

        if (!configFile.exists()) {
            logger.error("No subscriptions configuration file existing at '" + this.configFilePath);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            Subscription[] subs = null;
            try {
                subs = objectMapper.readValue(configFile, Subscription[].class);
            } catch (IOException e) {
                logger.error("Cant read subscriptions configuration", e);
                throw new UncheckedIOException(e);
            }

            if (subs != null && subs.length > 0) {
                this.ctxService.createSubscriptionsIfNotExist(subs);
            }
        }
    }
}
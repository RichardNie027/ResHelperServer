package com.tlg.reshelper.conf;

import com.tlg.reshelper.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Async
public class StoreHelperResTask {

    @Autowired
    private BusinessService businessService;

    @Scheduled(cron = "0 0/5 9-18 ? * MON-SAT")
    public void run() throws InterruptedException {
        businessService.loadGlobalBrandPicRes();
    }

}

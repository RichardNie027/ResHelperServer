package com.tlg.reshelper.conf;

import com.tlg.reshelper.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private BusinessService businessService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        businessService.loadGlobalBrandPic();
    }
}

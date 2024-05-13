package com.stock.pycurrent.schedule;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author fzc
 * @date 2024/2/19 10:35
 * @description
 */
@Configuration
@CommonsLog
public class PrepareData implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.warn("START V2" + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute());
    }

}

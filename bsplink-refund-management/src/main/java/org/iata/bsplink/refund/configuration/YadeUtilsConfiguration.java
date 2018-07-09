package org.iata.bsplink.refund.configuration;

import org.iata.bsplink.yadeutils.YadeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YadeUtilsConfiguration {
    
    @Bean
    public YadeUtils yadeUtils() {
        return new YadeUtils();
    }

}


package org.iata.bsplink.filemanager.configuration;

import org.iata.bsplink.yadeutils.YadeUtils;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class YadeUtilsConfigurationTest {

    @Bean
    @Primary
    public YadeUtils yadeUtils() {
        return Mockito.mock(YadeUtils.class);
    }
}

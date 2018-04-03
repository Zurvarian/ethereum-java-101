package ether.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vavr.jackson.datatype.VavrModule;
import io.vavr.jackson.datatype.VavrModule.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

@Configuration
public class SerializationConfig {

    @Bean
    public Module vavrModule() {
        return new VavrModule(new Settings().deserializeNullAsEmptyCollection(true).useOptionInPlainFormat(true));
    }

    @Bean
    public Module parameterNamesModule() {
        return new ParameterNamesModule(PROPERTIES);
    }

}

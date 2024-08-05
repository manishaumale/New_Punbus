package com.idms.base.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper configuration. Refer to documentation at http://modelmapper.org/
 * 
 * @author Hemant Makkar
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configure the ModelMapper
        modelMapper.getConfiguration()
            .setDeepCopyEnabled(true)
//            .setSkipNullEnabled(true)
            .setAmbiguityIgnored(true)
//            .setPropertyCondition(Conditions.isNotNull())
            .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
        
    }
    
    
}
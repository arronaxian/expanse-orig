package com.ds.expanse.app.configuration;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration
{
    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception
    {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Override
    protected String getDatabaseName() {
        return "expanse";
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        return new MongoClient("localhost" , 27017 );
    }

    public @Bean MongoTemplate mongoTemplate() throws Exception     {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
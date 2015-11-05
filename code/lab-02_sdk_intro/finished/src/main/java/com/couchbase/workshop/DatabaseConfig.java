package com.couchbase.workshop;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Centralized database configuration, to bootstrap the cluster and bucket as needed.
 */
@Configuration
public class DatabaseConfig {

    @Value("${hostname:127.0.0.1}")
    private String hostname;

    @Value("${bucketName:test-sample}")
    private String bucketName;

    @Value("${password:}")
    private String password;

    CouchbaseEnvironment couchbaseEnvironment() {
        return DefaultCouchbaseEnvironment
            .builder()
            .build();
    }

    @Bean
    public Cluster cluster() {
        return CouchbaseCluster.create(couchbaseEnvironment(), hostname);
    }

    @Bean
    public Bucket bucket() {
        return cluster().openBucket(bucketName, password);
    }

}

/**
 * Copyright (c) 2015 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */
package com.couchbase.workshop;

import com.couchbase.client.java.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

/**
 * Index checker which creates all not-yet-created indexes.
 */
@Component
public class IndexChecker implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexChecker.class);

    private final Bucket bucket;
    private ApplicationContext applicationContext;

    @Autowired
    public IndexChecker(final Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ensureIndexes();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.applicationContext = ctx;
    }

    /**
     * Helper method to make sure that all needed indexes are created if they aren't already.
     *
     * @throws Exception
     */
    private void ensureIndexes() throws Exception {
        LOGGER.info("Loading current Indexes for bucket {}", bucket.name());

        List<Index> current = currentIndexes();

        LOGGER.info("Found {} indexes on bucket {}", current.size(), bucket.name());
        LOGGER.debug("Found Indexes: {}", current);

        List<Index> wanted = wantedIndexes();

        LOGGER.info("Found {} indexes to potentially create on bucket {}", wanted.size(), bucket.name());
        LOGGER.debug("Wanted Indexes: {}", wanted);

        wanted.removeAll(current);

        LOGGER.info("Creating {} new indexes.", wanted.size());

        createIndexes(wanted);
    }

    /**
     * Loads all current indexes for the bucket and returns them as a value object.
     *
     * @return the list of found indexes.
     */
    private List<Index> currentIndexes() {

        // IMPLEMENT ME

        return Collections.emptyList();
    }

    /**
     * Loads all wanted indexes from a resource file.
     *
     * @return the list of indexes to ensure.
     */
    private List<Index> wantedIndexes() {
        Resource indexes = applicationContext.getResource("classpath:wanted_indexes.csv");

        // IMPLEMENT ME

        return Collections.emptyList();
    }

    /**
     * Creates all given indexes.
     *
     * @param toCreate indexes to create.
     */
    private void createIndexes(List<Index> toCreate) {

        // IMPLEMENT ME

    }

}

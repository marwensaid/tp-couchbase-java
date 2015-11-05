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
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.query.Query;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.query.QueryRow;
import com.couchbase.client.java.query.dsl.path.index.IndexType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.couchbase.client.java.query.Index.createIndex;
import static com.couchbase.client.java.query.Index.createPrimaryIndex;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

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

        QueryResult indexResult = bucket.query(Query.simple(
            select("indexes.*").from("system:indexes").where(i("keyspace_id").eq(s(bucket.name())))
        ));

        List<Index> currentIndexes = new ArrayList<Index>();
        for (QueryRow foundIndex : indexResult) {
            IndexType indexType;
            if (foundIndex.value().getString("using").equals("view")) {
                indexType = IndexType.VIEW;
            } else {
                indexType = IndexType.GSI;
            }


            boolean primary = foundIndex.value().containsKey("is_primary") ? foundIndex.value().getBoolean("is_primary") : false;
            currentIndexes.add(new Index(
                foundIndex.value().getString("name"),
                primary,
                indexType,
                (String[]) foundIndex.value().getArray("index_key").toList().toArray(new String[0])
            ));
        }

        return currentIndexes;
    }

    /**
     * Loads all wanted indexes from a resource file.
     *
     * @return the list of indexes to ensure.
     */
    private List<Index> wantedIndexes() {
        Resource indexes = applicationContext.getResource("classpath:wanted_indexes.csv");

        List<Index> wanted = new ArrayList<Index>();
        try {
            try(BufferedReader br = new BufferedReader(new FileReader(indexes.getFile()))) {
                for(String line; (line = br.readLine()) != null; ) {
                    String[] row = line.split(";");
                    IndexType indexType;
                    if (row[3].toUpperCase().equals("VIEW")) {
                        indexType = IndexType.VIEW;
                    } else {
                        indexType = IndexType.GSI;
                    }
                    String[] fields = row[2].split(",");
                    wanted.add(new Index(row[0], Boolean.parseBoolean(row[1]), indexType, fields));
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not load wanted indexes.", ex);
        }

        return wanted;
    }

    /**
     * Creates all given indexes.
     *
     * @param toCreate indexes to create.
     */
    private void createIndexes(List<Index> toCreate) {
        for (Index idx : toCreate) {
            LOGGER.info("Creating index: {}", idx);

            Query query;
            if (idx.isPrimary()) {
                query = Query.simple(createPrimaryIndex().on(bucket.name()).using(idx.getIndexType()));
            } else {
                query = Query.simple(createIndex(idx.getName()).on(bucket.name(), x(JsonArray.from(idx.getFields()))));
            }

            QueryResult indexResult = bucket.query(query);
            if (indexResult.finalSuccess()) {
                LOGGER.info("Index creation for {} successful.", idx.getName());
            } else {
                LOGGER.warn("Could not create index {}, because of {}.", idx.getName(), indexResult.errors());
            }
        }
    }

}

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
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.Query;
import com.couchbase.client.java.query.QueryParams;
import com.couchbase.client.java.query.QueryPlan;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.query.QueryRow;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import com.couchbase.client.java.query.dsl.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;
import static com.couchbase.client.java.query.dsl.functions.AggregateFunctions.count;

/**
 * HTTP Controller to run queries and return results.
 */
@RestController
@RequestMapping("/query")
public class QueryController {

    private final Bucket bucket;

    private final QueryPlan queryPlan;

    @Autowired
    public QueryController(final Bucket bucket) {
        this.bucket = bucket;

        queryPlan = bucket.prepare(
            select("airportname")
                .from(i(bucket.name()))
                .where(x("type").eq(s("airport")).and(x("city").like(s("S%"))))
                .orderBy(Sort.asc("airportname"))
        );
    }

    @RequestMapping("/readYourWrite")
    public Integer readYourWrite() {
        bucket.upsert(
            JsonDocument.create("doc-" + System.nanoTime(), JsonObject.create().put("type", "counter"))
        );

        QueryResult query = bucket.query(Query.simple(
            select(count("*").as("cnt")).from(i(bucket.name())).where(x("type").eq(s("counter"))),
            QueryParams.build().consistency(ScanConsistency.REQUEST_PLUS)
        ));

        return query.allRows().get(0).value().getInt("cnt");
    }

    @RequestMapping("/prepared")
    public List<String> prepared() {

        QueryResult result = bucket.query(Query.prepared(queryPlan));

        List<String> names = new ArrayList<>();
        for (QueryRow row : result) {
            names.add(row.value().getString("airportname"));
        }

        return names;
    }

    @RequestMapping("/parameterized")
    public Map<String, Object> parameterized(@RequestParam String faa) {

        QueryResult query = bucket.query(Query.parametrized(
                select(i(bucket.name()) + ".*")
                    .from(i(bucket.name()))
                    .where(x("faa").eq("$1")),
                JsonArray.from(faa))
        );

        return query.allRows().get(0).value().toMap();
    }

}

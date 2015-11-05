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
import com.couchbase.client.java.query.QueryPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        queryPlan = null; // IMPLEMENT ME
    }

    @RequestMapping("/readYourWrite")
    public Integer readYourWrite() {

        // IMPLEMENT ME

        return 0;
    }

    @RequestMapping("/prepared")
    public List<String> prepared() {

        // IMPLEMENT ME

        return Collections.emptyList();
    }

    @RequestMapping("/parameterized")
    public Map<String, Object> parameterized(@RequestParam String faa) {

        // IMPLEMENT ME

        return Collections.emptyMap();
    }

}

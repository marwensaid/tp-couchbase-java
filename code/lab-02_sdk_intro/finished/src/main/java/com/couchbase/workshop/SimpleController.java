package com.couchbase.workshop;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.Query;
import com.couchbase.client.java.query.QueryResult;
import com.sun.corba.se.spi.ior.ObjectKey;
import jdk.nashorn.api.scripting.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;
import static com.couchbase.client.java.query.dsl.functions.AggregateFunctions.count;

@RestController
@RequestMapping("/")
public class SimpleController {

    private final Bucket bucket;

    @Autowired
    public SimpleController(Bucket bucket) {
        this.bucket = bucket;
    }

    @Value("${bucketName:test-sample}")
    private String bucketName;

    @RequestMapping("/hello")
    public Map<String, Object> insert() {
        String id = "user::" + UUID.randomUUID().toString();
        JsonObject content = JsonObject
                .create()
                .put("hello", "world")
                .put("nanotime", System.nanoTime())
                .put("type", "user");
        JsonDocument document = JsonDocument.create(id, content);

        bucket.insert(document);

        JsonDocument loaded = bucket.get(id);
        return loaded.content().toMap();
    }

    @RequestMapping("/orange")
    public Map<String, Object> insertOrange() {
        String id = "user::" + UUID.randomUUID().toString();
        JsonObject content = JsonObject
                .create()
                .put("hello", "vert")
                .put("nanotime", System.nanoTime())
                .put("type", "user");
        JsonDocument document = JsonDocument.create(id, content);

        bucket.insert(document);

        JsonDocument loaded = bucket.get(id);
        return loaded.content().toMap();
    }

    @RequestMapping("/wvf")
    public Map<String, Object> insertWvf() {
        String id = "user::" + UUID.randomUUID().toString();
        JsonObject object = JsonObject
                .create()
                .put("project", "video search")
                .put("updateTime", System.currentTimeMillis())
                .put("userId", "me")
                .put("dataBase", bucketName);

        JsonDocument document = JsonDocument.create(id, object);
        bucket.insert(document);
        JsonDocument loaded = bucket.get(id);
        return loaded.content().toMap();
    }

    @RequestMapping("/world")
    public Integer read() {

        QueryResult result = bucket.query(Query.simple(
                select(count("*").as("count"))
                        .from(i(bucket.name()))
                        .where(x("type").eq(s("user")))
        ));

        return result.allRows().get(0).value().getInt("count");
    }


}

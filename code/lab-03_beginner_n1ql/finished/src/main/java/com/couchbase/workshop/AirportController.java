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
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.Query;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.query.QueryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import static com.couchbase.client.java.query.dsl.Sort.asc;
import static com.couchbase.client.java.query.dsl.functions.ArrayFunctions.arrayLength;

@RestController
@RequestMapping("/airport")
public class AirportController {

    private final Bucket bucket;

    @Autowired
    public AirportController(Bucket bucket) {
        this.bucket = bucket;
    }

    /**
     * Returns a list of all airport names, sorted by name.
     *
     * This query does not use the DSL as an example, but can easily be converted.
     *
     * @return list of airport names
     */
    @RequestMapping("/allNames")
    public List<String> allAirportNames() {
        String query = "SELECT airportname FROM `" + bucket.name()
            + "` WHERE type = 'airport' ORDER BY airportname ASC";

        QueryResult result = bucket.query(Query.simple(query));
        List<String> airports = new ArrayList<>();
        for (QueryRow row : result) {
            airports.add(row.value().getString("airportname"));
        }

        return airports;
    }

    /**
     * Returns airport names which start with the given prefix.
     *
     * @param prefix the prefix they need to start with
     * @return the list of airport names.
     */
    @RequestMapping("/prefixedBy")
    public List<String> airportNamesStartingWith(@RequestParam String prefix) {
        QueryResult result = bucket.query(Query.simple(
            select("airportname")
                .from(i(bucket.name()))
                .where(
                    x("type").eq(s("airport")).and(x("airportname").like(s(prefix.toUpperCase() + "%")))
                )
                .orderBy(asc("airportname"))
        ));

        List<String> airports = new ArrayList<>();
        for (QueryRow row : result) {
            airports.add(row.value().getString("airportname"));
        }

        return airports;
    }

    /**
     * Returns the full airport information by its FAA code (like LAX or SFO).
     *
     * @param faa the faa code.
     * @return the airport info, or 404 if not found.
     */
    @RequestMapping("/byFaa")
    public ResponseEntity<Map<String, Object>> byFaa(@RequestParam String faa) {
        QueryResult result = bucket.query(Query.simple(
            select(i(bucket.name()) + ".*")
                .from(i(bucket.name()))
                .where(
                    x("type").eq(s("airport")).and(x("faa").eq(s(faa)))
                )
        ));

        List<QueryRow> rows = result.allRows();

        if (rows.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
            result.allRows().get(0).value().toMap(),
            HttpStatus.OK
        );
    }

    /**
     * Returns all routes by the given airline.
     *
     * @param airline the airline to check.
     * @return the list of routes.
     */
    @RequestMapping("/routes")
    public ResponseEntity<List<String>> routesByAirline(@RequestParam String airline) {
        QueryResult result = bucket.query(Query.simple(
            select(
                arrayLength("schedule").as("num_routes"),
                x("sourceairport").as("source"),
                x("destinationairport").as("dest"),
                x("airline")
            )
                .from(i(bucket.name()))
                .where(
                    x("type").eq(s("route")).and(x("airline").eq(s(airline)))
                )
                .orderBy(asc("source"))
        ));


        List<String> destinations = new ArrayList<>();
        for (QueryRow row : result) {
            JsonObject value = row.value();
            destinations.add(
                value.getString("source")
                    + " -> " + value.getString("dest")
                    + ": " + value.getLong("num_routes")
            );
        }

        return new ResponseEntity<List<String>>(destinations, HttpStatus.OK);
    }

}

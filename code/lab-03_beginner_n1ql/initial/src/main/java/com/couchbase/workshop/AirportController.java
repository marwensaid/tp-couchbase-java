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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        // TODO: implement me

        return Collections.emptyList();
    }

    /**
     * Returns airport names which start with the given prefix.
     *
     * @param prefix the prefix they need to start with
     * @return the list of airport names.
     */
    @RequestMapping("/prefixedBy")
    public List<String> airportNamesStartingWith(@RequestParam String prefix) {

        // TODO: implement me

        return Collections.emptyList();
    }

    /**
     * Returns the full airport information by its FAA code (like LAX or SFO).
     *
     * @param faa the faa code.
     * @return the airport info, or 404 if not found.
     */
    @RequestMapping("/byFaa")
    public ResponseEntity<Map<String, Object>> byFaa(@RequestParam String faa) {

        // TODO: implement me

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Returns all routes by the given airline.
     *
     * @param airline the airline to check.
     * @return the list of routes.
     */
    @RequestMapping("/routes")
    public ResponseEntity<List<String>> routesByAirline(@RequestParam String airline) {

        // TODO: implement me

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}

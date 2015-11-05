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

import com.couchbase.client.java.document.json.JsonObject;
import java.util.Arrays;
import java.util.List;

public class FlightPath {

    private final String name;
    private final String flight;
    private final List<String> equipment;
    private final String utc;
    private final String source;
    private final String destination;

    private FlightPath(String name, String flight, List<String> equipment, String utc, String source, String destination) {
        this.name = name;
        this.flight = flight;
        this.equipment = equipment;
        this.utc = utc;
        this.source = source;
        this.destination = destination;
    }

    public static FlightPath create(JsonObject input, String from, String to) {
        return new FlightPath(
            input.getString("name"),
            input.getString("flight"),
            Arrays.asList(input.getString("equipment").split(" ")),
            input.getString("utc"),
            from,
            to
        );
    }

    public String getName() {
        return name;
    }

    public String getFlight() {
        return flight;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public String getUtc() {
        return utc;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }
}

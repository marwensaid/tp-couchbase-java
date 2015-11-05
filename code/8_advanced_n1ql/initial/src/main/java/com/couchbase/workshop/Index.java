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

import com.couchbase.client.java.query.dsl.path.index.IndexType;
import java.util.Arrays;

/**
 * Value object to represent an Index.
 */
public class Index {

    private final String name;
    private final boolean primary;
    private final String[] fields;
    private final IndexType indexType;

    public Index(String name, boolean primary, IndexType indexType, String... fields) {
        this.name = name;
        this.primary = primary;
        this.fields = fields;
        this.indexType = indexType;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return primary;
    }

    public String[] getFields() {
        return fields;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Index{");
        sb.append("name='").append(name).append('\'');
        sb.append(", primary=").append(primary);
        sb.append(", fields=").append(Arrays.toString(fields));
        sb.append(", indexType=").append(indexType);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Index index = (Index) o;

        if (primary != index.primary) return false;
        if (name != null ? !name.equals(index.name) : index.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(fields, index.fields)) return false;
        return indexType == index.indexType;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (primary ? 1 : 0);
        result = 31 * result + (fields != null ? Arrays.hashCode(fields) : 0);
        result = 31 * result + (indexType != null ? indexType.hashCode() : 0);
        return result;
    }
}

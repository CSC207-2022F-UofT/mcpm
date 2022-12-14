package org.hydev.mcpm.utils.arrays;

/**
 * A data structure supporting appending of index-value pairs
 * and querying the sum of all values in a fixed window size.
 * This only supports addition non-decreasing index pairs, and queries of non-increasing indices,
 * and queries of a fixed window size.
 */
public interface FixedWindowSum {
    /**
     * Add an index-value pair to the window.
     *
     * @param index the index of the pair
     * @param val value to add to data structure
     */
    void add(long index, long val);

    /**
     * Query the sum of all values that have an index within the window size (exclusive) that ends at the given index.
     *
     * @param index the given index
     * @return the result of the query
     */
    long sum(long index);
}

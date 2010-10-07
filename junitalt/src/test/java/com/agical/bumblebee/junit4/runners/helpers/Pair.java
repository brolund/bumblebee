/**
 * 
 */
package com.agical.bumblebee.junit4.runners.helpers;

public class Pair<K, V> {
    public final K key;
    public final V value;
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    @Override
    public String toString() {
        return key + "=>" + value;
    }
}
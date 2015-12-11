package com.m3rcuriel.controve;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class wrapping a {@link HashMap} of states and their keys, including useful extraction fuctions.
 * Allows the internal map to be iterated over.
 *
 * @author Lee Mracek
 */
public class StateHolder implements Iterable<Map.Entry<String, Object>> {
  private HashMap<String, Object> states = new HashMap<String, Object>();

  /**
   * Insert a key, value pair into the StateHolder.
   *
   * @param key   the hashmap key
   * @param value the value
   */
  public void put(String key, Object value) {
    states.put(key, value);
  }

  /**
   * Retrieve an entry from the StateHolder using a key.
   *
   * @param key the object to be retrieved
   * @return the object
   */
  public Object get(String key) {
    return states.get(key);
  }

  /**
   * Get a set containing the keys in the StateHolder.
   *
   * @return the set of all keys in the StateHolder
   */
  public Set<String> keySet() {
    return states.keySet();
  }

  @Override
  public Iterator<Map.Entry<String, Object>> iterator() {
    return states.entrySet().iterator();
  }
}

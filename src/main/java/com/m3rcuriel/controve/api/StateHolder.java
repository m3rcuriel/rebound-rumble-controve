package com.m3rcuriel.controve.api;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Lee Mracek
 */
public interface StateHolder {

  Supplier get(String key);

  void put(String key, Supplier accessor);

  Set<String> keySet();

  String getName();
}

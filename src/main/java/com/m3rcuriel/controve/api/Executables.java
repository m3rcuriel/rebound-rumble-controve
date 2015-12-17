package com.m3rcuriel.controve.api;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Threadsafe list of {@link Executable} instances.
 *
 * @author Lee Mracek
 */
public final class Executables implements Iterable<Executable> {
  private final CopyOnWriteArrayList<Executable> executables = new CopyOnWriteArrayList<>();

  public Executables() {
  }

  /**
   * Register an {@link Executable} to be run as part of this executables instance.
   *
   * @param r the {@link Executable} to be registered
   * @return true if the {@link Executable} was added
   */
  public boolean register(Executable r) {
    return executables.addIfAbsent(r);
  }

  /**
   * Unregister an {@link Executable}, or prevent it from being run.
   *
   * @param r the {@link Executable} to be removed
   * @return true if the {@link Executable} was removed
   */
  public boolean unregister(Executable r) {
    return executables.remove(r);
  }

  /**
   * Unregister all {@link Executables}.
   */
  public void unregisterAll() {
    executables.clear();
  }

  @Override
  public Iterator<Executable> iterator() {
    return executables.iterator();
  }

  /**
   * Check if the instance is empty.
   *
   * @return true if the instance is empty
   */
  public boolean isEmpty() {
    return executables.isEmpty();
  }
}

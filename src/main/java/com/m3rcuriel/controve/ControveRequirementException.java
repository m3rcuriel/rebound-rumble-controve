package com.m3rcuriel.controve;

/**
 * Exception signaling a requirement missing (typically hardware).
 *
 * @author Lee Mracek
 */
public class ControveRequirementException extends RuntimeException {

  public ControveRequirementException() {
  }

  /**
   * Construct a new exception based on a message.
   *
   * @param message the message
   */
  public ControveRequirementException(String message) {
    super(message);
  }

  /**
   * Construct a new exception based on the cause.
   *
   * @param cause the cause
   */
  public ControveRequirementException(Throwable cause) {
    super(cause);
  }

  /**
   * Construct a new exception.
   *
   * @param message the message
   * @param cause   the cause
   */
  public ControveRequirementException(String message, Throwable cause) {
    super(message, cause);
  }
}

package com.milibrodereservas.citame.services;

import lombok.Data;

@Data
public class ValidationException extends RuntimeException {
  public static final int DB_GENERIC_ERROR = -1;
  public static final int DUPLICATED = 1;
  public static final int REQUIRED_VALUE = 2;
  public static final int LENGTH_FIELD_MAX = 3;
  public static final int LENGTH_FIELD_MIN = 4;
  public static final int FORMAT_FIELD_BAD = 5;
  public static final int NOT_FOUND = 10;
  public static final int NOT_FOUND_PARAM = 11;

  private int code;
  private String detail;
    public ValidationException(String message, int code, String detail) {
      super(message);
      this.code = code;
      this.detail = detail;
    }
}

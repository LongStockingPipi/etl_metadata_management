package pers.jason.etl.rest.pojo.dto;

import java.io.Serializable;

public class SimpleResponse implements Serializable {

  private static final String SUCCESS = "请求成功";

  private static final String FAILED = "请求失败";

  private Result result;

  private String code;

  private String message;

  private Object object;

  private SimpleResponse(Result result, String message) {
    this.result = result;
    this.message = message;
  }

  private SimpleResponse(Result result, String message, Object o) {
    this.result = result;
    this.message = message;
    this.object = o;
  }

  public static SimpleResponse createSuccessResponse() {
    return new SimpleResponse(Result.SUCCESS, SUCCESS);
  }

  public static SimpleResponse createSuccessResponse(String message) {
    return new SimpleResponse(Result.SUCCESS, message);
  }

  public static SimpleResponse createSuccessResponse(String message, Object object) {
    return new SimpleResponse(Result.SUCCESS, message, object);
  }

  public static SimpleResponse createSuccessResponse( Object object) {
    return new SimpleResponse(Result.SUCCESS, SUCCESS, object);
  }

  public enum Result {
    SUCCESS("success", 0),
    FAILED("failed", 1);

    public String name;

    public Integer code;

    Result(String name, Integer code) {
      this.name = name;
      this.code = code;
    }
  }

}

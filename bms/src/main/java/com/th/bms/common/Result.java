package com.th.bms.common;

import lombok.Data;

/**
 * 返回数据封装
 */
@Data
public class Result {

  private Integer code; // 状态码 200 为成功 大于等于 300 为失败

  private String message; // 成功或失败的提示消息

  private Object data; // 返回的数据

  // 封装结果
  public static Result result(Integer code, String message, Object data) {
    Result res = new Result();
    res.setCode(code);
    res.setMessage(message);
    res.setData(data);
    return res;
  }

  // 失败
  public static Result fail(String message) {
    return result(300, message, null);
  }
  public static Result fail(Integer code, String message) {
    return result(code, message, null);
  }

  // 成功
  public static Result success(String message) {
    return result(200, message, null);
  }
  public static Result success(String message, Object data) {
    return result(200, message, data);
  }
}
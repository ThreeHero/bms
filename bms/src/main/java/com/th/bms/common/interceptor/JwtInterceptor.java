package com.th.bms.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.th.bms.common.CustomException;
import com.th.bms.entity.User;
import com.th.bms.service.UserService;
import com.th.bms.utils.TokenUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * jwt拦截器
 */
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

  @Autowired
  private UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String token = request.getHeader("Authorization");

    // 执行认证
    if (StrUtil.isBlank(token)) {
      throw new CustomException("无token，请重新登录");
    }

    token = token.split(" ")[1];
    // 获取 token 中的 user id
    String userId;
    try {
      userId = JWT.decode(token).getAudience().get(0);
    } catch (JWTDecodeException j) {
      throw new CustomException("token验证失败，请重新登录");
    }
    // 根据token中的userid查询数据库
    User user = userService.getById(userId);
    if (user == null) {
      throw new CustomException("用户不存在，请重新登录");
    }
    // 用户密码加签验证 token
    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(TokenUtils.ThreeHero)).build();
    try {
      jwtVerifier.verify(token); // 验证token
    } catch (JWTVerificationException e) {
      throw new CustomException("token校验失败，请重新登录");
    }
    return true;
  }

}

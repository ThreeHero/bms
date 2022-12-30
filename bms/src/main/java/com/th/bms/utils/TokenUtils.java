package com.th.bms.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.th.bms.entity.User;
import java.util.Date;

public class TokenUtils {

  public static final String ThreeHero = "my_hero_is_th"; // 设置加密令牌

  // 生成token
  public static String createToken(User user) {

    // token
    return JWT.create().withAudience(user.getId().toString()) // 将 id 保存入token 作为载荷
        .withExpiresAt(DateUtil.offsetHour(new Date(), 12)) // 设置两小时后token过期
        .sign(Algorithm.HMAC256(ThreeHero)); // 令牌加密
  }


}

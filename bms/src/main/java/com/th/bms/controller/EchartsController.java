package com.th.bms.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.th.bms.common.Result;
import com.th.bms.entity.User;
import com.th.bms.service.UserService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

  @Autowired
  private UserService userService;

  /**
   * 示例
   * @return
   */
  @GetMapping("/example")
  public Result getExample() {
    Map<String, Object> map = new HashMap<>();
    map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
    map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
    return Result.success("查询成功", map);
  }

  @GetMapping("/members")
  public Result members() {
    Map<String, Object> map = new HashMap<>();

    List<User> list = userService.list();
    int q1 = 0; // 第一 季度
    int q2 = 0; // 第二 季度
    int q3 = 0; // 第三 季度
    int q4 = 0; // 第四 季度
    for (User user : list) {
      Date createTime = user.getCreateTime();
      Quarter quarter = DateUtil.quarterEnum(createTime);
      switch (quarter) {
        case Q1: q1 += 1; break;
        case Q2: q2 += 1; break;
        case Q3: q3 += 1; break;
        case Q4: q4 += 1; break;
        default: break;
      }
    }

    map.put("x", CollUtil.newArrayList("第一季度", "第二季度", "第三季度", "第四季度"));
    map.put("data", CollUtil.newArrayList(q1, q2, q3, q4));
    return Result.success("查询成功", map);
  }
}

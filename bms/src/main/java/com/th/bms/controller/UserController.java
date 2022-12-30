package com.th.bms.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bms.common.Result;
import com.th.bms.dto.UserDTO;
import com.th.bms.entity.User;
import com.th.bms.service.UserService;
import com.th.bms.utils.TokenUtils;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public Result login(@RequestBody User user) {
    // 获取密码
    String password = user.getPassword();
    // 对密码进行md5加密
    password = DigestUtils.md5DigestAsHex(password.getBytes());
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    // 比对用户名
    queryWrapper.eq(User::getUsername, user.getUsername());
    User u = userService.getOne(queryWrapper);

    if (u == null) {
      return Result.fail("没有该用户");
    }
    // 比对密码
    if (!u.getPassword().equals(password)) {
      return Result.fail("密码错误");
    }

    // 不需要给前端返回密码
    u.setPassword("");
    // 生成token
    String token = TokenUtils.createToken(u);
    HashMap<String, Object> map = new HashMap<>();
    map.put("token", token);
    map.put("user", u);

    return Result.success("登录成功", map);
  }

  // 增
  @PostMapping
  public Result save(@RequestBody User user) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(user.getUsername() != null, User::getUsername, user.getUsername());

    User one = userService.getOne(queryWrapper);
    if (one != null) {
      // 有重名的
      return Result.fail("用户名重复");
    }

    // 设置密码字段
    user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
    userService.save(user);
    return Result.success("新增成功");
  }

  /**
   * 修改密码
   * @return
   */
  @PostMapping("/password")
  public Result updatePassword(@RequestBody UserDTO userDTO) {
    // 根据id查询账号
    User user = userService.getById(userDTO.getId());
    // 判断该用户是否为空
    if (user == null) {
      return Result.fail("没有该用户, 修改失败");
    }
    // 比对旧密码与数据库密码是否一致
    // 对旧密码进行加密
    String password = DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes());
    if (!password.equals(user.getPassword())) {
      return Result.fail("原密码错误");
    }

    // 成功 将新密码设置进数据库
    user.setPassword(DigestUtils.md5DigestAsHex(userDTO.getNewPassword().getBytes()));
    userService.updateById(user);
    return Result.success("修改密码成功");
  }
  // 删
  @DeleteMapping("/{id}")
  public Result removeById(@PathVariable Long id) {
    userService.removeById(id);
    return Result.success("删除成功");
  }
  // 改
  @PutMapping
  public Result update(@RequestBody User user) {
    if (user.getId() == null) {
      return Result.fail("缺少用户id");
    }
    userService.updateById(user);
    return Result.success("修改成功");
  }

  // 查
  @GetMapping("/{id}")
  public Result getById(@PathVariable Long id) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getId, id);
    User user = userService.getOne(queryWrapper);
    if (user != null) {
      return Result.success("查询成功", user);
    }
    return Result.fail("没找到");
  }

  @GetMapping("/page")
  public Result page(int page, int pageSize, User user) {
    Page<User> pageInfo = new Page<>(page, pageSize);

    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper
        .like(user.getUsername() != null, User::getUsername, user.getUsername())
        .like(user.getNickname() != null, User::getNickname, user.getNickname())
        .like(user.getAddress() != null, User::getAddress, user.getAddress())
        .like(user.getEmail() != null, User::getEmail, user.getEmail())
        .like(user.getPhone() != null, User::getPhone, user.getPhone());

    queryWrapper.orderByDesc(User::getId);

    userService.page(pageInfo, queryWrapper);

    return Result.success("查询成功", pageInfo);

  }

  // 导出功能
  @GetMapping("/export")
  public Result export(HttpServletResponse response) throws Exception {
    // 从数据库查询所有数据
    List<User> list = userService.list();

    // 写到浏览器
    ExcelWriter writer = ExcelUtil.getWriter(true);

    writer.addHeaderAlias("username", "用户名");
    writer.addHeaderAlias("nickname", "昵称");
    writer.addHeaderAlias("email", "邮箱");
    writer.addHeaderAlias("phone", "电话");
    writer.addHeaderAlias("address", "地址");

    writer.write(list, true);

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=uts-8");
    String fileName = URLEncoder.encode("用户信息", "UTF-8");
    response.setHeader("Content-Disposition", "attachmemt;filename=" + fileName + ".xlsx");

    ServletOutputStream out = response.getOutputStream();
    writer.flush(out, true);
    out.close();
    writer.close();

    return Result.success("导出成功");
  }

  // 导入功能
  @PostMapping("/import")
  public Result imp(MultipartFile file) throws Exception {
    InputStream inputStream = file.getInputStream();
    ExcelReader reader = ExcelUtil.getReader(inputStream);
    reader.addHeaderAlias("用户名", "username");
    reader.addHeaderAlias("昵称", "nickname");
    reader.addHeaderAlias("邮箱", "email");
    reader.addHeaderAlias("电话", "phone");
    reader.addHeaderAlias("地址", "address");

    List<User> list = reader.readAll(User.class);
    userService.saveBatch(list);
    return Result.success("导入成功");
  }

}


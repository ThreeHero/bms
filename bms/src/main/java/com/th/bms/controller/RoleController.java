package com.th.bms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bms.common.Result;
import com.th.bms.entity.Role;
import com.th.bms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

  @Autowired
  private RoleService roleService;

  // 增
  @PostMapping
  public Result save(@RequestBody Role role) {
    roleService.save(role);
    return Result.success("新增成功");
  }

  // 删
  @DeleteMapping("/{id}")
  public Result removeById(@PathVariable Integer id) {
    roleService.removeById(id);
    return Result.success("删除成功");
  }

  // 改
  @PutMapping
  public Result update(@RequestBody Role role) {
    roleService.updateById(role);
    return Result.success("修改成功");
  }

  // 查
  @GetMapping("/{id}")
  public Result getById(@PathVariable Integer id) {
    Role role = roleService.getById(id);
    return Result.success("查询成功", role);
  }

  @GetMapping("/page")
  public Result page(int page, int pageSize, String name) {
    Page<Role> pageInfo = new Page<>(page, pageSize);

    LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper.like(name != null, Role::getName, name);
    queryWrapper.orderByDesc(Role::getId);

    return Result.success("查询成功", pageInfo);
  }
}

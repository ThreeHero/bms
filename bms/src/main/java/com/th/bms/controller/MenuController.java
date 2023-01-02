package com.th.bms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bms.common.Result;
import com.th.bms.entity.Menu;
import com.th.bms.entity.Role;
import com.th.bms.entity.RoleMenu;
import com.th.bms.service.MenuService;
import com.th.bms.service.RoleMenuService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/menu")
@Slf4j
public class MenuController {

  @Autowired
  private MenuService menuService;

  @Autowired
  private RoleMenuService roleMenuService;

  // 增
  @PostMapping
  public Result save(Menu menu) {
     menuService.save(menu);
     return Result.success("新增成功");
  }
  // 删除
  @DeleteMapping("/{id}")
  public Result removeById(@PathVariable Integer id) {
    menuService.removeById(id);
    return Result.success("删除成功");
  }

  // 改
  @PutMapping
  public Result update(@RequestBody Menu menu) {
    menuService.updateById(menu);
    return Result.success("修改成功");
  }

  @GetMapping("/{id}")
  public Result getOne(@PathVariable Integer id) {
    Menu menu = menuService.getById(id);
    return Result.success("查询成功", menu);
  }

  @GetMapping
  public Result getAll() {
    List<Menu> menuList = menuService.list();
    return Result.success("查询成功", menuList);
  }

  // 根据角色id查询菜单
  @GetMapping("role/{roleId}")
  public Result getMenu(@PathVariable Integer roleId) {
    LambdaQueryWrapper<RoleMenu>  queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(roleId != null, RoleMenu::getRoleId, roleId);
    List<RoleMenu> roleMenuList = roleMenuService.list(queryWrapper);
    List<Integer> ids = new ArrayList<>();
    for (RoleMenu roleMenu: roleMenuList) {
      ids.add(roleMenu.getMenuId());
    }
    List<Menu> menus = menuService.listByIds(ids);
    return Result.success("查询成功", menus);
  }
}

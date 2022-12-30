package com.th.bms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.util.List;
import lombok.Data;

@Data
public class Menu {

  private Integer id;
  private String icon;
  private String name;
  private String path;
  private String description;

  @TableField(exist = false)
  private List<Menu> children;

}

package com.th.bms.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.util.Date;
import lombok.Data;

@Data
public class User {

  private Long id;
  private String username;
  private String password;
  private String nickname;
  private String email;
  private String phone;
  private String address;
  @TableField("avatar_url")
  private String avatarUrl;

  @TableField("create_time")
  private Date createTime;

  // 逻辑删除
  @TableLogic
  private Boolean isDeleted;

}

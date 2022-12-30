package com.th.bms.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("file")
public class Files {

  private Integer id;
  private String name;
  private String type;
  private Long size;
  private String url;
  private String md5;

  @TableLogic
  private Boolean isDeleted;

}

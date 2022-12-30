package com.th.bms.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import java.util.Collections;

/**
 * mp 代码生成器
 */
public class CodeGenerator {
  public static void main(String[] args) {
    generate();
  }

  private static void generate() {
    FastAutoGenerator.create("jdbc:mysql://localhost:3306/bms", "root", "admin123")
      .globalConfig(builder -> {
        builder.author("threehero") // 设置作者
            // .fileOverride() // 覆盖已生成文件
            .outputDir("D:\\code\\project\\bms\\bms\\src\\main\\java"); // 指定输出目录
      })
      .packageConfig(builder -> {
        builder.parent("com.th.bms") // 设置父包名
            .moduleName("") // 设置父包模块名
            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\code\\project\\bms\\bms\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
      })
      .strategyConfig(builder -> {
        builder.entityBuilder().enableLombok();
        builder
            .addInclude("user") // 设置需要生成的表名
            .addTablePrefix(""); // 设置过滤表前缀
      })
      .execute();
  }
}

package com.th.bms.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.th.bms.common.Result;
import com.th.bms.entity.Files;
import com.th.bms.entity.User;
import com.th.bms.service.FileService;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

  @Value("${files.upload.path}")
  private String fileUploadPath; // 保存的路径
  @Value("${server.ip}")
  private String serverIp;
  @Value("${server.port}")
  private String serverPort;

  @Autowired
  private FileService fileService;


  /**
   * 文件上传
   * @param file
   * @return
   * @throws IOException
   */
  @PostMapping("/upload")
  public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
    String originalFilename = file.getOriginalFilename(); // 传入的文件名
    String type = FileUtil.extName(originalFilename); // 后缀名
    // String contentType = file.getContentType(); // 类型/后缀名
    long size = file.getSize(); // 文件 大小 (B) -> (KB) / 1024

    // 定义一个文件唯一的标识码
    String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type; // a6602e595f474d17a3b4981c60728cfd.png

    File uploadFile = new File(fileUploadPath + fileUUID); // 文件路径

    // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
    File parentFile = uploadFile.getParentFile();
    if(!parentFile.exists()) {
      parentFile.mkdirs();
    }

    String url;

    // 获取文件的md5
    String md5 = SecureUtil.md5(file.getInputStream());

    // 从数据库查询是否存在相同的记录
    Files dbFiles = getFileByMd5(md5);
    if (dbFiles != null) {
      url = dbFiles.getUrl();

      return Result.fail("已有相同文件, 请勿重复上传, 文件地址: " + url);
    } else {
      // 上传文件到磁盘
      file.transferTo(uploadFile);
      // 数据库若不存在重复文件，则不删除刚才上传的文件
      url = "http://" + serverIp + ":" +  serverPort + "/file/" + fileUUID;
    }


    // 存储数据库
    Files saveFile = new Files();
    saveFile.setName(originalFilename);
    saveFile.setType(type);
    saveFile.setSize(size / 1024); // 单位 kb
    saveFile.setUrl(url);
    saveFile.setMd5(md5);
    fileService.save(saveFile);

    return Result.success("上传成功", url);
  }

  private Files getFileByMd5(String md5) {
    // 查询文件的md5是否存在
    QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("md5", md5);

    List<Files> filesList = fileService.list(queryWrapper);
    return filesList.size() == 0 ? null : filesList.get(0);
  }


  /**
   * 文件下载
   * @param fileUUID
   * @param response
   * @throws IOException
   */
  @GetMapping("/{fileUUID}")
  public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
    // 根据文件的唯一标识码获取文件
    File uploadFile = new File(fileUploadPath + fileUUID);
    // 设置输出流的格式
    ServletOutputStream os = response.getOutputStream();
    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID,
        StandardCharsets.UTF_8));
    response.setContentType("application/octet-stream");

    // 读取文件的字节流
    os.write(FileUtil.readBytes(uploadFile));
    os.flush();
    os.close();
  }

  // 文件分页查询
  @GetMapping("/page")
  public Result page(int page, int pageSize) {
    Page<Files> pageInfo = new Page<>(page, pageSize);

    LambdaQueryWrapper<Files> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByDesc(Files::getId);
    fileService.page(pageInfo, queryWrapper);

    return Result.success("查询成功", pageInfo);
  }

  /**
   * 删除文件
   */
  @DeleteMapping("/{id}")
  public Result removeById(@PathVariable Integer id) {
    fileService.removeById(id);
    return Result.success("删除成功");

  }

}

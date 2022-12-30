package com.th.bms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.th.bms.entity.Files;
import com.th.bms.mapper.FileMapper;
import com.th.bms.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements FileService {

}

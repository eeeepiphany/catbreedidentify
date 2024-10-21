package com.aida.cat.service;

import com.aida.cat.mapper.RecordMapper;
import com.aida.cat.model.Record;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RecordServiceImpl implements RecordService{

    @Override
    public boolean addRecord(Record record) {
        return recordMapper.addRecord(record) > 0;
    }


    @Resource
    private RecordMapper recordMapper;
    // @Resource是Java EE 6的注解，用于指示Spring容器自动注入资源。
    // Spring容器会自动注入一个RecordMapper的实现，这样RecordServiceImpl就可以使用它来执行数据库操作。


    @Override
    public boolean editRecord(Record record) {
        return recordMapper.editRecord(record) > 0;
    }

    @Override
    public Record findRecordById(int recordId) {
        return recordMapper.findRecordById(recordId);
    }

    @Override
    public List<Record> findAllRecord() {
        return recordMapper.findAllRecord();
    }

    @Override
    public PageInfo<Record> getRecordByPage(int page) {
        PageHelper.startPage(page, 5);
        System.out.println(new PageInfo<>(recordMapper.findAllRecord()));
        return new PageInfo<>(recordMapper.findAllRecord());
    }
}

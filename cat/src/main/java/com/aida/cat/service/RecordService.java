package com.aida.cat.service;

import com.aida.cat.model.Record;

import java.util.List;

import com.github.pagehelper.PageInfo;


public interface RecordService {
    boolean addRecord(Record record);
    boolean editRecord(Record record);
    Record findRecordById(int recordId);
    List<Record> findAllRecord();
    PageInfo<Record> getRecordByPage(int page);
//    PageInfo<HouseInfo> getHouseInfoByTitle(String title, int page);  不知道用不用加上
}

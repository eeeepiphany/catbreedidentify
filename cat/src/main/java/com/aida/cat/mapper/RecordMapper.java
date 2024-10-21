package com.aida.cat.mapper;

import com.aida.cat.model.Record;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RecordMapper {

    @Insert("insert into record(publisher, ai_breed, artifact_breed, file_path) " +
            "values(#{publisherName}, #{aiBreed}, #{artiBreed}, #{filePath})")
    int addRecord(Record record);

//    @Delete("delete from house_info where house_id=#{houseId}")       应该没有吧
//    int delHouseInfo(int houseId);

    @Update("update record set publisher=#{publisherName}, ai_breed=#{aiBreed}, artifact_breed=#{artiBreed} where record_id=#{recordId}")
    int editRecord(Record record);

    Record findRecordById(int recordId);

    List<Record> findAllRecord();



}

package com.aida.cat.controller;

import com.aida.cat.model.Record;
import com.aida.cat.service.RecordService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("record")
public class RecordController {
    @Resource
    private RecordService recordService;

//    @RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
//    public Map<String, Object> findAll() {
//        List<Record> data = recordService.findAllRecord();
//        Map<String, Object> map = new HashMap<>();
//        if (data == null) {
//            map.put("code", 500);
//        } else {
//            map.put("data", data);
//            map.put("code", 200);
//        }
//        return map;
//    }

//    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    public Map<String, Object> addRecord(@RequestBody Record record) {
//        Map<String, Object> map = new HashMap<>();
//        if (recordService.addRecord(record)) {
//            map.put("code", 200);
//        } else {
//            map.put("code", 500);
//        }
//        return map;
//    }

    @RequestMapping(value = "edit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Map<String, Object> editRecord(@RequestBody Record record) {
        System.out.println(record);
        Map<String, Object> map = new HashMap<>();
        if (recordService.editRecord(record)) {
            map.put("code", 200);
        } else {
            map.put("code", 500);
        }
        return map;
    }



    @RequestMapping(value = "find/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Map<String, Object> findRecord(@PathVariable("id") int id) {
        Record data = recordService.findRecordById(id);
        Map<String, Object> map = new HashMap<>();
        if (data == null) {
            map.put("code", 500);
        } else {
            map.put("data", data);
            map.put("code", 200);
        }
        return map;
    }

    @RequestMapping(value = "page/{page}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public Map<String, Object> findHouseByPage(@PathVariable("page") int page) {
        PageInfo<Record> data = recordService.getRecordByPage(page);
        Map<String, Object> map = new HashMap<>();
        if (data == null) {
            map.put("code", 500);
        } else {
            map.put("data", data);
            map.put("code", 200);
        }
        return map;
    }

}

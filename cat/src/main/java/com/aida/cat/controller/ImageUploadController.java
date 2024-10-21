package com.aida.cat.controller;

import com.aida.cat.model.Record;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aida.cat.service.RecordService;

import javax.annotation.Resource;


@RestController
@CrossOrigin
@RequestMapping("image")
public class ImageUploadController {

    private String filePath;  // 类级别的变量


    @Resource
    private RecordService recordService;

    @Value("C:\\Users\\错误\\Desktop\\demo")
    private String uploadPath;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            long timestamp = System.currentTimeMillis();
            String fileName = timestamp + "_" + file.getOriginalFilename();
            String filePath = uploadPath + "\\" + fileName;
            file.transferTo(new File(filePath));

            // 创建一个Map来存放响应体数据
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "上传成功");
            responseBody.put("fileName", fileName);

            // 返回包含文件名的JSON对象
            return ResponseEntity.ok(responseBody);


        } catch (IOException e) {
            e.printStackTrace();
            // 创建一个Map来存放错误响应体数据
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "上传失败");
            // 返回错误响应，状态码设置为内部服务器错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }





    @PostMapping("/aiRecognition")
    public ResponseEntity<?> aiRecognition(@RequestParam("file") MultipartFile file, @RequestParam("publisherName") String publisherName) { // 接收publisher作为字符串) {
        try {

            // 保存文件到指定路径
            String fileName = file.getOriginalFilename();
            String filePath = uploadPath + "/" + fileName;
            file.transferTo(new File(filePath));

            // 调用Python AI模型进行预测
            String result = callPythonAIModel(filePath);

            // 创建响应体
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("result", result);

            // 创建一个HttpHeaders对象来设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("charset", "UTF-8");



            if(result.equals("The image entered is not a cat picture")){
            }else{

                // 创建Record对象并添加到数据库
                Record newRecord = new Record(null, publisherName, result, "null", filePath); // 填充正确的值
                recordService.addRecord(newRecord);     // 调用RecordService的addRecord方法
            }



            // 返回预测结果，并设置响应头
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(responseBody);



        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "AI识别失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




    }


    private String callPythonAIModel(String filePath) throws IOException, InterruptedException {
        // 获取Anaconda环境中的Python解释器路径
        String anacondaPythonPath = "D:\\developer_tools\\python\\Anaconda3\\envs\\pytorch\\python.exe";

        // 构建Python脚本的命令，包括传递图片路径作为参数
        String pythonScriptPath = "D:\\workspace_developer\\workspace_pycharm\\ai_learning\\predict.py";
        // 构建命令数组
        List<String> command = new ArrayList<>();
        command.add(anacondaPythonPath);  // 使用Anaconda环境中的Python解释器
        command.add(pythonScriptPath);
        command.add(filePath);

        // 使用ProcessBuilder执行命令
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true); // 合并错误流和标准流

        // 启动Python脚本
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        StringBuilder output = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        String line;

        // 读取输出
        while ((line = reader.readLine()) != null) {
            output.append(line).append(System.lineSeparator());
        }
        // 读取错误输出
        while ((line = errReader.readLine()) != null) {
            errors.append(line).append(System.lineSeparator());
        }

        // 等待脚本执行完成
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            // 打印错误信息以供调试
            throw new IOException("Python script failed with exit code " + exitCode + " and errors: " + errors.toString());
        }
        return output.toString().trim(); // 返回输出并去除尾部换行符
    }
}


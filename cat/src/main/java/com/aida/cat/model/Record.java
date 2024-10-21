package com.aida.cat.model;

import lombok.Data;

@Data
public class Record {
    private Integer recordId;
    private String publisherName;
    private String aiBreed;
    private String artiBreed;
    private String filePath;

    public Record() {
    }

    public Record(Integer recordId, String publisherName, String aiBreed, String artiBreed, String filePath) {
        this.recordId = recordId;
        this.publisherName = publisherName;
        this.aiBreed = aiBreed;
        this.artiBreed = artiBreed;
        this.filePath = filePath;
    }
}

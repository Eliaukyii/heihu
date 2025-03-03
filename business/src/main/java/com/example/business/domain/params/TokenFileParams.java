package com.example.business.domain.params;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class TokenFileParams {

    public static String filePath;

    public static String fileName;

    @Value("${file.token.filePath}")
    public void setFilePath(String filePath){
        TokenFileParams.filePath = filePath;
    }

    @Value("${file.token.fileName}")
    public void setFileName(String fileName) {
        TokenFileParams.fileName = fileName;
    }


}

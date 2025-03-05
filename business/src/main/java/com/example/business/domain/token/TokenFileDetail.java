package com.example.business.domain.token;

import lombok.Data;

import java.util.Date;

@Data
public class TokenFileDetail {

    /**
     * 存储token文件里的token
     */
    private String token;

    /**
     * 存储token文件的最后修改时间
     */
    private Date lastModified;


}

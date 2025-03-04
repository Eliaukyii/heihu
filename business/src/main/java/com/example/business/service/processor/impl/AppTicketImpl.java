package com.example.business.service.processor.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.business.constant.MsgInfoData;
import com.example.business.constant.MsgType;
import com.example.business.constant.SaveToken;
import com.example.business.domain.MsgInfo;
import com.example.business.domain.TokenFileDetail;
import com.example.business.domain.params.TokenFileParams;
import com.example.business.domain.response.ErpAuthResponse;
import com.example.business.service.processor.Processor;
import com.example.business.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Component
@Slf4j
public class AppTicketImpl implements Processor {

    public static TokenFileParams tokenFileParams = new TokenFileParams();

    /**
     * 大约一天刷新一次token
     */
    public static long timeSeconds = 24 * 60 * 60 *1000L;

    @Override
    public void handle(MsgInfo msgInfo) {
        MsgInfoData.MSG_INFO = msgInfo;
        TokenFileDetail fileDetails = this.getFileDetails();
        if (fileDetails != null) {
            Date lastModified = fileDetails.getLastModified();
            String token = fileDetails.getToken();

            Date now = new Date();
            //token文件为空或超过48小时，重新获取erp的token
            if (StringUtils.isBlank(token) || (now.getTime() - lastModified.getTime()) > timeSeconds) {
                ErpAuthResponse erpToken = TokenUtil.getErpToken();
//                SaveToken.erpAuthResponse = erpToken;
                SaveToken.erpToken = erpToken.getValue().getAccessToken();

                this.writeTokenToFile();
            } else {
                log.info("token文件存储的token未超期，无需重新获取erp的token");
            }

        }

    }

    private void writeTokenToFile() {
        try {
            Path tokenFilePath = Paths.get(tokenFileParams.filePath + tokenFileParams.fileName);
            Files.write(tokenFilePath, SaveToken.erpToken.getBytes());
            log.info("token写入文件");
        } catch (IOException e) {
            log.error("获取token并写入文件失败：" + e.getMessage());
        }

    }

    @Override
    public String getType() {
        return MsgType.APP_TICKET;
    }

    public static TokenFileDetail getFileDetails() {

        String filePath = tokenFileParams.filePath + tokenFileParams.fileName;
        TokenFileDetail tokenFileDetail = new TokenFileDetail();
        try {
            // 创建文件对象
            File file = new File(filePath);

            // 检查文件是否存在
            if (!file.exists()) {
                log.error("文件不存在: " + filePath);
                return tokenFileDetail;
            }

            // 读取文件内容
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            tokenFileDetail.setToken(content);
            // 获取文件的最后修改时间
            Date lastModified = new Date(file.lastModified());
            tokenFileDetail.setLastModified(lastModified);

            // 打印结果
            log.info("获取erpToken - 文件内容：" + content);
            log.info("获取erpToken - 最后修改时间：" + lastModified);
        } catch (IOException e) {
            log.error("获取erpToken - 读取文件时出错: " + e.getMessage());
        }
        return tokenFileDetail;
    }

}

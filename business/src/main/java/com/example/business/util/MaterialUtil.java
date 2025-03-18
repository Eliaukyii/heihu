package com.example.business.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.business.constant.SaveToken;
import com.example.business.domain.MaterialDefinitionErp;
import com.example.business.domain.MaterialDefinitionHeihu;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamErp;
import com.example.business.domain.response.HeihuAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MaterialUtil {

    public static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();
    public static ApiParamsErp apiParamsErp = new ApiParamsErp();

    public static void MaterialHandle(MsgInfo msgInfo){

        //获取存货code
        String code = msgInfo.getBizContent().getCode();
        //查询erp中存货详情
        //请求erp获取详细信息
        WebClient webClientErp = WebClient.builder()
                .baseUrl(apiParamsErp.url)
                .defaultHeader("openToken", SaveToken.erpToken)
                .defaultHeader("appKey", apiParamsErp.appKey)
                .defaultHeader("appSecret", apiParamsErp.appSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String SelectFields = "Code,Name,Specification,InventoryClass.Code,InventoryClass.Name,Unit.Code,Unit.Name,IsBatch,BatchCodeRule,Warehouse.Code,Warehouse.Name,InvLocation.Code,InvLocation.Name,Finfout,ControledLevel,FinfoutAttr,SafeQuantity";
        RequestParamErp requestParamErp = new RequestParamErp(code, SelectFields);

        List<MaterialDefinitionErp> list = new ArrayList<>();
        list = webClientErp.post()
                .uri(apiParamsErp.customerUri)
                .bodyValue(requestParamErp)
                .retrieve()
                .bodyToMono(list.getClass())
                .block();
        if (CollectionUtils.isEmpty(list)) {
            log.error("未查询到erp中存货详情");
            return;
        }

        log.info("物料定义，存货新增/修改 - 根据code请求erp的响应数据：" + list);

        //组装数据
        MaterialDefinitionHeihu data = MaterialUtil.disposeData(list);

        //请求黑湖
        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.url)
                .defaultHeader("X-AUTH", SaveToken.getHeihuToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        HeihuAuthResponse heihuResponse = webClient.post()
                .uri(apiParamsHeihu.materialUri)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(HeihuAuthResponse.class)
                .block();
        if (!heihuResponse.getCode().equals("200")) {
            log.error("物料定义，存货新增/修改失败，失败信息：" + heihuResponse.getMessage());
        }
    }

    public static MaterialDefinitionHeihu disposeData(List<MaterialDefinitionErp> list) {
        MaterialDefinitionErp erp = list.get(list.size() - 1) ;
        MaterialDefinitionHeihu heihu = new MaterialDefinitionHeihu();
        heihu.setMaterialCode(erp.getCode());
        heihu.setMaterialName(erp.getName());
        heihu.setSpecification(erp.getSpecification());
        heihu.setMaterialCategoryCode(erp.getInventoryClass().getCode());
        heihu.setUnitName(erp.getUnit().getName());

        String isBatch = erp.getIsBatch();
        //若启用批次管理
        if ("True".equals(isBatch)) {
            heihu.setBatchManagementEnable("是");
            heihu.setBatchNoRuleCode("PC");
            heihu.setFifoEnabled("是");
            //先进先出开启后，管控等级传弱管控
            if ("是".equals(heihu.getFifoEnabled())) {
                heihu.setControlLevel("弱管控");
            }

        } else if ("False".equals(isBatch)) {
            //不传批次号规则
            //先进先出也不传或传否
//            heihu.setFifoEnabled("否");
        } else {
            log.error("不能识别的批次管理标志，仅识别True、False，相关erp信息：{}", erp);
        }

        heihu.setDefaultWarehouseCode(erp.getWarehouse().getCode());
        heihu.setDefaultLocationCode(erp.getInvLocation().getCode());

        heihu.setFifoAttr("批次号");
        heihu.setCust_field3__c(erp.getSafeQuantity());

        return heihu;
    }


}

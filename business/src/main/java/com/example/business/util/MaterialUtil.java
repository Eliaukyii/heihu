package com.example.business.util;

import com.example.business.constant.SaveToken;
import com.example.business.domain.MaterialDefinitionErp;
import com.example.business.domain.MaterialDefinitionHeihu;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamErp;
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
        List<MaterialDefinitionErp> ErpResponseData = webClientErp.post()
                .uri(apiParamsErp.customerUri)
                .bodyValue(requestParamErp)
                .retrieve()
                .bodyToMono(list.getClass())
                .block();
        log.info("物料定义，存货新增/修改 - 根据code请求erp的响应数据：" + ErpResponseData);

        //组装数据
        MaterialDefinitionHeihu data = MaterialUtil.disposeData(list);

        //请求黑湖
        WebClient webClient = WebClient.builder()
                .baseUrl(apiParamsHeihu.url)
                .defaultHeader("X-AUTH", SaveToken.getHeihuToken())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String heihuResponse = webClient.post()
                .uri(apiParamsHeihu.materialUri)
                .bodyValue(data)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("物料定义，存货新增/修改 - 请求黑湖响应数据：" + heihuResponse);
    }

    public static MaterialDefinitionHeihu disposeData(List<MaterialDefinitionErp> list) {
        MaterialDefinitionErp erp = list.get(0);
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
            log.info("不能识别的批次管理标志，仅识别True、False");
        }

        heihu.setDefaultWarehouseCode(erp.getWarehouse().getCode());
        heihu.setDefaultLocationCode(erp.getInvLocation().getCode());

        heihu.setFifoAttr("批次号");
        heihu.setCust_field3__c(erp.getSafeQuantity());

        return heihu;
    }


}

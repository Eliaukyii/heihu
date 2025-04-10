package com.example.business.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.business.constant.SaveToken;
import com.example.business.domain.MaterialDefinitionErp;
import com.example.business.domain.MaterialDefinitionHeihu;
import com.example.business.domain.MaterialDefinitionHeihuFileds;
import com.example.business.domain.msg.MsgInfo;
import com.example.business.domain.other.InvLocation;
import com.example.business.domain.other.Warehouse;
import com.example.business.domain.params.ApiParamsErp;
import com.example.business.domain.params.ApiParamsHeihu;
import com.example.business.domain.request.RequestParamErp;
import com.example.business.domain.response.HeihuAuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MaterialUtil {

    @Autowired
    private ObjectMapper objectMapperUpper;
    private static ApiParamsHeihu apiParamsHeihu = new ApiParamsHeihu();
    private static ApiParamsErp apiParamsErp = new ApiParamsErp();

    private static MaterialUtil materialUtil;

    @PostConstruct
    public void init() {
        materialUtil = this;
        materialUtil.objectMapperUpper = this.objectMapperUpper;
    }

    public static void MaterialHandle(MsgInfo msgInfo) {

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

        String SelectFields = "Code,Name,Specification,InventoryClass.Code,InventoryClass.Name,Unit.Code,Unit.Name,IsBatch,BatchCodeRule,Warehouse.Code,Warehouse.Name,InvLocation.Code,InvLocation.Name,Finfout,ControledLevel,FinfoutAttr,SafeQuantity,AvagCost";
        RequestParamErp requestParamErp = new RequestParamErp(code, SelectFields);


        String responseData = webClientErp.post()
                .uri(apiParamsErp.inventoryUri)
                .bodyValue(requestParamErp)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<MaterialDefinitionErp> list = new ArrayList<>();
        try {
            list = materialUtil.objectMapperUpper.readValue(responseData, new TypeReference<List<MaterialDefinitionErp>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("物料清单 - 数据转换失败");
            return;
        }

        if (CollectionUtils.isEmpty(list)) {
            log.error("未查询到erp中存货详情");
            return;
        }

        log.info("物料定义，存货新增/修改 - 根据code请求erp的响应数据：" + list);

        //组装数据
        MaterialDefinitionHeihu data = null;
        try {
            data = MaterialUtil.disposeData(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

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
            log.error("物料定义，存货新增/修改失败，失败信息：" + heihuResponse.getData() +"；" + heihuResponse.getMessage());
        }
    }

    public static MaterialDefinitionHeihu disposeData(List<MaterialDefinitionErp> list) throws ServerException {
        MaterialDefinitionErp erp = list.get(list.size() - 1);
        MaterialDefinitionHeihu heihu = new MaterialDefinitionHeihu();
        heihu.setMaterialCode(erp.getCode());
        heihu.setMaterialName(erp.getName());
        heihu.setSpecification(erp.getSpecification());
        heihu.setMaterialCategoryCode(erp.getInventoryClass().getCode());
        heihu.setUnitName(erp.getUnit().getName());

        String isBatch = erp.getIsBatch();
        //若物料分类编号以04开头，则不启用批次号传否
        String materialCategoryCode = heihu.getMaterialCategoryCode();
        if (materialCategoryCode.startsWith("04")) {
            heihu.setBatchManagementEnable("否");
        } else {
            heihu.setFifoAttr("批次号");
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
                throw new ServerException("不能识别的批次管理标志，仅识别True、False，相关erp信息：" + erp);
            }
        }

        Warehouse warehouse = erp.getWarehouse();
        InvLocation invLocation = erp.getInvLocation();
        heihu.setDefaultWarehouseCode(warehouse == null ? null : warehouse.getCode());
        heihu.setDefaultLocationCode(invLocation == null ? null : invLocation.getCode());

        List<MaterialDefinitionHeihuFileds> customFields = new ArrayList<>();
        customFields.add(new MaterialDefinitionHeihuFileds("cust_field3__c", erp.getSafeQuantity()));
        customFields.add(new MaterialDefinitionHeihuFileds("cust_field6__c", erp.getAvagCost()));

        heihu.setCustomFields(customFields);
        heihu.setEnableFlag("启用");

        return heihu;
    }


}

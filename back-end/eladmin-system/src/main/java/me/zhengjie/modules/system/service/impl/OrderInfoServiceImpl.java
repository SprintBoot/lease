/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.OrderInfo;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.OrderInfoRepository;
import me.zhengjie.modules.system.service.OrderInfoService;
import me.zhengjie.modules.system.service.dto.OrderInfoDto;
import me.zhengjie.modules.system.service.dto.OrderInfoQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.OrderInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author lease
* @date 2021-06-20
**/
@Service
@RequiredArgsConstructor
public class OrderInfoServiceImpl implements OrderInfoService {

    private final OrderInfoRepository orderInfoRepository;
    private final OrderInfoMapper orderInfoMapper;

    @Override
    public Map<String,Object> queryAll(OrderInfoQueryCriteria criteria, Pageable pageable){
        Page<OrderInfo> page = orderInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(orderInfoMapper::toDto));
    }

    @Override
    public List<OrderInfoDto> queryAll(OrderInfoQueryCriteria criteria){
        return orderInfoMapper.toDto(orderInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public OrderInfoDto findById(Integer orderId) {
        OrderInfo orderInfo = orderInfoRepository.findById(orderId).orElseGet(OrderInfo::new);
        ValidationUtil.isNull(orderInfo.getOrderId(),"OrderInfo","orderId",orderId);
        return orderInfoMapper.toDto(orderInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderInfoDto create(OrderInfo resources) {
        return orderInfoMapper.toDto(orderInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrderInfo resources) {
        OrderInfo orderInfo = orderInfoRepository.findById(resources.getOrderId()).orElseGet(OrderInfo::new);
        ValidationUtil.isNull( orderInfo.getOrderId(),"OrderInfo","id",resources.getOrderId());
        orderInfo.copy(resources);
        orderInfoRepository.save(orderInfo);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer orderId : ids) {
            orderInfoRepository.deleteById(orderId);
        }
    }

    @Override
    public void download(List<OrderInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OrderInfoDto orderInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("顾客id", orderInfo.getConsumerId());
            map.put("商品id", orderInfo.getGoodsId());
            map.put("状态", orderInfo.getStatus());
            map.put("下单时间", orderInfo.getOrderTime());
            map.put("支付时间", orderInfo.getPayTime());
            map.put("发货时间", orderInfo.getSendTime());
            map.put("归还时间", orderInfo.getReturnTime());
            map.put("签收时间", orderInfo.getReceiveTime());
            map.put("租用时长", orderInfo.getLeaseDuration());
            map.put("租价", orderInfo.getLeasePrice());
            map.put("是否买断", orderInfo.getIsBuy());
            map.put("原价", orderInfo.getOrginPrice());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
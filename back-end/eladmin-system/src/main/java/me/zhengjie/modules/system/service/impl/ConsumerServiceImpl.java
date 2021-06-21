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

import me.zhengjie.modules.system.domain.Consumer;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.ConsumerRepository;
import me.zhengjie.modules.system.service.ConsumerService;
import me.zhengjie.modules.system.service.dto.ConsumerDto;
import me.zhengjie.modules.system.service.dto.ConsumerQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.ConsumerMapper;
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
* @date 2021-06-21
**/
@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final ConsumerMapper consumerMapper;

    @Override
    public Map<String,Object> queryAll(ConsumerQueryCriteria criteria, Pageable pageable){
        Page<Consumer> page = consumerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(consumerMapper::toDto));
    }

    @Override
    public List<ConsumerDto> queryAll(ConsumerQueryCriteria criteria){
        return consumerMapper.toDto(consumerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }


    @Override
    @Transactional
    public ConsumerDto findById(Integer consumerId) {
        Consumer consumer = consumerRepository.findById(consumerId).orElseGet(Consumer::new);
        ValidationUtil.isNull(consumer.getConsumerId(),"Consumer","consumerId",consumerId);
        return consumerMapper.toDto(consumer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsumerDto create(Consumer resources) {
        return consumerMapper.toDto(consumerRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Consumer resources) {
        Consumer consumer = consumerRepository.findById(resources.getConsumerId()).orElseGet(Consumer::new);
        ValidationUtil.isNull( consumer.getConsumerId(),"Consumer","id",resources.getConsumerId());
        consumer.copy(resources);
        consumerRepository.save(consumer);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer consumerId : ids) {
            consumerRepository.deleteById(consumerId);
        }
    }

    @Override
    public void download(List<ConsumerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ConsumerDto consumer : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" openId",  consumer.getOpenId());
            map.put(" nickname",  consumer.getNickname());
            map.put(" phone",  consumer.getPhone());
            map.put(" gender",  consumer.getGender());
            map.put(" createTime",  consumer.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
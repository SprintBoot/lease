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

import me.zhengjie.modules.system.domain.Goods;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.GoodsRepository;
import me.zhengjie.modules.system.service.GoodsService;
import me.zhengjie.modules.system.service.dto.GoodsDto;
import me.zhengjie.modules.system.service.dto.GoodsQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.GoodsMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
* @date 2021-06-19
**/
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodsMapper goodsMapper;

    private final UserRepository userRepository;

    @Override
    public Map<String,Object> queryAll(GoodsQueryCriteria criteria, Pageable pageable){
        UserDetails user =  SecurityUtils.getCurrentUser();
        User u = userRepository.findByUsername(user.getUsername());
        if(u.getDept().getId()==2){ // 厂商
            criteria.setBussId(u.getId().intValue());
        }
        Page<Goods> page = goodsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(goodsMapper::toDto));
    }

    @Override
    public List<GoodsDto> queryAll(GoodsQueryCriteria criteria){
        UserDetails user =  SecurityUtils.getCurrentUser();
        User u = userRepository.findByUsername(user.getUsername());
        if(u.getDept().getId()==2){ // 厂商
            criteria.setBussId(u.getId().intValue());
        }
        return goodsMapper.toDto(goodsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public List<GoodsDto> queryAllByConsumer(GoodsQueryCriteria criteria){
        return goodsMapper.toDto(goodsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public GoodsDto findById(Integer goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseGet(Goods::new);
        ValidationUtil.isNull(goods.getGoodsId(),"Goods","goodsId",goodsId);
        return goodsMapper.toDto(goods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsDto create(Goods resources) {
        UserDetails user =  SecurityUtils.getCurrentUser();
        User u = userRepository.findByUsername(user.getUsername());
        resources.setBussId(u.getId().intValue());
        resources.setState(0);
        return goodsMapper.toDto(goodsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Goods resources) {
        UserDetails user =  SecurityUtils.getCurrentUser();
        User u = userRepository.findByUsername(user.getUsername());
        Goods goods = goodsRepository.findById(resources.getGoodsId()).orElseGet(Goods::new);
        ValidationUtil.isNull( goods.getGoodsId(),"Goods","id",resources.getGoodsId());
        goods.copy(resources);
        goodsRepository.save(goods);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer goodsId : ids) {
            goodsRepository.deleteById(goodsId);
        }
    }

    @Override
    public void download(List<GoodsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (GoodsDto goods : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("厂商id", goods.getBussId());
            map.put("类别id", goods.getCateId());
            map.put("商品名称", goods.getGoodsName());
            map.put("日租价格", goods.getLeasePrice());
            map.put("买断价", goods.getBuyPrice());
            map.put("图片地址", goods.getImg());
            map.put("审核人id", goods.getVerifyBy());
            map.put("状态", goods.getState());
            map.put("描述", goods.getDetail());
            map.put("创建者", goods.getCreateBy());
            map.put("更新者", goods.getUpdateBy());
            map.put("创建日期", goods.getCreateTime());
            map.put("更新时间", goods.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
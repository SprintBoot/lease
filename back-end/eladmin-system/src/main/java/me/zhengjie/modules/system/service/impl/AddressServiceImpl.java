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

import me.zhengjie.modules.system.domain.Address;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.repository.AddressRepository;
import me.zhengjie.modules.system.service.AddressService;
import me.zhengjie.modules.system.service.dto.AddressDto;
import me.zhengjie.modules.system.service.dto.AddressQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.AddressMapper;
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
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public Map<String,Object> queryAll(AddressQueryCriteria criteria, Pageable pageable){
        Page<Address> page = addressRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(addressMapper::toDto));
    }

    @Override
    public List<AddressDto> queryAll(AddressQueryCriteria criteria){
        return addressMapper.toDto(addressRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public AddressDto findById(Integer addressId) {
        Address address = addressRepository.findById(addressId).orElseGet(Address::new);
        ValidationUtil.isNull(address.getAddressId(),"Address","addressId",addressId);
        return addressMapper.toDto(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddressDto create(Address resources) {
        return addressMapper.toDto(addressRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Address resources) {
        Address address = addressRepository.findById(resources.getAddressId()).orElseGet(Address::new);
        ValidationUtil.isNull( address.getAddressId(),"Address","id",resources.getAddressId());
        address.copy(resources);
        addressRepository.save(address);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer addressId : ids) {
            addressRepository.deleteById(addressId);
        }
    }

    @Override
    public void download(List<AddressDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AddressDto address : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" address",  address.getAddress());
            map.put(" consumerId",  address.getConsumerId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
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
package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Address;
import me.zhengjie.modules.system.service.dto.AddressDto;
import me.zhengjie.modules.system.service.dto.AddressQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @description 服务接口
* @author lease
* @date 2021-06-21
**/
public interface AddressService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(AddressQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<AddressDto>
    */
    List<AddressDto> queryAll(AddressQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param addressId ID
     * @return AddressDto
     */
    AddressDto findById(Integer addressId);

    /**
    * 创建
    * @param resources /
    * @return AddressDto
    */
    AddressDto create(Address resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(Address resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Integer[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<AddressDto> all, HttpServletResponse response) throws IOException;
}
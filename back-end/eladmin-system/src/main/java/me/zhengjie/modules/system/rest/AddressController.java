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
package me.zhengjie.modules.system.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.system.domain.Address;
import me.zhengjie.modules.system.service.AddressService;
import me.zhengjie.modules.system.service.dto.AddressQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author lease
* @date 2021-06-21
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/system/address管理")
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('address:list')")
    public void download(HttpServletResponse response, AddressQueryCriteria criteria) throws IOException {
        addressService.download(addressService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/system/address")
    @ApiOperation("查询api/system/address")
    @PreAuthorize("@el.check('address:list')")
    public ResponseEntity<Object> query(AddressQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(addressService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/system/address")
    @ApiOperation("新增api/system/address")
    @PreAuthorize("@el.check('address:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Address resources){
        return new ResponseEntity<>(addressService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/system/address")
    @ApiOperation("修改api/system/address")
    @PreAuthorize("@el.check('address:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Address resources){
        addressService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/system/address")
    @ApiOperation("删除api/system/address")
    @PreAuthorize("@el.check('address:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        addressService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
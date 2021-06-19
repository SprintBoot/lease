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
import me.zhengjie.modules.system.domain.LeaseMode;
import me.zhengjie.modules.system.service.LeaseModeService;
import me.zhengjie.modules.system.service.dto.LeaseModeQueryCriteria;
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
* @date 2021-06-19
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/system/leaseMode管理")
@RequestMapping("/api/leaseMode")
public class LeaseModeController {

    private final LeaseModeService leaseModeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('leaseMode:list')")
    public void download(HttpServletResponse response, LeaseModeQueryCriteria criteria) throws IOException {
        leaseModeService.download(leaseModeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/system/leaseMode")
    @ApiOperation("查询api/system/leaseMode")
    @PreAuthorize("@el.check('leaseMode:list')")
    public ResponseEntity<Object> query(LeaseModeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(leaseModeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/system/leaseMode")
    @ApiOperation("新增api/system/leaseMode")
    @PreAuthorize("@el.check('leaseMode:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody LeaseMode resources){
        return new ResponseEntity<>(leaseModeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/system/leaseMode")
    @ApiOperation("修改api/system/leaseMode")
    @PreAuthorize("@el.check('leaseMode:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody LeaseMode resources){
        leaseModeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/system/leaseMode")
    @ApiOperation("删除api/system/leaseMode")
    @PreAuthorize("@el.check('leaseMode:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        leaseModeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
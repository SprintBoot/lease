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
import me.zhengjie.modules.system.domain.Goods;
import me.zhengjie.modules.system.service.GoodsService;
import me.zhengjie.modules.system.service.dto.GoodsQueryCriteria;
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
@Api(tags = "api/system/goods管理")
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('goods:list')")
    public void download(HttpServletResponse response, GoodsQueryCriteria criteria) throws IOException {
        goodsService.download(goodsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/system/goods")
    @ApiOperation("查询api/system/goods")
    @PreAuthorize("@el.check('goods:list')")
    public ResponseEntity<Object> query(GoodsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(goodsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/system/goods")
    @ApiOperation("新增api/system/goods")
    @PreAuthorize("@el.check('goods:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Goods resources){
        return new ResponseEntity<>(goodsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/system/goods")
    @ApiOperation("修改api/system/goods")
    @PreAuthorize("@el.check('goods:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Goods resources){
        goodsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/system/goods")
    @ApiOperation("删除api/system/goods")
    @PreAuthorize("@el.check('goods:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        goodsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
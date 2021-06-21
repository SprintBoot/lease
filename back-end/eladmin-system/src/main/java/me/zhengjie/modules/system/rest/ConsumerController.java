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

import com.alibaba.fastjson.JSONObject;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.system.domain.Consumer;
import me.zhengjie.modules.system.service.ConsumerService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.ConsumerDto;
import me.zhengjie.modules.system.service.dto.ConsumerQueryCriteria;
import me.zhengjie.modules.system.utils.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author lease
* @date 2021-06-21
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/system/consumer管理")
@RequestMapping("/api/consumer")
public class ConsumerController {

    private final ConsumerService consumerService;

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @Log("用户登录")
    @ApiOperation("用户登录")
    @ResponseBody
    @GetMapping(value="/login")
    public void login(String code) {

        String appid = "wxeb551f75d6ae0406";

        String secret = "4aa1b8d1dbf31344e3abab18dde1a90a";

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        String str = WxUtil.httpRequest(url, "GET", null);

        JSONObject jsonObject = JSONObject.parseObject(str);

        String openid = jsonObject.get("openid").toString();

        ConsumerQueryCriteria criteria = new ConsumerQueryCriteria();
        criteria.setOpenId(openid);
        List<ConsumerDto> clist = consumerService.queryAll(criteria);

//        if(clist.size() ==0 ){
//
//        }




    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('consumer:list')")
    public void download(HttpServletResponse response, ConsumerQueryCriteria criteria) throws IOException {
        consumerService.download(consumerService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/system/consumer")
    @ApiOperation("查询api/system/consumer")
    @PreAuthorize("@el.check('consumer:list')")
    public ResponseEntity<Object> query(ConsumerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(consumerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/system/consumer")
    @ApiOperation("新增api/system/consumer")
    @PreAuthorize("@el.check('consumer:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Consumer resources){
        return new ResponseEntity<>(consumerService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/system/consumer")
    @ApiOperation("修改api/system/consumer")
    @PreAuthorize("@el.check('consumer:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Consumer resources){
        consumerService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除api/system/consumer")
    @ApiOperation("删除api/system/consumer")
    @PreAuthorize("@el.check('consumer:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        consumerService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
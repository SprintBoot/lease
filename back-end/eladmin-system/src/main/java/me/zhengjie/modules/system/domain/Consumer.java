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
package me.zhengjie.modules.system.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author lease
* @date 2021-06-21
**/
@Entity
@Data
@Table(name="consumer")
public class Consumer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumer_id")
    @ApiModelProperty(value = "用户id")
    private Integer consumerId;

    @Column(name = "open_id",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "openId")
    private String openId;

    @Column(name = "nickname")
    @ApiModelProperty(value = "nickname")
    private String nickname;

    @Column(name = "phone")
    @ApiModelProperty(value = "phone")
    private String phone;

    @Column(name = "gender")
    @ApiModelProperty(value = "gender")
    private String gender;

    @Column(name = "create_time")
    @ApiModelProperty(value = "createTime")
    private Timestamp createTime;

    public void copy(Consumer source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
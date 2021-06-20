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
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author lease
* @date 2021-06-19
**/
@Entity
@Data
@Table(name="lease_mode")
public class LeaseMode implements Serializable {

    @Id
    @Column(name = "mode_id")
    @ApiModelProperty(value = "modeId")
    private Integer modeId;

    @Column(name = "mode_name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "modeName")
    private String modeName;

    @Column(name = "mode_describe",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "modeDescribe")
    private String modeDescribe;

    public void copy(LeaseMode source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
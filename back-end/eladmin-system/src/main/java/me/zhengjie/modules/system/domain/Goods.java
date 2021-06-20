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
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author lease
* @date 2021-06-19
**/
@Entity
@Data
@Table(name="goods")
public class Goods extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @Column(name = "buss_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "厂商id")
    private Integer bussId;

    @Column(name = "cate_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "类别id")
    private Integer cateId;

    @Column(name = "goods_name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @Column(name = "lease_price",nullable = false)
    @NotNull
    @ApiModelProperty(value = "日租价格")
    private Double leasePrice;

    @Column(name = "buy_price",nullable = false)
    @NotNull
    @ApiModelProperty(value = "买断价")
    private Double buyPrice;

    @Column(name = "img",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "图片地址")
    private String img;

    @Column(name = "verify_by")
    @ApiModelProperty(value = "审核人id")
    private Integer verifyBy;

    @Column(name = "state",nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态")
    private Integer state;

    @Column(name = "detail")
    @ApiModelProperty(value = "描述")
    private String detail;

    @Column(name = "create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @Column(name = "update_by")
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(Goods source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
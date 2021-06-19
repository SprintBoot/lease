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
* @date 2021-06-20
**/
@Entity
@Data
@Table(name="order_info")
public class OrderInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    @ApiModelProperty(value = "订单id")
    private Integer orderId;

    @Column(name = "consumer_id")
    @ApiModelProperty(value = "顾客id")
    private Integer consumerId;

    @Column(name = "goods_id")
    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @Column(name = "order_time")
    @ApiModelProperty(value = "下单时间")
    private Timestamp orderTime;

    @Column(name = "pay_time")
    @ApiModelProperty(value = "支付时间")
    private Timestamp payTime;

    @Column(name = "send_time")
    @ApiModelProperty(value = "发货时间")
    private Timestamp sendTime;

    @Column(name = "return_time")
    @ApiModelProperty(value = "归还时间")
    private Timestamp returnTime;

    @Column(name = "receive_time")
    @ApiModelProperty(value = "签收时间")
    private Timestamp receiveTime;

    @Column(name = "lease_duration")
    @ApiModelProperty(value = "租用时长")
    private Integer leaseDuration;

    @Column(name = "lease_price")
    @ApiModelProperty(value = "租价")
    private Double leasePrice;

    @Column(name = "is_buy")
    @ApiModelProperty(value = "是否买断")
    private Boolean isBuy;

    @Column(name = "orgin_price")
    @ApiModelProperty(value = "原价")
    private Double orginPrice;

    public void copy(OrderInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
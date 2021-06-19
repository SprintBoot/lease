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
package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author lease
* @date 2021-06-20
**/
@Data
public class OrderInfoDto implements Serializable {

    /** 订单id */
    private Integer orderId;

    /** 顾客id */
    private Integer consumerId;

    /** 商品id */
    private Integer goodsId;

    /** 状态 */
    private Integer status;

    /** 下单时间 */
    private Timestamp orderTime;

    /** 支付时间 */
    private Timestamp payTime;

    /** 发货时间 */
    private Timestamp sendTime;

    /** 归还时间 */
    private Timestamp returnTime;

    /** 签收时间 */
    private Timestamp receiveTime;

    /** 租用时长 */
    private Integer leaseDuration;

    /** 租价 */
    private Double leasePrice;

    /** 是否买断 */
    private Boolean isBuy;

    /** 原价 */
    private Double orginPrice;
}
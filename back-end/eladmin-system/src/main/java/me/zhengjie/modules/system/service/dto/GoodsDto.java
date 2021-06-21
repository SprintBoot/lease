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
import me.zhengjie.base.BaseDTO;

import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author lease
* @date 2021-06-19
**/
@Data
public class GoodsDto extends BaseDTO implements Serializable {

    /** 商品id */
    private Integer goodsId;

    /** 厂商id */
    private Integer bussId;

    /** 类别id */
    private Integer cateId;

    /** 商品名称 */
    private String goodsName;

    /** 日租价格 */
    private Double leasePrice;

    /** 买断价 */
    private Double buyPrice;

    /** 图片地址 */
    private String img;

    /** 审核人id */
    private Integer verifyBy;

    /** 状态 */
    private Integer state;

    /** 描述 */
    private String detail;

}
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
import java.util.List;
import me.zhengjie.annotation.Query;

/**
* @website https://el-admin.vip
* @author lease
* @date 2021-06-20
**/
@Data
public class OrderInfoQueryCriteria{

    /** 精确 */
    @Query
    private Integer consumerId;

    /** 精确 */
    @Query
    private Integer goodsId;

    /** 精确 */
    @Query
    private Integer status;

    /** 精确 */
    @Query
    private Boolean isBuy;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> orderTime;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> payTime;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> sendTime;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> returnTime;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> receiveTime;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Integer> leaseDuration;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Double> leasePrice;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Double> orginPrice;
}
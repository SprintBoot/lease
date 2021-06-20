<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">顾客id</label>
        <el-input v-model="query.consumerId" clearable placeholder="顾客id" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">商品id</label>
        <el-input v-model="query.goodsId" clearable placeholder="商品id" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">状态</label>
        <el-input v-model="query.status" clearable placeholder="状态" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">是否买断</label>
        <el-input v-model="query.isBuy" clearable placeholder="是否买断" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker
          v-model="query.orderTime"
          start-placeholder="orderTimeStart"
          end-placeholder="orderTimeStart"
          class="date-item"
        />
        <date-range-picker
          v-model="query.payTime"
          start-placeholder="payTimeStart"
          end-placeholder="payTimeStart"
          class="date-item"
        />
        <date-range-picker
          v-model="query.sendTime"
          start-placeholder="sendTimeStart"
          end-placeholder="sendTimeStart"
          class="date-item"
        />
        <date-range-picker
          v-model="query.returnTime"
          start-placeholder="returnTimeStart"
          end-placeholder="returnTimeStart"
          class="date-item"
        />
        <date-range-picker
          v-model="query.receiveTime"
          start-placeholder="receiveTimeStart"
          end-placeholder="receiveTimeStart"
          class="date-item"
        />
        <date-range-picker
          v-model="query.leaseDuration"
          start-placeholder="leaseDurationStart"
          end-placeholder="leaseDurationStart"
          class="date-item"
        />
        <date-range-picker
          v-model="query.leasePrice"
          start-placeholder="leasePriceStart"
          end-placeholder="leasePriceStart"
          class="date-item"
        />
        <date-range-picker
          v-model="query.orginPrice"
          start-placeholder="orginPriceStart"
          end-placeholder="orginPriceStart"
          class="date-item"
        />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderId" label="订单id" />
        <el-table-column prop="consumerId" label="顾客id" />
        <el-table-column prop="goodsId" label="商品id" />
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            {{ dict.label.order_status[scope.row.status] }}
          </template>
        </el-table-column>
        <el-table-column prop="orderTime" label="下单时间" />
        <el-table-column prop="payTime" label="支付时间" />
        <el-table-column prop="sendTime" label="发货时间" />
        <el-table-column prop="returnTime" label="归还时间" />
        <el-table-column prop="receiveTime" label="签收时间" />
        <el-table-column prop="leaseDuration" label="租用时长" />
        <el-table-column prop="leasePrice" label="租价" />
        <el-table-column prop="isBuy" label="是否买断" />
        <el-table-column prop="orginPrice" label="原价" />
        <el-table-column v-if="checkPer(['admin','orderInfo:edit','orderInfo:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudOrderInfo from '@/api/orderInfo'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { orderId: null, consumerId: null, goodsId: null, status: null, orderTime: null, payTime: null, sendTime: null, returnTime: null, receiveTime: null, leaseDuration: null, leasePrice: null, isBuy: null, orginPrice: null }
export default {
  name: 'OrderInfo',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['order_status'],
  cruds() {
    return CRUD({ title: 'api/system/orderInfo', url: 'api/orderInfo', idField: 'orderId', sort: 'orderId,desc', crudMethod: { ...crudOrderInfo }})
  },
  data() {
    return {
      permission: {
        edit: ['admin', 'orderInfo:edit']
      },
      rules: {
      },
      queryTypeOptions: [
        { key: 'consumerId', display_name: '顾客id' },
        { key: 'goodsId', display_name: '商品id' },
        { key: 'status', display_name: '状态' },
        { key: 'isBuy', display_name: '是否买断' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>

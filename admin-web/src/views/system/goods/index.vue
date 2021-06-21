<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">厂商id</label>
        <el-input v-model="query.bussId" clearable placeholder="厂商id" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">所属类别</label>
        <el-select v-model="query.cateId" filterable class="filter-item" @keyup.enter.native="crud.toQuery" placeholder="请选择">
          <el-option
            v-for="item in dict.category"
            :key="item.id"
            :label="item.label"
            :value="item.value" />
        </el-select>
        <label class="el-form-item-label">商品名称</label>
        <el-input v-model="query.goodsName" clearable placeholder="商品名称" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">状态</label>
        <el-select v-model="query.state"  class="filter-item" @keyup.enter.native="crud.toQuery" placeholder="请选择">
          <el-option
            v-for="item in dict.goods_status"
            :key="item.id"
            :label="item.label"
            :value="item.value" />
        </el-select>

        <date-range-picker
          v-model="query.leasePrice"
          start-placeholder="leasePriceStart"
          end-placeholder="leasePriceStart"
          class="date-item"
        />
        <date-range-picker
          v-model="query.buyPrice"
          start-placeholder="buyPriceStart"
          end-placeholder="buyPriceStart"
          class="date-item"
        />
        <rrOperation :crud="crud" />

      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="类别id" prop="cateId">
            <el-select v-model="form.cateId" filterable placeholder="请选择">
              <el-option
                v-for="item in dict.category"
                :key="item.id"
                :label="item.label"
                :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="商品名称" prop="goodsName">
            <el-input v-model="form.goodsName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="日租价格" prop="leasePrice">
            <el-input v-model="form.leasePrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="买断价" prop="buyPrice">
            <el-input v-model="form.buyPrice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="图片地址" prop="img">
            <el-input v-model="form.img" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.detail" :rows="3" type="textarea" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="bussId" label="厂商id" />
        <el-table-column prop="cateId" label="类别id">
          <template slot-scope="scope">
            {{ dict.label.category[scope.row.cateId] }}
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="leasePrice" label="日租价格" />
        <el-table-column prop="buyPrice" label="买断价" />
        <el-table-column prop="img" label="图片地址" />
        <el-table-column prop="verifyBy" label="审核人id" />
        <el-table-column prop="state" label="状态">
          <template slot-scope="scope">
            {{ dict.label.goods_status[scope.row.state] }}
          </template>
        </el-table-column>
        <el-table-column prop="detail" label="描述" />
        <el-table-column v-if="checkPer(['admin','goods:edit','goods:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
<!--            <udOperation-->
<!--              :data="scope.row"-->
<!--              :permission="permission"-->
<!--            />-->
            <el-button  v-permission="permission.examine" :loading="crud.status.cu === 2" :size="mini" type="primary"  icon="el-icon-edit" @click="enableGoods(scope.row)" />
            <el-button  v-permission="permission.put" :disabled="scope.row.state === 0" :loading="crud.status.cu === 2" :size="mini" type="primary" @click="putGoods(scope.row)" >
              {{ scope.row.state === 2 ? '下架' : '上架' + '商品 ' }}</el-button>
          </template>

        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudGoods from '@/api/goods'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { goodsId: null, bussId: null, cateId: null, goodsName: null, leasePrice: null, buyPrice: null, img: null, verifyBy: null, state: null, detail: null, createBy: null, updateBy: null, createTime: null, updateTime: null }
export default {
  name: 'Goods',
  components: { pagination, crudOperation, rrOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['category', 'goods_status'],
  cruds() {
    return CRUD({ title: 'api/system/goods', url: 'api/goods', idField: 'goodsId', sort: 'goodsId,desc', crudMethod: { ...crudGoods }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'goods:add'],
        edit: ['admin', 'goods:edit'],
        del: ['admin', 'goods:del'],
        examine: ['admin', 'goods:examine'],
        put: ['admin', 'goods:put']
      },
      rules: {
        bussId: [
          { required: true, message: '厂商id不能为空', trigger: 'blur' }
        ],
        cateId: [
          { required: true, message: '类别id不能为空', trigger: 'blur' }
        ],
        goodsName: [
          { required: true, message: '商品名称不能为空', trigger: 'blur' }
        ],
        leasePrice: [
          { required: true, message: '日租价格不能为空', trigger: 'blur' }
        ],
        buyPrice: [
          { required: true, message: '买断价不能为空', trigger: 'blur' }
        ],
        img: [
          { required: true, message: '图片地址不能为空', trigger: 'blur' }
        ],
        state: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'bussId', display_name: '厂商id' },
        { key: 'cateId', display_name: '类别id' },
        { key: 'goodsName', display_name: '商品名称' },
        { key: 'state', display_name: '状态' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },
    enableGoods(data) {
      this.$confirm('此操作将审核通过商品 ' + data.goodsName + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        data.state = 1
        crudGoods.edit(data).then(res => {
          this.crud.notify(this.dict.label.state[1] + '成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(() => {
          data.state = 1
        })
      }).catch(() => {
        data.state = 1
      })
    },
    putGoods(data) {
      let changeDate = 3
      if (data.state === 1 || data.state === 3) {
        changeDate = 2
      }
      this.$confirm('此操作将' + changeDate === 3 ? '上架' : '下架' + '商品 ' + data.goodsName + ', 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        data.state = changeDate
        crudGoods.edit(data).then(res => {
          this.crud.notify('操作成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
        }).catch(() => {
          data.state = changeDate
        })
      }).catch(() => {
        data.state = changeDate
      })
    }
  }
}
</script>

<style scoped>

</style>

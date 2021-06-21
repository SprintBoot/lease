<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">用户id</label>
        <el-input v-model="query.consumerId" clearable placeholder="用户id" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">openId</label>
        <el-input v-model="query.openId" clearable placeholder="openId" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">nickname</label>
        <el-input v-model="query.nickname" clearable placeholder="nickname" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
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
        <el-table-column prop="consumerId" label="用户id" />
        <el-table-column prop="openId" label="openId" />
        <el-table-column prop="nickname" label="nickname" />
        <el-table-column prop="phone" label="phone" />
        <el-table-column prop="gender" label="gender" />
        <el-table-column prop="createTime" label="createTime" />
        <el-table-column v-if="checkPer(['admin','consumer:edit','consumer:del'])" label="操作" width="150px" align="center">
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
import crudConsumer from '@/api/consumer'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { consumerId: null, openId: null, nickname: null, phone: null, gender: null, createTime: null }
export default {
  name: 'Consumer',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'api/system/consumer', url: 'api/consumer', idField: 'consumerId', sort: 'consumerId,desc', crudMethod: { ...crudConsumer }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'consumer:add'],
        edit: ['admin', 'consumer:edit'],
        del: ['admin', 'consumer:del']
      },
      rules: {
        openId: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'consumerId', display_name: '用户id' },
        { key: 'openId', display_name: 'openId' },
        { key: 'nickname', display_name: 'nickname' }
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

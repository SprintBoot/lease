import request from '@/utils/request'

export function edit(data) {
  return request({
    url: 'api/orderInfo',
    method: 'put',
    data
  })
}

export default { edit }

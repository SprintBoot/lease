const WXAPI = require('apifm-wxapi')
import Dialog from '@vant/weapp/dialog/dialog'

async function checkSession(){
  return new Promise((resolve, reject) => {
    wx.checkSession({
      success() {
        return resolve(true)
      },
      fail() {
        return resolve(false)
      }
    })
  })
}

// async function bindSeller() {
//   const token = wx.getStorageSync('token')
//   const referrer = wx.getStorageSync('referrer')
//   if (!token) {
//     return
//   }
//   if (!referrer) {
//     return
//   }
//   const res = await WXAPI.bindSeller({
//     token,
//     uid: referrer
//   })
// }

// 检测登录状态，返回 true / false
// async function checkHasLogined() {
//   const token = wx.getStorageSync('token')
//   if (!token) {
//     return false
//   }
//   const loggined = await checkSession()
//   if (!loggined) {
//     wx.removeStorageSync('token')
//     return false
//   }
//   const checkTokenRes = await WXAPI.checkToken(token)
//   if (checkTokenRes.code != 0) {
//     wx.removeStorageSync('token')
//     return false
//   }
//   return true
// }

// async function wxaCode(){
//   return new Promise((resolve, reject) => {
//     wx.login({
//       success(res) {
//         return resolve(res.code)
//       },
//       fail() {
//         wx.showToast({
//           title: '获取code失败',
//           icon: 'none'
//         })
//         return resolve('获取code失败')
//       }
//     })
//   })
// }

async function login(page){
  const _this = this
  const userInfo = wx.getStorageSync('userInfo')
  wx.login({
    success: function (res) {
      // const componentAppid = wx.getStorageSync('componentAppid')  
      if (userInfo) {
        console.log("缓存获取",userInfo)
      } else {
        wx.login({
          success: function (loginRes) {
              //获取用户信息
            wx.getUserInfo({
              success: function (infoRes) {
                wx.setStorageSync('userBase', JSON.parse(infoRes.rawData))
                console.log('===开始request请求');
                //请求服务端的登录接口
                wx.request({
                  url: 'http://127.0.0.1:8000/api/consumer/login',
                  data: {
                    code: loginRes.code,//临时登录凭证
                    rawData: infoRes.rawData,//用户非敏感信息
                    encrypteData: infoRes.encryptedData,//用户敏感信息
                    iv: infoRes.iv,//解密算法的向量
                  },
                  success: function (res) {
                    res = res.data;
                    wx.hideLoading();
                    wx.setStorageSync('userInfo',res)
                  },
                  fail: function (error) {
                    console.log(error);
                  }
                });
              }
            });
          }
        });
        
      }
    }
  })
  return userInfo
}

// async function authorize() {
//   // const code = await wxaCode()
//   // const resLogin = await WXAPI.login_wx(code)
//   // if (resLogin.code == 0) {bindSeller
//   //   wx.setStorageSync('token', resLogin.data.token)
//   //   wx.setStorageSync('uid', resLogin.data.uid)
//   //   return resLogin
//   // }
//   return new Promise((resolve, reject) => {
//     wx.login({
//       success: function (res) {
//         const code = res.code
        
//         // 下面开始调用注册接口
//         const componentAppid = wx.getStorageSync('componentAppid')
//         if (componentAppid) {
//           WXAPI.wxappServiceAuthorize({
//             code: code,
//             referrer: referrer
//           }).then(function (res) {
//             if (res.code == 0) {
//               wx.setStorageSync('token', res.data.token)
//               wx.setStorageSync('uid', res.data.uid)
//               resolve(res)
//             } else {
//               wx.showToast({
//                 title: res.msg,
//                 icon: 'none'
//               })
//               reject(res.msg)
//             }
//           })
//         } else {
//           WXAPI.authorize({
//             code: code,
//             referrer: referrer
//           }).then(function (res) {
//             if (res.code == 0) {
//               wx.setStorageSync('token', res.data.token)
//               wx.setStorageSync('uid', res.data.uid)
//               resolve(res)
//             } else {
//               wx.showToast({
//                 title: res.msg,
//                 icon: 'none'
//               })
//               reject(res.msg)
//             }
//           })
//         }
//       },
//       fail: err => {
//         reject(err)
//       }
//     })
//   })
// }

// function loginOut(){
//   wx.removeStorageSync('token')
//   wx.removeStorageSync('uid')
// }

// async function checkAndAuthorize (scope) {
//   return new Promise((resolve, reject) => {
//     wx.getSetting({
//       success(res) {
//         if (!res.authSetting[scope]) {
//           wx.authorize({
//             scope: scope,
//             success() {
//               resolve() // 无返回参数
//             },
//             fail(e){
//               console.error(e)
//               // if (e.errMsg.indexof('auth deny') != -1) {
//               //   wx.showToast({
//               //     title: e.errMsg,
//               //     icon: 'none'
//               //   })
//               // }
//               wx.showModal({
//                 title: '无权操作',
//                 content: '需要获得您的授权',
//                 showCancel: false,
//                 confirmText: '立即授权',
//                 confirmColor: '#e64340',
//                 success(res) {
//                   wx.openSetting();
//                 },
//                 fail(e){
//                   console.error(e)
//                   reject(e)
//                 },
//               })
//             }
//           })
//         } else {
//           resolve() // 无返回参数
//         }
//       },
//       fail(e){
//         console.error(e)
//         reject(e)
//       }
//     })
//   })  
// }

module.exports = {
  // checkHasLogined: checkHasLogined,
  // wxaCode: wxaCode,
  login: login,
  // loginOut: loginOut,
  // checkAndAuthorize: checkAndAuthorize,
  // authorize: authorize,
  // bindSeller: bindSeller
}
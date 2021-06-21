const WXAPI = require('apifm-wxapi')
const CONFIG = require('config.js')
const AUTH = require('utils/auth')
App({
  onLaunch: function() {
    const subDomain = wx.getExtConfigSync().subDomain
    const componentAppid = wx.getExtConfigSync().componentAppid
    if (componentAppid) {
      wx.setStorageSync('appid', wx.getAccountInfoSync().miniProgram.appId)
      wx.setStorageSync('componentAppid', componentAppid)
    }
    if (subDomain) {
      WXAPI.init(subDomain)
    } else {
      WXAPI.init(CONFIG.subDomain)
      WXAPI.setMerchantId(CONFIG.merchantId)
    }
    
    const that = this;
    // 检测新版本
    const updateManager = wx.getUpdateManager()
    updateManager.onUpdateReady(function () {
      wx.showModal({
        title: '更新提示',
        content: '新版本已经准备好，是否重启应用？',
        success(res) {
          if (res.confirm) {
            // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
            updateManager.applyUpdate()
          }
        }
      })
    })
    /**
     * 初次加载判断网络情况
     * 无网络状态下根据实际情况进行调整
     */
    wx.getNetworkType({
      success(res) {
        const networkType = res.networkType
        if (networkType === 'none') {
          that.globalData.isConnected = false
          wx.showToast({
            title: '当前无网络',
            icon: 'loading',
            duration: 2000
          })
        }
      }
    });
    /**
     * 监听网络状态变化
     * 可根据业务需求进行调整
     */
    wx.onNetworkStatusChange(function(res) {
      if (!res.isConnected) {
        that.globalData.isConnected = false
        wx.showToast({
          title: '网络已断开',
          icon: 'loading',
          duration: 2000
        })
      } else {
        that.globalData.isConnected = true
        wx.hideToast()
      }
    })
    WXAPI.queryConfigBatch('WITHDRAW_MIN,ALLOW_SELF_COLLECTION,order_hx_uids,subscribe_ids,share_profile,adminUserIds,goodsDetailSkuShowType,shopMod,needIdCheck,balance_pay_pwd,shipping_address_gps,shipping_address_region_level,shopping_cart_vop_open,show_wx_quanzi,cps_open,recycle_open,categoryMod,hide_reputation,show_seller_number,show_goods_echarts,show_buy_dynamic,goods_search_show_type,show_3_seller,show_quan_exchange_score,show_score_exchange_growth').then(res => {
      if (res.code == 0) {
        res.data.forEach(config => {
          wx.setStorageSync(config.key, config.value);
          wx.setStorageSync('mallName', "租好机");
        })
        if (this.configLoadOK) {
          this.configLoadOK()
        }
      }
    })
    // ---------------检测navbar高度
    let menuButtonObject = wx.getMenuButtonBoundingClientRect();
    // console.log("小程序胶囊信息",menuButtonObject)
    wx.getSystemInfo({
      success: res => {
        let statusBarHeight = res.statusBarHeight,
          navTop = menuButtonObject.top,//胶囊按钮与顶部的距离
          navHeight = statusBarHeight + menuButtonObject.height + (menuButtonObject.top - statusBarHeight)*2;//导航高度
        this.globalData.navHeight = navHeight;
        this.globalData.navTop = navTop;
        this.globalData.windowHeight = res.windowHeight;
        this.globalData.menuButtonObject = menuButtonObject;
        console.log("navHeight",navHeight);
      },
      fail(err) {
        console.log(err);
      }
    })
  },

  onShow (e) {
    
    // 自动登录
    // AUTH.checkHasLogined().then(isLogined => {
    //   if (!isLogined) {
    //     AUTH.login()
    //   } else {
    //     AUTH.bindSeller()
    //   }
    // })
    const userInfo = wx.getStorageSync('userInfo')
    const userBase = wx.getStorageSync('userBase')
    console.log(userInfo,userBase)
    if(!(userInfo&&userBase)){
      AUTH.login()
      userInfo = wx.getStorageSync('userInfo')
      userBase = wx.getStorageSync('userBase')
      this.setData({
        userInfo : userInfo,
        userBase: userBase
      })
    }

  },
  globalData: {
    isConnected: true,
    sdkAppID: CONFIG.sdkAppID
  }
})
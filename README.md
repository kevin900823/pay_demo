# 客户集成工程示例 Eclipse Demo 工程

## 简介

tysyt SDK 是为移动端应用量身打造的下一代支付系统，通过一个 SDK 便可以同时接入多种主流移动支付渠道。使用 SDK 发起交易的基本流程参见 SDK 交易流程

## 版本要求
Android SDK 要求 Android 2.3 及以上版本

## 注意事项
该SDK可能会与其他第三方jar包冲突，当同时使用这些jar包的时候用户需要根据情况判断保留哪一方的jar包。 libs 目录下的是 jar 文件。

## 版本1.0.3
待更新

## 配置初始化
依赖包<br>
* 微信支付依赖包： libammsdk.jar<br>
* 支付宝支付依赖包： alipaySdk-20151112.jar <br>
* 银联支付依赖包：UPPayAssistEx.jar 依赖安卓包：unionpay.apk <br>
* 收银台 所需依赖包： checkout_cash.jar、gson-2.2.4.jar <br>
以上 jar 包位置在下载目录的 lib/libs 请确保gson 版本够高<br>
权限声明<br>
~~~ xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
~~~
注册Activity<br>
对于支付宝，需要添加<br>
~~~xml
<activity
    android:name="com.alipay.sdk.app.H5PayActivity"
    android:configChanges="orientation|keyboardHidden|navigation"
    android:exported="false"
    android:screenOrientation="behind"
android:windowSoftInputMode="adjustResize|stateHidden" />
~~~
对于微信支付，需要添加<br>
~~~xml
<activity
      android:name="cn.wd.checkout.WDWechatPaymentActivity"
      android:launchMode="singleTop"
      android:theme="@android:style/Theme.Translucent.NoTitleBar" />
<activity-alias
      android:name=".wxapi.WXPayEntryActivity"
      android:exported="true"
      android:targetActivity="cn.wd.checkout.WDWechatPaymentActivity" />
~~~
对于银联支付，需要添加<br>
~~~xml
<activity
            android:name="cn.wd.checkout.WDUnionPaymentActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" />
~~~ 
* 注意事项
 在项目根目录创建 assets 放入 银联在线支付服务安卓包 unionpay.apk <br>
 （可见demo/paydemo/assets ）另可在银联官网下载<br>

##开始接入<br>
~~~java 
  // 在主activity的onCreate函数中初始化账户中的AppID和AppSecret 、 第三个参数设置日志是否打印
  CheckOut.setAppIdAndSecret("wd2015tst001", "6XtC7H8NuykaRv423hrf1gGS09FEZQoB", true);
~~~

##发起支付

支付调用方法有两种 任选一种即可:<br>
####万达统一收银台支付方法(一)<br>
~~~java
//cn.wd.checkout.WDPay.reqPayAsync(WDChannelTypes channelType,String submerno String billTitle, String goods_desc, Long billTotalFee, String billNum, String order_desc, Map<String, String> optional, WDCallBack callback);

  WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.wepay, 
								submerno,
                        		goodsTitle,               //订单标题
                    			goodsDesc,
                                i,                           //订单金额(分)
                                orderTitle,  //订单流水号
                                orderDesc,
                                null,            //扩展参数(可以null)
                                bcCallback);
~~~
####参数说明:<br>
* channelType 支付类型 对于支付手机APP端目前只支持wepay(微信支付手机原生APP支付) alipay（支付宝手机原生APP支付） 
* submerno	子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
* billTitle 商品名称, 32个字节内, 汉字以2个字节计
* goods_desc 商品描述
* billTotalFee 支付金额，以分为单位，必须是正整数
* billNum 商户自定义订单号
* order_desc 订单描述
* optional 为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
* callback 支付完成后的回调函数

####万达统一收银台支付方法(二)<br>
~~~java
//cn.wd.checkout.WDPay.reqPayAsync(WDChannelTypes channelType,String submerno, String billTitle, String goods_desc, Long billTotalFee, String billNum, String order_desc, Map<String, String> optional, Handler mHandler)

	WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.wepay, 
								submerno,
                    			goodsTitle,               //订单标题
                    			goodsDesc,
                    			i,                           //订单金额(分)
                    			orderTitle,  //订单流水号
                    			orderDesc,
                    			null,            //扩展参数(可以null)
                    			handler);
~~~
####参数说明: <br>
* channelType 支付类型 对于支付手机APP端目前只支持WX_APP, ALI_APP, UN_APP 
* submerno	子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
* billTitle 商品名称, 32个字节内, 汉字以2个字节计 
* goods_desc 商品描述 
* billTotalFee 支付金额，以分为单位，必须是正整数 
* billNum 商户自定义订单号 
* order_desc 订单描述 
* optional 为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求 
* mHandler 支付完成后的回调函数 UI层通知


    * 注：参照demo中MainActivity<br>
    
##获取支付状态

####返回对象说明
~~~java
/**
 * 支付结果返回类
 */
public class WDPayResult implements WDResult {
	// result包含支付成功、取消支付、支付失败
	private String result;
	// 针对支付失败的情况，提供失败原因
	private String errMsg;
	// 提供详细的支付信息，比如原生的支付宝返回信息
	private String detailInfo;
}
~~~
#####对应第一种支付的回调函数接收状态
~~~java
 //支付结果返回入口
    WDCallBack bcCallback = new WDCallBack() {
        @Override
        public void done(final WDResult bcResult) {
            final WDPayResult bcPayResult = (WDPayResult)bcResult;
            PayDemoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	//此处关闭loading界面
                	loadingDialog.dismiss();
                    String result = bcPayResult.getResult();
                    Log.i("demo", "done   result="+result);
                    if (result.equals(WDPayResult.RESULT_SUCCESS))
                        Toast.makeText(PayDemoActivity.this, "用户支付成功", Toast.LENGTH_LONG).show();
                    else if (result.equals(WDPayResult.RESULT_CANCEL))
                        Toast.makeText(PayDemoActivity.this, "用户取消支付", Toast.LENGTH_LONG).show();
                    else if(result.equals(WDPayResult.RESULT_FAIL)) {
                        Toast.makeText(PayDemoActivity.this, "支付失败, 原因: " + bcPayResult.getErrMsg()
                                + ", " + bcPayResult.getDetailInfo(), Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_UNKNOWN_WAY)){
                    	Toast.makeText(PayDemoActivity.this, "未知支付渠道", Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_WEIXIN_VERSION_ERROR)){
                    	Toast.makeText(PayDemoActivity.this, "针对微信 支付版本错误（版本不支持）", Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_EXCEPTION)){
                    	Toast.makeText(PayDemoActivity.this, "支付过程中的Exception", Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_ERR_FROM_CHANNEL)){
                    	Toast.makeText(PayDemoActivity.this, "从第三方app支付渠道返回的错误信息，原因: " + bcPayResult.getErrMsg(), Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_INVALID_PARAMS)){
                    	Toast.makeText(PayDemoActivity.this, "参数不合法造成的支付失败", Toast.LENGTH_LONG).show();
                    }else if(result.equals(WDPayResult.RESULT_PAYING_UNCONFIRMED)){
                    	Toast.makeText(PayDemoActivity.this, "表示支付中，未获取确认信息", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(PayDemoActivity.this, "invalid return", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
~~~
#####对应第二种支付的Hander接收反馈状态
~~~java
 Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		//此处关闭loading界面
            loadingDialog.dismiss();
            String info="";
    		switch (msg.what) {
			case WDPayResult.RESULT_SUCCESS_HANDLER:
				info= (String) msg.obj;
				break;
			case WDPayResult.RESULT_CANCEL_HANDLER:
				 info = (String) msg.obj;
				break;
			case WDPayResult.RESULT_FAIL_HANDLER:
				 info = (String) msg.obj;
					break;
			default:
				break;
			}
    		Log.i("demo", "msg.what="+msg.what +" info="+info);
    		Toast.makeText(PayDemoActivity.this, info, Toast.LENGTH_LONG).show();
    	};
    };
~~~
##反馈状态对应含义
~~~java
(result)
|--成功  WDPayResult.RESULT_SUCCESS_HANDLER = 1 ; WDPayResult.RESULT_SUCCESS = "SUCCESS"; 
|
|--用户取消 	WDPayResult.RESULT_CANCEL_HANDLER = -1; WDPayResult.RESULT_CANCEL = "CANCEL";
|
|											|-- 调用sdk失败	(errMsg)	|WDPayResult.FAIL_UNKNOWN_WAY = "UNKNOWN_WAY" 未知的支付渠道
|											|							|WDPayResult.FAIL_EXCEPTION = "FAIL_EXCEPTION";  参数初始错误 或 调起微信支付sdk错误
|											|							|WDPayResult.FAIL_INVALID_PARAMS = "FAIL_INVALID_PARAMS" ; 支付参数不合法 、 支付渠道参数不合法 
|--失败 WDPayResult.RESULT_FAIL= "FAIL"---	|							|WDPayResult.FAIL_NETWORK_ISSUE = "FAIL_NETWORK_ISSUE"; 网络问题造成的支付失败
|		WDPayResult.RESULT_FAIL_HANDLER = 0;|
|											|
											|
											|--支付渠道返回失败	(errMsg)|WDPayResult.RESULT_PAYING_UNCONFIRMED = "RESULT_PAYING_UNCONFIRMED"; 订单正在处理中，无法获取成功确认信息
																		|WDPayResult.FAIL_ERR_FROM_CHANNEL = "FAIL_ERR_FROM_CHANNEL";从第三方app支付渠道返回的错误信息（支付渠道返回失败）

~~~
##关于渠道<br>
Android SDK 目前适用于 alipay（支付宝）、weixin（微信）、uppay（银联）这三个渠道<br>
######对应本SDK:<br>
* weixin -- WDReqParams.WDChannelTypes.wepay<br>
* alipay -- WDReqParams.WDChannelTypes.alipay<br>
* uppay -- WDReqParams.WDChannelTypes.uppay











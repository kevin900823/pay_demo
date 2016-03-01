package cn.cashier.appnative;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import cn.cashier.PayMethodListItem;
import cn.cashier.R;
import cn.wd.checkout.api.CheckOut;
import cn.wd.checkout.api.WDCallBack;
import cn.wd.checkout.api.WDPay;
import cn.wd.checkout.api.WDPayResult;
import cn.wd.checkout.api.WDReqParams;
import cn.wd.checkout.api.WDResult;

public class PayDemoActivity extends Activity {


	//��д��ǰapp ��Ӧ���̻���
	private final String submerno ="wdtstsub00001";
	
    private ListView payMethod;
	private ProgressDialog loadingDialog;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHss", Locale.CHINA);
    SimpleDateFormat simpleDateFormattemp = new SimpleDateFormat("SSS", Locale.CHINA);
/**
 * 
(result)
|--�ɹ�  WDPayResult.RESULT_SUCCESS_HANDLER = 1 ; WDPayResult.RESULT_SUCCESS = "SUCCESS"; 
|
|--�û�ȡ�� 	WDPayResult.RESULT_CANCEL_HANDLER = -1; WDPayResult.RESULT_CANCEL = "CANCEL";
|
|											|-- ����sdkʧ��	(errMsg)	|WDPayResult.FAIL_UNKNOWN_WAY = "UNKNOWN_WAY" δ֪��֧������
|											|							|WDPayResult.FAIL_EXCEPTION = "FAIL_EXCEPTION";  ������ʼ���� �� ����΢��֧��sdk����
|											|							|WDPayResult.FAIL_INVALID_PARAMS = "FAIL_INVALID_PARAMS" ; ֧���������Ϸ� �� ֧�������������Ϸ� 
|--ʧ�� WDPayResult.RESULT_FAIL= "FAIL"---	|							|WDPayResult.FAIL_NETWORK_ISSUE = "FAIL_NETWORK_ISSUE"; ����������ɵ�֧��ʧ��
|		WDPayResult.RESULT_FAIL_HANDLER = 0;|
|											|
											|
											|--֧����������ʧ��	(errMsg)|WDPayResult.RESULT_PAYING_UNCONFIRMED = "RESULT_PAYING_UNCONFIRMED"; �������ڴ����У��޷���ȡ�ɹ�ȷ����Ϣ
																		|WDPayResult.FAIL_ERR_FROM_CHANNEL = "FAIL_ERR_FROM_CHANNEL";�ӵ�����app֧���������صĴ�����Ϣ��֧����������ʧ�ܣ�
									


 */
    //֧������������
    WDCallBack bcCallback = new WDCallBack() {
        @Override
        public void done(final WDResult bcResult) {
            final WDPayResult bcPayResult = (WDPayResult)bcResult;
            PayDemoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	CloesLoading();
                    String result = bcPayResult.getResult();
                    Log.i("demo", "done   result="+result);
                    if (result.equals(WDPayResult.RESULT_SUCCESS))
                        Toast.makeText(PayDemoActivity.this, "�û�֧���ɹ�", Toast.LENGTH_LONG).show();
                    else if (result.equals(WDPayResult.RESULT_CANCEL))
                        Toast.makeText(PayDemoActivity.this, "�û�ȡ��֧��", Toast.LENGTH_LONG).show();
                    else if(result.equals(WDPayResult.RESULT_FAIL)) {
                    	String info = "֧��ʧ��, ԭ��: " + bcPayResult.getErrMsg()
                                + ", " + bcPayResult.getDetailInfo();
                        Toast.makeText(PayDemoActivity.this, info, Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_UNKNOWN_WAY)){
                    	Toast.makeText(PayDemoActivity.this, "δ֪֧������", Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_WEIXIN_VERSION_ERROR)){
                    	Toast.makeText(PayDemoActivity.this, "���΢�� ֧���汾���󣨰汾��֧�֣�", Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_EXCEPTION)){
                    	Toast.makeText(PayDemoActivity.this, "֧�������е�Exception", Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_ERR_FROM_CHANNEL)){
                    	Toast.makeText(PayDemoActivity.this, "�ӵ�����app֧���������صĴ�����Ϣ��ԭ��: " + bcPayResult.getErrMsg(), Toast.LENGTH_LONG).show();
                    } else if(result.equals(WDPayResult.FAIL_INVALID_PARAMS)){
                    	Toast.makeText(PayDemoActivity.this, "�������Ϸ���ɵ�֧��ʧ��", Toast.LENGTH_LONG).show();
                    }else if(result.equals(WDPayResult.RESULT_PAYING_UNCONFIRMED)){
                    	Toast.makeText(PayDemoActivity.this, "��ʾ֧���У�δ��ȡȷ����Ϣ", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(PayDemoActivity.this, "invalid return", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
    
    Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		CloesLoading();
            
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
	private EditText mGoodsMoney;
	private EditText mGoodsTitle;
	private EditText mGoodsTitleDesc;
	private EditText mOrderTitle;
	private EditText mOrderTitleDesc;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_pay);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
     
        
        
        payMethod = (ListView) this.findViewById(R.id.payMethod);
        Integer[] payIcons = new Integer[]{R.drawable.wechat,R.drawable.wechat, R.drawable.alipay, R.drawable.alipay,R.drawable.unionpay,R.drawable.unionpay,R.drawable.icon_wonderspay,R.drawable.icon_wonderspay};
        final String[] payNames = new String[]{"΢��֧��","΢��֧�� UI����", "֧����֧��", "֧����֧�� UI����", "����֧��", "����֧�� UI����","��֧��","��֧����װ����"};
        String[] payDescs = new String[]{"ʹ��΢��֧�����������CNY�Ʒ�","ʹ��΢��֧�����������CNY�Ʒ�", "ʹ��֧����֧�����������CNY�Ʒ�", "ʹ��֧����֧�����������CNY�Ʒ�", "ʹ������֧�����������CNY�Ʒ�", "ʹ������֧�����������CNY�Ʒ�", "ʹ����֧�����������CNY�Ʒ�", "��ת����������ذ�װ������֧��APP"};
        PayMethodListItem adapter = new PayMethodListItem(this, payIcons, payNames, payDescs);
        payMethod.setAdapter(adapter);
        
        
        mGoodsMoney = (EditText) findViewById(R.id.edt_main_money);
        mGoodsTitle = (EditText) findViewById(R.id.edt_main_goods_title);
        mGoodsTitleDesc = (EditText) findViewById(R.id.edt_main_goods_title_desc);
        mOrderTitle = (EditText) findViewById(R.id.edt_main_order_title);
        mOrderTitleDesc = (EditText) findViewById(R.id.edt_main_order_title_desc);
        mOrderTitle.setText(getBillNum());
        
        
        // �������֧��̫��, ���������￪������, ��progressdialogΪ��
        loadingDialog = new ProgressDialog(PayDemoActivity.this);
        loadingDialog.setMessage("����������֧�������Ժ�...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        payMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	
            	// ����activity��onCreate�����г�ʼ���˻��е�AppID��AppSecret 
            	// appId     appid ͳһ����̨ǩԼ��ȡ idΨһ
                // appSecret App Secret ͳһ����̨ǩԼ��ȡ ��Ψһ ÿ�춼���������� ����Ҫÿ������
                CheckOut.setAppIdAndSecret("wd2015tst001", "6XtC7H8NuykaRv423hrf1gGS09FEZQoB");
                CheckOut.setIsPrint(true);
                /**
                 * ���÷������绷��  CT Ϊ�������Ի��� �����ô˷���Ϊ��������
                 */
                CheckOut.setNetworkWay("CST");
                CheckOut.setLianNetworkWay("TS");
                
            	
            	String money = mGoodsMoney.getText().toString().trim();
            	String goodsTitle = mGoodsTitle.getText().toString().trim();
            	String goodsDesc = mGoodsTitleDesc.getText().toString().trim();
            	String orderTitle = mOrderTitle.getText().toString().trim();
            	mOrderTitle.setText(getBillNum());
            	String orderDesc = mOrderTitleDesc.getText().toString().trim();
            	Long i = 0l ;
            	if(isNumeric(money)){
            		i = Long.parseLong(money);
            	}else{
            		Toast.makeText(PayDemoActivity.this, "��������ȷ�Ľ��׽���λ���֣�", Toast.LENGTH_LONG).show();
            		return;
            	}
                switch (position) {
                    case 0: //΢��
                        loadingDialog.show();
                        //����΢��֧��, �ֻ��ڴ�̫С����OutOfResourcesException��ɵĿ���, �����޷����֧��
                        //�����΢��������ڵ�����
                        WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.wepay, submerno,
                        		goodsTitle,               //��������
                    			goodsDesc,
                                i,                           //�������(��)
                                orderTitle,  //������ˮ��
                                orderDesc,
                                null,            //��չ����(����null)
                                bcCallback);
                        
                        break;
                    case 1: //΢��
                    	loadingDialog.show();
                    	//����΢��֧��, �ֻ��ڴ�̫С����OutOfResourcesException��ɵĿ���, �����޷����֧��
                    	//�����΢��������ڵ�����
                    	
                    	WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.wepay, submerno,
                    			goodsTitle,               //��������
                    			goodsDesc,
                    			i,                           //�������(��)
                    			orderTitle,  //������ˮ��
                    			orderDesc,
                    			null,            //��չ����(����null)
                    			handler);
                    	break;

                    case 2: //֧����֧��
                        loadingDialog.show();
                        
                        WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.alipay, submerno,
                        		goodsTitle,               //��������
                    			goodsDesc,
                                i,                           //�������(��)
                                orderTitle,  //������ˮ��
                                orderDesc,
                                null,            //��չ����(����null)
                                bcCallback);
                        
                        
                        break;
                    case 3: //֧����֧��
                    	loadingDialog.show();
                    	
                    	WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.alipay, submerno,
                    			goodsTitle,               //��������
                    			goodsDesc,
                    			i,                           //�������(��)
                    			orderTitle,  //������ˮ��
                    			orderDesc,
                    			null,            //��չ����(����null)
                    			handler);
                    	
                    	break;
                    case 4: //����֧��
                    	loadingDialog.show();
                    	
                    	WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.uppay, 
                    			submerno,
                    			goodsTitle,               //��������
                    			goodsDesc,
                    			i,                           //�������(��)
                    			orderTitle,  //������ˮ��
                    			orderDesc,
                    			null,            //��չ����(����null)
                    			bcCallback);
                    	
                    	
                    	break;
                    case 5: //����֧��
                    	loadingDialog.show();
                    	
                    	WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.uppay, 
                    			submerno,
                    			goodsTitle,               //��������
                    			goodsDesc,
                    			i,                           //�������(��)
                    			orderTitle,  //������ˮ��
                    			orderDesc,
                    			null,            //��չ����(����null)
                    			handler);
                    	
                    	break;
                    case 6: //��֧��
                    	loadingDialog.show();
                    	
                    	WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.wdepay, 
                    			submerno,
                    			goodsTitle,               //��������
                    			goodsDesc,
                    			i,                           //�������(��)
                    			orderTitle,  //������ˮ��
                    			orderDesc,
                    			null,            //��չ����(����null)
                    			bcCallback);
                    	
                    	break;
                    case 7: //��֧��
                    	loadingDialog.show();
                    	
//                    	WDPay.getInstance(PayDemoActivity.this).reqPayAsync(WDReqParams.WDChannelTypes.wdepay, 
//                    			submerno,
//                    			goodsTitle,               //��������
//                    			goodsDesc,
//                    			i,                           //�������(��)
//                    			orderTitle,  //������ˮ��
//                    			orderDesc,
//                    			null,            //��չ����(����null)
//                    			handler);
//                    	http://www.wdepay.cn/MobileFront/user/download/WandaApk.do
                    	
                    	Intent intent= new Intent(); 
        			    intent.setAction("android.intent.action.VIEW");   
        			    Uri content_url = Uri.parse("http://www.wdepay.cn/MobileFront/user/download/WandaApk.do");   
        			    intent.setData(content_url);  
        			    startActivity(intent);
                    	
                    	break;
                    default:
                }
            }
        });
    }
	
	   public void onResume() {
		   super.onResume();
	    	CloesLoading();
	    }

	private void CloesLoading() {
		if(loadingDialog!=null && loadingDialog.isShowing()){
			//�˴��ر�loading����
		    loadingDialog.dismiss();
		}
	};
	 String getBillNum() {
	        return "974"+simpleDateFormat.format(new Date())+simpleDateFormattemp.format(new Date())+"and";
	    }
	 
	 public final static boolean isNumeric(String s) {  
	        if (s != null && !"".equals(s.trim()))  
	            return s.matches("^[0-9]+(.[0-9]{1,2})?$");  
	        else  
	            return false;  
	    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        
        return true;
    }
    
}

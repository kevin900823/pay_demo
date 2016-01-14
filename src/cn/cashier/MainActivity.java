package cn.cashier;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import cn.cashier.R;
import cn.cashier.appnative.PayDemoActivity;


public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private ListView mLv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		initView();
		
		
	}
	
	/**
     * 将实例转化成符合后台请求的键值对
     * 用于以json方式post请求
     */
    public Map<String, Object> transToBillReqMapParams(){
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("appid", "wd2015tst001");
        params.put("appsecret", "6dpz8k5820o4cj315h894gx0dj0vaxa7");

        return params;
    }
	
	private void initView() {
		Integer[] payIcons = new Integer[]{R.drawable.icon_app_pay,R.drawable.icon_hybrid_pay, R.drawable.icon_scan_pay};
		final String[] payNames = new String[]{"APP原生支付","手机网页支付", "扫码支付"};
		String[] payDescs = new String[]{"","", ""};
		
		mLv = (ListView) findViewById(R.id.main_lv);
		PayMethodListItem adapter = new PayMethodListItem(this, payIcons, payNames, payDescs);
		mLv.setAdapter(adapter);
		
		mLv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		switch (arg2) {
		case 0:
			
			startActivity(new Intent(MainActivity.this, PayDemoActivity.class));
			
			break;
		case 1:
//			startActivity(new Intent(MainActivity.this, PayWebActivity.class));
			 Intent intent= new Intent(); 
			    intent.setAction("android.intent.action.VIEW");   
			    Uri content_url = Uri.parse(DEFAULT_URL);   
			    intent.setData(content_url);  
			    startActivity(intent);
			
			break;
		case 2:
			Toast.makeText(MainActivity.this, "敬请期待！", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
	}
//	private static final String DEFAULT_URL = "http://10.1.64.205:18001/WebCashierDesk/text.jsp"; 

//	private static final String DEFAULT_URL = "http://cashtest.wdepay.cn:20080/WebCashierDesk/text.jsp" ;
  private static final String DEFAULT_URL = "http://cash.wdepay.cn:20080/WebCashierDesk/text.jsp" ;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}

package cn.paydemo;

import cn.paydemo.appnative.PayDemoActivity;
import cn.paydemo.web.PayWebActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
			startActivity(new Intent(MainActivity.this, PayWebActivity.class));
			break;
		case 2:
			Toast.makeText(MainActivity.this, "敬请期待！", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}

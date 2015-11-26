package cn.paydemo.web;

import cn.paydemo.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * 统一收银台web支付
 * @author Administrator
 *
 */
public class PayWebActivity extends Activity {
	private static final String DEFAULT_URL = "http://101.230.192.79/WebCashierDesk/text.jsp"; 
	private WebView pw_WV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_pay);
		
		pw_WV = (WebView) findViewById(R.id.wp_wb);
        init(pw_WV);  
    }  
      
    private void init(WebView v) {  
        WebSettings webSettings = v.getSettings();  
        webSettings.setJavaScriptEnabled(true);  
        webSettings.setSupportZoom(true);  
        //webSettings.setUseWideViewPort(true);  
        v.setWebViewClient(mWebViewClientBase);  
        v.setWebChromeClient(mWebChromeClientBase);  
        v.loadUrl(DEFAULT_URL);  
//        v.onResume();  
    }  
      
    private WebViewClientBase mWebViewClientBase = new WebViewClientBase();  
      
    private class WebViewClientBase extends WebViewClient {  
  
        @Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            // TODO Auto-generated method stub  
            return super.shouldOverrideUrlLoading(view, url);  
        }  
  
        @Override  
        public void onPageStarted(WebView view, String url, Bitmap favicon) {  
            // TODO Auto-generated method stub  
            super.onPageStarted(view, url, favicon);  
        }  
  
        @Override  
        public void onPageFinished(WebView view, String url) {  
            // TODO Auto-generated method stub  
            super.onPageFinished(view, url);  
        }  
  
        @Override  
        public void onReceivedError(WebView view, int errorCode,  
                String description, String failingUrl) {  
            // TODO Auto-generated method stub  
            super.onReceivedError(view, errorCode, description, failingUrl);  
        }  
  
        @Override  
        public void doUpdateVisitedHistory(WebView view, String url,  
                boolean isReload) {  
            // TODO Auto-generated method stub  
            super.doUpdateVisitedHistory(view, url, isReload);  
        }  
    }  
      
    private WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase();  
    final Activity MyActivity = this;
    private class WebChromeClientBase extends WebChromeClient {  
  
        @Override  
        public void onProgressChanged(WebView view, int newProgress) {  
        	MyActivity.setProgress(newProgress * 1000);  
        }  
  
        @Override  
        public void onReceivedTitle(WebView view, String title) {  
            // TODO Auto-generated method stub  
            super.onReceivedTitle(view, title);  
        }  
  
        @Override  
        public void onReceivedTouchIconUrl(WebView view, String url,  
                boolean precomposed) {  
            // TODO Auto-generated method stub  
            super.onReceivedTouchIconUrl(view, url, precomposed);  
        }  
  
        @Override  
        public boolean onCreateWindow(WebView view, boolean isDialog,  
                boolean isUserGesture, Message resultMsg) {  
            // TODO Auto-generated method stub  
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);  
        }  
          
    }
	

}

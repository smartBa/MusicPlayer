package c.musicplayer.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import c.musicplayer.R;

public class MainSearch extends AppCompatActivity {
    private ImageView close;
    private WebView webView;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        init();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
            }
        });
    }
    public void init(){
        close=(ImageView)findViewById(R.id.close);
        back=(ImageView)findViewById(R.id.backSearch);
         webView= (WebView) findViewById(R.id.webView);
        webView.loadUrl("http://m.kugou.com");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

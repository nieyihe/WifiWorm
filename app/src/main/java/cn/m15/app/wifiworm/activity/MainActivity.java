package cn.m15.app.wifiworm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cn.m15.app.wifiworm.R;
import cn.m15.app.wifiworm.presenter.ShowPsdPresenter;
import cn.m15.app.wifiworm.ui.view.IWifiPsdView;


public class MainActivity extends Activity implements IWifiPsdView {

    private TextView mShowWifiPassword;

    private ShowPsdPresenter showPsdPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        showPsdPresenter = new ShowPsdPresenter(this);
        showPsdPresenter.showPassword();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化视图
     * */
    private void initView(){
        mShowWifiPassword = (TextView) this.findViewById(R.id.activity_main_wifi_password);
    }


    @Override
    public void showPassword(String psd) {
        mShowWifiPassword.setText(psd);
    }
}

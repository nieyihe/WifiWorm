package cn.m15.app.wifiworm.presenter;

import android.app.Activity;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.m15.app.wifiworm.entity.WifiInfo;
import cn.m15.app.wifiworm.ui.view.IWifiPsdView;

/**
 * Created by ${shitt} on 2016/6/28.
 */
public class ShowPsdPresenter {

    private IWifiPsdView wifiPsdView;

    public ShowPsdPresenter(IWifiPsdView wifiPsdView) {
        this.wifiPsdView = wifiPsdView;
    }

    /**
     * չʾ����
     */
    public void showPassword() {
        String psd = "û�л�ȡ������";
        psd = getWifiPsd();
        this.wifiPsdView.showPassword(psd);
    }

    /**
     * ��ȡ��ǰ����Wifi��Wifi����
     * */
    private String getWifiPsd() {
        WifiManager wifiManager = (WifiManager) ((Activity) wifiPsdView).getSystemService(Activity.WIFI_SERVICE);
        android.net.wifi.WifiInfo localwifiInfo = wifiManager.getConnectionInfo();
        WifiInfo wifiInfo = new WifiInfo();
        Process process = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        StringBuffer wifiConf = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            dataOutputStream
                    .writeBytes("cat /data/misc/wifi/*.conf\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                wifiConf.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Pattern network = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL);
        Matcher networkMatcher = network.matcher(wifiConf.toString());
        while (networkMatcher.find()) {
            String networkBlock = networkMatcher.group();
            Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
            Matcher ssidMatcher = ssid.matcher(networkBlock);

            if (ssidMatcher.find()) {
                wifiInfo.ssid = ssidMatcher.group(1);
                if(localwifiInfo.getSSID().equals(wifiInfo.ssid)) {
                    Pattern psk = Pattern.compile("psk=\"([^\"]+)\"");
                    Matcher pskMatcher = psk.matcher(networkBlock);
                    if (pskMatcher.find()) {
                        wifiInfo.password = pskMatcher.group(1);
                    } else {
                        wifiInfo.password = "������";
                    }
                }
            }

        }

        return wifiInfo.password;
    }

    private List<WifiInfo> getWifiInfos() throws Exception {
        List<WifiInfo> wifiInfos = new ArrayList<WifiInfo>();

        Process process = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        StringBuffer wifiConf = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            dataOutputStream
                    .writeBytes("cat /data/misc/wifi/*.conf\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                wifiConf.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            process.waitFor();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
                throw e;
            }
        }

        Pattern network = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL);
        Matcher networkMatcher = network.matcher(wifiConf.toString());
        while (networkMatcher.find()) {
            String networkBlock = networkMatcher.group();
            Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
            Matcher ssidMatcher = ssid.matcher(networkBlock);

            if (ssidMatcher.find()) {
                WifiInfo wifiInfo = new WifiInfo();
                wifiInfo.ssid = ssidMatcher.group(1);
                Pattern psk = Pattern.compile("psk=\"([^\"]+)\"");
                Matcher pskMatcher = psk.matcher(networkBlock);
                if (pskMatcher.find()) {
                    wifiInfo.password = pskMatcher.group(1);
                } else {
                    wifiInfo.password = "������";
                }
                wifiInfos.add(wifiInfo);
            }

        }

        return wifiInfos;
    }
}
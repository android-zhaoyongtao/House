//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shen.baselibrary.utiles;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Locale;

public class DeviceInfoUtils {
    private static final boolean DEBUG = false;
    private static final String TAG = "DeviceInfoUtils";
    public static final String DEFAULT_IMEI = "";
    public static final String DEVICE_ID_FILENAME = "DEVICE_ID";
    public static final String DEVICE_ID_FILENAME_NEW = "DEV";
    public static final String DEVICE_ID_FILENAME_NEW_V2 = "DEVV2";
    public static final String ANDROID_ID_FILENAME = "ANDROID_ID";

    public DeviceInfoUtils() {
    }


    public static String getImsi(Context context) {
        if (context != null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return "";
                    }
                    return telephonyManager.getSubscriberId();
                }
            } catch (Exception var2) {
                ;
            }
        }

        return "";
    }

    public static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);//手机系统信息的一个服务
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            return tm.getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    public static synchronized String getDeviceSerialNumber() {
        String serial = "";

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
            if (serial == null) {
                serial = "";
            }
        } catch (Exception var3) {
            ;
        }

        return serial;
    }

    public static String getCPUSerial() {
        String line = "";
        String cpuAddress = null;

        try {
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (int i = 1; i < 100; ++i) {
                line = input.readLine();
                if (line == null) {
                    break;
                }

                line = line.toLowerCase();
                int p1 = line.indexOf("serial");
                int p2 = line.indexOf(":");
                if (p1 > -1 && p2 > 0) {
                    cpuAddress = line.substring(p2 + 1);
                    cpuAddress = cpuAddress.trim();
                    break;
                }
            }
        } catch (IOException var8) {
            ;
        }

        return cpuAddress;
    }

    public static String getAndroidId(Context context) {
        String ai = Secure.getString(context.getContentResolver(), "android_id");
        return ai == null ? "" : ai;
    }

    public static String getWiFiMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;

        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }

            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            } else {
                byte[] addr = networkInterface.getHardwareAddress();
                byte[] var4 = addr;
                int var5 = addr.length;

                for (int var6 = 0; var6 < var5; ++var6) {
                    byte b = var4[var6];
                    buf.append(String.format("%02X:", b));
                }

                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }

                macAddress = buf.toString();
                return macAddress;
            }
        } catch (SocketException var8) {
            var8.printStackTrace();
            return "02:00:00:00:00:02";
        }
    }

    public static String getEthernetMacAddress() {
        String mac = "";

        try {
            Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();

            while (localEnumeration.hasMoreElements()) {
                NetworkInterface localNetworkInterface = (NetworkInterface) localEnumeration.nextElement();
                String interfaceName = localNetworkInterface.getDisplayName();
                if (interfaceName != null && interfaceName.equals("eth0")) {
                    mac = bytesToMacAddress(localNetworkInterface.getHardwareAddress());
                    if (mac != null && mac.startsWith("0:")) {
                        mac = "0" + mac;
                    }
                    break;
                }
            }
        } catch (SocketException var4) {
            var4.printStackTrace();
        }

        return mac;
    }

    private static String bytesToMacAddress(byte[] mac) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < mac.length; ++i) {
            byte b = mac[i];
            if (b >= 0 && b <= 16) {
                sb.append("0" + Integer.toHexString(b));
            } else if (b > 16) {
                sb.append(Integer.toHexString(b));
            } else {
                int value = 256 + b;
                sb.append(Integer.toHexString(value));
            }

            if (i != mac.length - 1) {
                sb.append(":");
            }
        }

        return sb.toString();
    }

    public static String getHostIP() {
        String hostIp = null;

        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;

            while (true) {
                while (nis.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) nis.nextElement();
                    Enumeration ias = ni.getInetAddresses();

                    while (ias.hasMoreElements()) {
                        ia = (InetAddress) ias.nextElement();
                        if (!(ia instanceof Inet6Address)) {
                            String ip = ia.getHostAddress();
                            if (!"127.0.0.1".equals(ip)) {
                                hostIp = ia.getHostAddress();
                                break;
                            }
                        }
                    }
                }

                return hostIp;
            }
        } catch (SocketException var6) {
            var6.printStackTrace();
            return hostIp;
        }
    }

    public static String getCurLanguage() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        return language.trim() + "_" + country.trim();
    }

    public static String getDeviceDpiInfo(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        DecimalFormat df = new DecimalFormat("##0.00");
        return String.valueOf(df.format((double) displayMetrics.density));
    }

    public static String getScreenSize(Context ctx) {
        Point size = getSizeFromRealDisplayMetrics(ctx);
        return size != null ? size.x + "x" + size.y : getSizeFromDisplayMetrics(ctx);
    }

    public static Point getSizeFromRealDisplayMetrics(Context ctx) {
        try {
            Resources r = ctx.getResources();
            Class<?> rClass = r.getClass();
            Method method = rClass.getMethod("getRealDisplayMetrics");
            DisplayMetrics dm = (DisplayMetrics) method.invoke(r);
            return new Point(dm.widthPixels, dm.heightPixels);
        } catch (Exception var5) {
            return null;
        }
    }

    public static String getSizeFromDisplayMetrics(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        return dm == null ? "0x0" : dm.widthPixels + "x" + dm.heightPixels;
    }
}

package com.gangbeng.basemodule.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LogUtil {
    public static final int NONLOG = 0;
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    private static int level = 6;
    private static String TAG = "LogUtil";
    private static String filemane = "loginfo";
    private static Boolean PRINT = Boolean.valueOf(false);
    private static final int MAX_LENGTH = 4000;

    public LogUtil() {
    }

    public static void v(String msg) {
        v((String)null, msg);
    }

    public static void v(String Tag, String msg) {
        if(level >= 1) {
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            String tag = getTagInfo(stackTraceElement);
            String[] result = divideLongString(msg + "     " + tag);
            String[] var5 = result;
            int var6 = result.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String s = var5[var7];
                if(Tag == null) {
                    Log.v(TAG, s);
                } else {
                    Log.v(TAG + "-" + Tag, s);
                }
            }

            if(PRINT.booleanValue()) {
                print(tag + "&" + msg);
            }
        }

    }

    public static void d(String msg) {
        d((String)null, (String)msg);
    }

    public static void d(String format, Object... objects) {
        d((String)null, (String)String.format(format, objects));
    }

    public static void d(String Tag, String msg) {
        if(level >= 2) {
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            String tag = getTagInfo(stackTraceElement);
            String[] result = divideLongString(msg + "     " + tag);
            String[] var5 = result;
            int var6 = result.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String s = var5[var7];
                if(Tag == null) {
                    Log.d(TAG, s);
                } else {
                    Log.d(TAG + "-" + Tag, s);
                }
            }

            if(PRINT.booleanValue()) {
                print(tag + "&" + msg);
            }
        }

    }

    public static void i(String msg) {
        i((String)null, msg);
    }

    public static void i(String Tag, String msg) {
        if(level >= 4) {
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            String tag = getTagInfo(stackTraceElement);
            String[] result = divideLongString(msg + "     " + tag);
            String[] var5 = result;
            int var6 = result.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String s = var5[var7];
                if(Tag == null) {
                    Log.i(TAG, s);
                } else {
                    Log.i(TAG + "-" + Tag, s);
                }
            }

            if(PRINT.booleanValue()) {
                print(tag + "&" + msg);
            }
        }

    }

    public static void w(String msg) {
        w((String)null, msg);
    }

    public static void w(String Tag, String msg) {
        if(level >= 5) {
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            String tag = getTagInfo(stackTraceElement);
            String[] result = divideLongString(msg + "     " + tag);
            String[] var5 = result;
            int var6 = result.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String s = var5[var7];
                if(Tag == null) {
                    Log.w(TAG, s);
                } else {
                    Log.w(TAG + "-" + Tag, s);
                }
            }

            if(PRINT.booleanValue()) {
                print(tag + "&" + msg);
            }
        }

    }

    public static void e(String msg) {
        e((String)null, msg);
    }

    public static void e(String Tag, String msg) {
        if(level >= 6) {
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            String tag = getTagInfo(stackTraceElement);
            String[] result = divideLongString(msg + "     " + tag);
            String[] var5 = result;
            int var6 = result.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String s = var5[var7];
                if(Tag == null) {
                    Log.e(TAG, s);
                } else {
                    Log.e(TAG + "-" + Tag, s);
                }
            }

            if(PRINT.booleanValue()) {
                print(tag + "&" + msg);
            }
        }

    }

    public static void t(Throwable tr) {
        t((String)null, "", tr);
    }

    public static void t(String msg, Throwable tr) {
        t((String)null, msg, tr);
    }

    public static void t(String Tag, String msg, Throwable tr) {
        if(level >= 6) {
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            String tag = getTagInfo(stackTraceElement);
            String[] result = divideLongString(msg + "     " + tag);
            String[] var6 = result;
            int var7 = result.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String s = var6[var8];
                if(Tag == null) {
                    Log.e(TAG, s, tr);
                } else {
                    Log.e(TAG + "-" + Tag, s, tr);
                }
            }

            if(PRINT.booleanValue()) {
                print(tag + "&" + msg, tr);
            }
        }

    }

    private static String[] divideLongString(String msg) {
        if(msg.length() < 4000) {
            return new String[]{msg};
        } else {
            int group = msg.length() / 4000;
            String[] result = new String[group + 1];

            for(int i = 0; i < group + 1; ++i) {
                result[i] = msg.substring(i * 4000, Math.min(msg.length(), i * 4000 + 4000));
            }

            return result;
        }
    }

    public static void json(String json) {
        d(jsonToLog(json));
    }

    public static void list(List<Object> list) {
        d(listToLog(list));
    }

    public static String jsonToLog(String json) {
        if(TextUtils.isEmpty(json)) {
            return "JSON{json is null}";
        } else {
            try {
                if(json.startsWith("{")) {
                    JSONObject e = new JSONObject(json);
                    json = e.toString(4);
                } else if(json.startsWith("[")) {
                    JSONArray e1 = new JSONArray(json);
                    json = e1.toString(4);
                }

                return json;
            } catch (JSONException var2) {
                return json;
            }
        }
    }

    public static void toast(Context context, String msg) {
        toast((String)null, (Context)context, (String)msg);
    }

    public static void toast(Context context, String format, Object... objects) {
        toast((String)null, (Context)context, (String)String.format(format, objects));
    }

    public static void toast(String Tag, final Context context, final String msg) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } else if(context instanceof Activity) {
            ((Activity)context).runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            e("在非主线程中输出toast");
        }

        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        String tag = getTagInfo(stackTraceElement);
        Log.i(TAG + "-" + Tag, msg + "     " + tag);
        if(PRINT.booleanValue()) {
            print("Toast:" + tag + "&" + msg);
        }

    }

    public static void toast(Context context, int resurceId) {
        String msg = context.getResources().getString(resurceId);
        toast(context, msg);
    }

    public static void toast(String Tag, Context context, int resurceId) {
        String msg = context.getResources().getString(resurceId);
        toast(Tag, context, msg);
    }

    public static String listToLog(List<Object> list) {
        StringBuilder builder = new StringBuilder();
        builder.append(list.getClass().getSimpleName() + ",size = " + list.size() + "\n");

        for(int i = 0; i < list.size(); ++i) {
            builder.append(i + "  " + list.get(i).toString() + "\n");
        }

        return builder.toString();
    }

    public static void setLevel(int level) {
        LogUtil.level = level;
    }

    public static void setTAG(String tAG) {
        TAG = tAG;
    }

    public static void setFilemane(String filemane) {
        LogUtil.filemane = filemane;
    }

    private static String getTagInfo(StackTraceElement[] stackTraceElement) {
        StringBuilder builder = new StringBuilder();

        for(int i = 3; i < stackTraceElement.length; ++i) {
            String tag = "%s.%s(%s:%d)";
            String callerClazzName = stackTraceElement[i].getClassName();
            if(!callerClazzName.equals(LogUtil.class.getName())) {
                if(callerClazzName.startsWith("android") || callerClazzName.startsWith("java")) {
                    break;
                }

                callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
                tag = String.format(tag, new Object[]{callerClazzName, stackTraceElement[i].getMethodName(), stackTraceElement[i].getFileName(), Integer.valueOf(stackTraceElement[i].getLineNumber())});
                builder.append(tag);
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    private static void print(String msg) {
        Date currentTime = new Date();
        String currentTimeStamp = (new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss")).format(currentTime);
        String filename = filemane + ".txt";
        String localPath = Environment.getExternalStorageDirectory().getPath() + "/DEBUG" + filemane;
        File file1 = new File(localPath);
        if(!file1.exists()) {
            file1.mkdirs();
        }

        String msagement = currentTimeStamp + "&" + msg + "\n";

        try {
            BufferedWriter e = new BufferedWriter(new FileWriter(localPath + "/" + filename));
            e.write(msagement);
            e.flush();
            e.close();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    private static void print(String msg, Throwable tr) {
        print(msg + "&Throwable:" + tr.getMessage());
    }

    public static void setPRINT(Boolean PRINT) {
        LogUtil.PRINT = PRINT;
    }
}
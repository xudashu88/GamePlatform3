package com.purity.yu.gameplatform.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtil {
    private static GZipUtil instance = null;

    private GZipUtil() {
    }

    public static GZipUtil getInstance() {
        if (null == instance) {
            instance = new GZipUtil();
        }
        return instance;
    }

    public static void zip(File srcFile, File desFile) throws IOException {
        GZIPOutputStream zos = null;
        FileInputStream fis = null;
        try {
//创建压缩输出流,将目标文件传入
            zos = new GZIPOutputStream(new FileOutputStream(desFile));
//创建文件输入流,将源文件传入
            fis = new FileInputStream(srcFile);
            byte[] buffer = new byte[1024];
            int len = -1;
//利用IO流写入写出的形式将源文件写入到目标文件中进行压缩
            while ((len = (fis.read(buffer))) != -1) {
                zos.write(buffer, 0, len);
            }
        } finally {
            close(zos);
            close(fis);
        }
    }

    private static void close(GZIPOutputStream zos) {
        if (zos != null) {
            try {
                zos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(FileInputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void unZip(File srcFile, File desFile) throws IOException {
        GZIPInputStream zis = null;
        FileOutputStream fos = null;
        try {
//创建压缩输入流,传入源文件
            zis = new GZIPInputStream(new FileInputStream(srcFile));
//创建文件输出流,传入目标文件
            fos = new FileOutputStream(desFile);
            byte[] buffer = new byte[1024];
            int len = -1;
//利用IO流写入写出的形式将压缩源文件解压到目标文件中
            while ((len = (zis.read(buffer))) != -1) {
                fos.write(buffer, 0, len);
            }
        } finally {
            close(zis);
            close(fos);
        }
    }

    private static void close(GZIPInputStream zos) {
        if (zos != null) {
            try {
                zos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(FileOutputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return phone + "手机号应为11位数";
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
                return phone + "请填入正确的手机号";
            }
        }
        return phone + "是手机号码";
    }

    public void getRepeat(String str) {
        char[] chars = str.toCharArray();
        int time = 0;
        for (int i = 0; i < chars.length - 1; i++) {
            if (str.indexOf(chars[i]) != str.lastIndexOf(chars[i])) {
                time++;
                System.out.println(time + "--" + str.indexOf(chars[i]) + "--" + chars[i]);
            }
        }
    }

    public static void main(String[] args) {
//        System.out.println(GZipUtil.getInstance().isPhone("19911111111"));
//        System.out.println(GZipUtil.getInstance().isPhone("19811111111"));
//        System.out.println(GZipUtil.getInstance().isPhone("17811111111"));
//        System.out.println(GZipUtil.getInstance().isPhone("17911111111"));
//        System.out.println(GZipUtil.getInstance().isPhone("16611111111"));
        GZipUtil.getInstance().getRepeat("庄莊闲閒和点點路释放釋立即刷完所稍試试朕閱阅打务務器邀停止赢令互房确確始會員会员余餘押注註得开開取分桌台限红紅在线線人数數菜返非傭免佣說说明未托百家樂乐總总下號号厅廳视讯視訊统統存款专区專區安全账賬联系聯繫客微信当天當投量利润潤次补牌補龙龍虎者标题標題常種种文關关頻频推送等待洗連连接匹配眼珠盘盤曱甴切换換電电絡络動移动域网網闭閉需要无效無我钱包錢暂暫敬街断斷该該你妳续續踢将將高清项項卓载載检查檢黑桃梅花块塊王费費澳门門五回收记录記錄设置設游戏遊戲规则規則消息退出额額度今昨一星期内內交易单單操作时间時間类别類入支後后請请選擇选择自定筹码籌碼语言語声音聲其他不能空现現管理過通过申删除部挑订訂议議服付方式昵暱金宝寶银行卡銀提您的姓手机機可前超按應应為为位擊击認认舊旧迷新輸输再修改登对子對最低发發中局场場大小结束結真实實于於整没有更多據据獲获械臂失败敗称稱商册冊简体问問繁體密湾灣太极極软件軟用户名戶或错误錯誤成功已经經被使审核審填正0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }
}

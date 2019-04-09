package com.purity.yu.gameplatform.base;

/**
 * Created by yucaihua on 2018/5/15.
 */

public class Constant {
    public static final String USER_ID = "id";
    public static final String USER_PWD = "password";
    public static final String PROMO_CODE = "promo_code";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_TOKEN_SOCKET = "user_token_socket";
    public static final String USER_NAME = "user_name";
    public static final String WECHAT_ICON = "wechat_icon";
    public static final String LOGIN_TYPE = "login_type";

    public static final String TOGGLE_ON = "toggle_on";//好路排序-開
    public static final String TOGGLE_OFF = "toggle_off";//好路排序-關
    public static final String CHIP_POSITION = "chip_position";//选中的筹码位置
    public static final String IS_EXPAND = "is_expand";//百家乐实时走势图缩进按钮 0-一半 1-隐藏  2-完全展开
    public static final String ROOM8 = "rtmp://119.188.246.219/live/bl01a";//视频1  rtmp://119.188.246.219/live/room101a rtmp://14.29.32.79/live/fish1 rtmp://119.188.246.219/live/room101a
    public static final String ROOM9 = "rtmp://119.188.246.219/live/bl01b";//视频2 rtmp://153.36.201.57/live/room5b rtmp://live.hkstv.hk.lxdns.com/live/hks rtmp://119.188.246.219/live/bl01b rtmp://119.188.246.219/live/room101b
    public static final String SOUND_SWITCH = "sound_switch";//设置-音效开关 0-开 1-关
    public static final String MUSIC_SWITCH = "music_switch";//设置-音乐开关 2-开 1-关
    public static final String VIDEO_SWITCH = "video_switch";//设置-视频开关 0-开 1-关
    public static final String PUSH_SWITCH = "push_switch";//设置-推送开关 0-开 1-关
    public static final String MUSIC_VOLUME = "music_volume";//设置-背景音量
    public static final String WHERE_SET = "where_set";//1(TabHostActivity) 2(BaccaratListActivity) 3(DtListActivity) 设置从哪里进就从哪里出
    public static final String WHERE_SETED = "where_seted";//1(TabHostActivity) 2(BaccaratListActivity) 3(DtListActivity) 设置从哪里进就从哪里出 只有重新选择了语言才会跳转指定Activity
    public static final String BAC_VIDEO_PLAYING = "bac_video_playing";//0-从未执行过百家乐视频 1-已执行
    public static final String VIDEO_PLAYING = "video_playing";//0-首次进入视频 1-获取视频分辨率 退出要重置

    public static final String ARCADE_TEST = "arcade_test";//测试 8个座位 119.147.144.26:9527
    public static final String ARCADE_TEST_IP = "119.147.144.26:9527";//测试IP
    public static final String CROCILISK_PARK = "crocilisk_park";//鳄鱼公园 8个座位 119.147.144.195:9527
    public static final String CROCILISK_PARK_IP = "119.147.144.195:9527";//鳄鱼公园IP
    public static final String ROBORAPTOR = "roboraptor";//雷霸龙 8个座位 119.147.144.26:9527
    public static final String ROBORAPTOR_IP = "119.147.144.26:9527";//雷霸龙IP
    public static final String CRAZY_BIRD = "crazy_bird";//疯狂小鸟 8个座位 218.77.100.252:9527
    public static final String CRAZY_BIRD_IP = "218.77.100.252:9527";//疯狂小鸟IP
    public static final String BEAUTY_AND_THE_BEAST = "beauty_and_the_beast";//美女与野兽 8个座位 218.77.100.252:9528
    public static final String BEAUTY_AND_THE_BEAST_IP = "218.77.100.252:9528";//美女与野兽IP
    public static final String ARCADE_NULL = "arcade_null";//美女与野兽IP


    public static final String FISH2 = "rtmp://14.29.32.79/live/fish2";//打鱼rtmp://14.29.32.79/live/fish1 rtmp://119.188.246.219/live/fish2
    public static final String FISH_1 = "rtmp://192.168.0.2/live/fish2";//打鱼rtmp://192.168.0.2/live/fish2 rtmp://120.236.191.50/live/fish1
    public static final String FISH_2 = "rtmp://120.236.191.50/live/fish2";//打鱼
    public static final String FISH_3 = "rtmp://120.236.191.50/live/fish3";//打鱼
    public static final String FISH_4 = "rtmp://120.236.191.50/live/fish4";//打鱼
    public static final String FISH_5 = "rtmp://120.236.191.50/live/fish5";//打鱼

    public static int SEND_CARD = 25;//百家乐倒计时
    public static final int DANIU_BUFFER = 400;//大牛视频缓冲500
    public static final boolean DANIU_HARDWARE = true;//true 硬解码 false软解码
    //BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
    public static final String BACCARAT_STATE = "baccarat_state";//状态
    public static final String BACCARAT_BET = "BET";//下注
    public static final String BACCARAT_RESULT = "RESULT";//发牌
    public static final String BACCARAT_OVER = "OVER";
    public static final String BACCARAT_WAIT = "WAITTING";//新加一局
    public static final String BACCARAT_CREATED = "CREATED";//新一轮开始
    public static final String BACCARAT_INNINGS_END = "INNINGS_END";//当前轮结束
    public static final String BACCARAT_FAULT = "FAULT";//机械臂异常
    public static final String BACCARAT_INVALID = "INVALID";//本局作废
    public static final String CHIP_PLAY_PAIR = "CHIP_PLAY_PAIR";//闲对下注筹码
    public static final String CHIP_BANK_PAIR = "CHIP_BANK_PAIR";//庄对下注筹码
    public static final String CHIP_BANK = "CHIP_BANK";//庄下注筹码
    public static final String CHIP_PLAY = "CHIP_PLAY";//闲下注筹码
    public static final String CHIP_TIE = "CHIP_TIE";//和下注筹码
    public static final String CHIP_BIG = "CHIP_BIG";//大下注筹码 发5张或者6张牌
    public static final String CHIP_SMALL = "CHIP_SMALL";//小下注筹码 只发4张
    public static final String SELECT_CHIP_NUMBER = "select_chip_number";//选中的筹码
    public static final String SELECT_CHIP_NUMBER_HOLY = "select_chip_number_holy";//盛天选中的筹码
    public static final String SELECT_CHIP_NUMBER_MACAU = "select_chip_number_macau";//澳门选中的筹码
    public static final String SELECT_CHIP_NUMBER_DT = "select_chip_number_dt";//龙虎选中的筹码
    public static final String SELECT_CHIP_NUMBER_BAC = "select_chip_number_bac";//龙虎选中的筹码
    public static final String ASK_BIG_EYE = "ask_big_eye";//大眼路 求出一个另一个相反
    public static final String ASK_SMALL = "ask_small";//小路 求出一个另一个相反
    public static final String ASK_COCKROACH = "ask_cockroach";//小强路 求出一个另一个相反
    public static final String IS_CHIP_SUCCESS = "is_chip_success";//当前局已下注就不能退出房间
    public static final String BET_LIMIT = "bet_limit";//下注限红
    public static final String MAX_BET = "max_bet";//最大下注
    public static final String MIN_BET = "min_bet";//最小下注
    public static final String HOLY_DAY_CHIP = "holy_day_chip";//盛天选中筹码
    public static final String HOLY_DAY_CHIP_NO = "holy_day_chip_no";//盛天未选中筹码
    public static final String MACAU_CHIP = "macau_chip";//澳门五路筹码
    public static final String MACAU_CHIP_NO = "macau_chip_no";//澳门五路未选中筹码
    public static final String DT_CHIP = "dt_chip";//龍虎五路筹码
    public static final String DT_CHIP_NO = "dt_chip_no";//龍虎未选中筹码

    public static final String BAC_ROOM_COUNT = "bac_room_count";//百家乐房间数
    public static final String DT_ROOM_COUNT = "dt_room_count";//龙虎房间数
    public static final String SINGLE_ROOM_COUNT = "single_room_count";//单挑房间数
    public static final String FISH_ROOM_COUNT = "fish_room_count";//扑鱼房间数


    public static final String BET0 = "bet0";//沒有任何点击时才初始化position=0为显示状态
    //    public static final String LOGIN = "user/login";
    public static final String LOGIN = "/login";
    public static final String LOGIN_OUT = "/logout";
    //    public static final String REGISTER = "user/register";
    public static final String REGISTER = "/register";

    public static final String LOGIN_INFO = "/platform/logininfo";//皇家获取token和userid
    public static final String ADD_AMOUNT = "/platform/add-amount";//每个大厅列表都要上分
    //    public static final String BET_RECORD = "user/gameRecord/list";//下注记录
    public static final String BET_RECORD = "/bet/list";
    public static final String QUOTA_RECORD = "/trade/list";//额度记录
    //    public static final String QUOTA_RECORD = "user/expenseRecord/list";//额度记录
    public static final String LOGIN_VALIDATE = "user/login/validateAuthorization";
    public static final String ROOM_COUNT = "user/roomCount";
    public static final String USER_INFO = "/user/info";
    //    public static final String PAY_CODE = "user/payment/findByName";//支付二维码或者卡号
    public static final String PAY_CODE = "bank/bar-code";//支付二维码
    public static final String PAY_BANK = "bank/company-bank";//银行卡信息
    //    public static final String PAY = "user/pay";
    public static final String PAY = "/deposit/apply";

    public static final String ADD_CARD = "/bank/add";
    public static final String WITHDRAW = "withdraw/apply";

    //    public static final String WITHDRAW = "user/withdraw";
    public static final String HJ_BASE_URL = "http://43.249.206.216/";
    public static final String SERVICE_WEB_URL = HJ_BASE_URL + "mobile/index.html";
    public static final String HJ_RECYCLE = "/platform/amount";
    public static final String NOTICE = "/notice";
    public static final String NO_READ = "/message/noread";
    public static final String MESSAGE_LIST = "/message/list";
    public static final String MESSAGE_REMOVE = "/message/remove";
    public static final String BANK_REMOVE = "/bank/remove";
    public static final String MESSAGE_DETAIL = "/message/info";
    public static final String USER_ACCOUNT = "/user/account";
    public static final String USER_PASSWORD_PAY = "/user/password-pay";
    public static final String USER_PASSWORD = "/user/password";
    public static final String GAME_LIST = "/game/list";
    public static final String BANK_LIST = "/bank/list";
    public static final String APP_INFO = "/app/info?app_id=f09d13c7c7a6fd5683b0bd69aeb45ec3";//这个写死了 f09dl3c7c7a6fd5683b0bd69aeb45ec3
    public static final String DEPOSIT_FIND = "/deposit/find";//获取存款申请的状态 1=审核中 2=审核通过
    public static final String WITHDRAW_FIND = "/withdraw/find";//获取取款申请的状态 1=审核中 2=审核通过
    public static final String SYS_NOTICE_LIST = "/sys-notice/list";

    public static final String PRE_GAME_LIST = "gameList";//提前加载游戏列表的数据
    public static final String PRE_BAC_LIST = "bacList";//提前加载百家乐的数据
    public static final String PRE_DT_LIST = "dtList";//提前加载龙虎的数据
    public static final String PRE_SINGLE_LIST = "singleList";//提前加载单挑的数据
    public static final String MONEY = "money";
    public static final String CLOSE_ALL_VIDEO = "close_all_video";//默认0开启 1关闭
    public static final String ADDS = "adds";//系统公告

    /**
     * 60秒倒计时
     */
    public static final int MAX_SECOND = 3;
    /**
     * Handler倒计时状态码
     */
    public static final int WHAT_COUNT_DOWN = 0x001;
}

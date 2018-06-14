package house.shen.com.house;

public class Key {


    /**
     * 请求服务器的参数Key
     */
    public static class ParamKey {
        public static final String MOBILE = "mobile";
        public static final String SMS_CODE = "sms_code";
        public static final String APPVER = "appver";
        public static final String SDKVER = "sdkver";
        public static final String OS = "os";
        public static final String STAT_KEY_ABTEST = "abversion";
        public static final String NB = "nb";
        public static final String SIGN = "sign";
        public static final String TS = "ts";
        public static final String TOKEN = "token";
    }


    /**
     * Intent传递的字段
     */
    public static final class IntentKey {
        public static final String URL = "url";
        public static final String TYPE = "type";
        public static final String CITY = "city";
        public static final String CITYNAME = "city_name";
        public static final String DEALER_ID = "dealerid";
        public static final String DEALER_TYPE = "dealertype";
        public static final String LOCATION = "location";
        public static final String MOBILE = "mobile";
        public static final String OUTPOS = "outpos";
        public static final String INPOS = "inpos";
        public static final String USEBROADCASTRECEIVER = "useBroadcastReceiver";
        public static final String FROM = "from";
        public static final String NEXTACTIVITY = "nextactivity";
        public static final String BACK = "back";
        public static final String BRAND = "brand";
        public static final String BRANDID = "brandid";
        public static final String BRANDNAME = "brandname";
        public static final String SERIESID = "seriesid";
        public static final String SERIESNAME = "seriesname";
        public static final String MODE_ID = "mode_id";
        public static final String MODE_NAME = "mode_name";
        public static final String SEARCHWORD = "searchword";
        public static final String SEARCHHOTWORD = "searchhotword";
        public static final String SEARCHTEXT = "searchtext";
        public static final String SEARCHCAR = "searchcar";
        public static final String SEARCHFROM = "searchfrom";
        public static final String SEARCHSTATISBEAN = "searchstaitsbean";
        public static final String WINDOW_MASK = "window_mask";
        public static final String WINDOW_FLAG = "window_flag";
        public static final String HAS_SERIES = "has_series";
        public static final String SERIES_OPEN = "series_open";
        public static final String ALL_BRANDS = "all_brands";
        public static final String FINDCAR_CONDITION = "condition";
        public static final String ISFOLLOW = "isfollow";
        public static final String SEARCHTYPE = "searchtype";//搜索setresult:1新车,2 U2
        public static final String SEARCHJUMPFROM = "searchjumpfrom";//
        public static final String CARTYPE = "cartype";//1新车,2二手车
        public static final String SEARCHSHOWTAB = "searchshowtab";
        public static final String CARID = "car_id";
        public static final String ARTICLEID = "articleid";
        public static final String BACKTIME = "backtime";
        public static final String SHOP4SID = "shop4sid";
    }

    public static final class RequestCode {
        public static final int STARTSEARCH = 100;
        public static final int CODE_LOGIN = 3;// 登录
        public static final int CODE_LOGIN_FOLLOWCAR = 4;// 关注的车登录
        public static final int CODE_LOGIN_ORDERLIST = 5;// 订单登录
        public static final int CODE_LOGIN_RESERVELIST = 6;// 预约登录
        public static final int CODE_ALL_BRAND = 7;
        public static int CODE_CITY = 1;
        public static int CODE_DETAIL = 2;
    }

    public static final class ResultCode {

    }


    public static final class BroadCastKey {
        public static final String LOGIN = "login";
        public static final String CHANGECITY = "changecity";
        public static final String MYFOLLOW = "MYFOLLOW";
    }

    public static final class SPKEY {
        public static final String SEARCH_HISTORY = "search_history_1_3";
        public static final String SEARCH_HISTORY2 = "search_history2";//新增的U2的历史
        public static final String SPKEY_CITYINFO = "spkey_cityinfo";
        public static final String SPKEY_USERINFO = "spkey_userinfo";
    }
}

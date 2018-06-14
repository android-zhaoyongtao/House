package house.shen.com.house.http;


/**
 * Created by dsr on 16/8/15.
 */
public class UrlConfig {
    public static final String FORMAT_DEVELOP_URL = "http://%1$s.api.test.dabanma.com/zz/";
    public static final String RELEASE_API_URL = "https://appapi.youxinche.com/zz/";
    public static final String HOST_API = ""; //ContextXin.DEBUG ? 1 == SPUtils.getIntFromSP("beta", 1) ? String.format(FORMAT_DEVELOP_URL, SPUtils.getStringFromSP("deve_path", "develop")) : RELEASE_API_URL : RELEASE_API_URL;
//    public static final String STATISURL = ContextXin.DEBUG ? "http://ab.test.xin.com/post_xczz.gif" : "https://ab.xin.com/post_xczz.gif";

    public static final String STATISTICS_ERROR = HOST_API + "log/client_error";
    public static final String GET_INFO = HOST_API + "general/get_info";
    public static final String GET_CITY = HOST_API + "general/get_city";
    public static final String MODE_CITY_LIST = HOST_API + "car/mode_city_list";// 根绝车型获取直租支持的城市
    public static final String CITYLIST = HOST_API + "general/city_list";
    public static final String FINDCAR_LIST = HOST_API + "car/zz_list";
    public static final String USEDCAR_LIST = HOST_API + "car/zz_list_u2";
    public static final String FOLLOW = HOST_API + "follow/follow";
    public static final String UNFOLLOW = HOST_API + "follow/unfollow";
    public static final String UPDATE_REGID = HOST_API + "push/update_reg_id";
    public static final String SEND_CODE = HOST_API + "user/send_code";
    public static final String LOGOUT = HOST_API + "user/logout";
    public static final String LOGIN = HOST_API + "user/login";
    public static final String USER_INFO = HOST_API + "user/info";// 个人中心
    public static final String RESERVE_LIST = HOST_API + "reserve/my_list";// 我的预约
    //    public static final String FINDCAR_SHOUFU = HOST_API + "car/zz_selection_dp";
//    public static final String FINDCAR_YUEGONG = HOST_API + "car/zz_selection_mp";
    public static final String FINDCAR_BRAND_SUPPORT_LIST = HOST_API + "car/all_brands";
    public static final String FINDCAR_BRAND_ALL_LIST = HOST_API + "general/brand_list";
    public static final String FINDCAR_U2_SERIES = HOST_API + "car/zz_series";
    public static final String FINDCAR_BRAND_ALL_SERIES = HOST_API + "general/series_list";
    //    public static final String FINDCAR_SORT_TYPE = HOST_API + "car/zz_selection";
    public static final String MYORDERS = HOST_API + "order/get_list";  //我的订单
    public static final String MYFOLLOW = HOST_API + "follow/get_list";  //关注的车
    public static final String SEARCH = HOST_API + "search/findcar_suggest";  //搜索suggestion http://doc.xin.com/pages/viewpage.action?pageId=4238861
    public static final String SEARCH_HOT = HOST_API + "search/hot_word";// 热门搜索/搜索热词
    public static final String LOGINOUT = HOST_API + "user/logout";//退出登录
    public static final String FEEDBACK = HOST_API + "car/feedback";// 意见反馈
    public static final String HOMEPAGE_COMMON = HOST_API + "page/index_common";//wiki:http://doc.xin.com/pages/viewpage.action?pageId=4251763
    public static final String HOMEPAGE_PERSONAL = HOST_API + "page/index_personal";
    public static final String ISFOLLOW = HOST_API + "follow/isfollow";
    public static final String CARDETAIL = HOST_API + "car/detail";
    public static final String GETADVERT = HOST_API + "operation/advert";//弹窗,开屏
    public static final String RESEVER_CARINFO = HOST_API + "car/mode_info";// 立即预约车型信息
    public static final String RESEVER_CAR_SUBMIT = HOST_API + "reserve/do_reserve";// 立即预约
    public static final String WISH_SUBMIT = HOST_API + "wish/handle";// 心愿单提交
    public static final String DYNAMIC_TEL400 = HOST_API + "car/dynamic_tel400";// 400
    public static final String DO_FOLLOW_4U2 = HOST_API + "follow/do_follow_4u2";// 二手车详情页 关注
    public static final String IS_FOLLOW_4U2 = HOST_API + "follow/is_follow_4u2";// 二手车详情页 关注状态
    public static final String OUT_CALL = HOST_API + "reserve/out_call";// 直租二手车预约免费电话
    public static final String PURCHASE_PROCESS = HOST_API + "car/purchase_process";// 直租二手车购车流程
    public static final String U2_DETAIL_CONFIG = HOST_API + "car/u2_detail_config";// 直租二手车购车流程

    public static final String MYFLOLLOW_OLDCAR = HOST_API + "follow/get_list_4u2";// 关注的车 二手车
    public static final String CHECK_VERSION_UPDATE = HOST_API + "general/check_version_update";// 关注的车 二手车
    public static final String ARTICLE_DETAIL = HOST_API + "article/detail";// 车主故事详情http://doc.xin.com/pages/viewpage.action?pageId=5249291
    public static final String ARTICLE_RECOMMEND = HOST_API + "car/recommend";// 车主故事下边车型推荐http://doc.xin.com/pages/viewpage.action?pageId=5249704
    public static final String STORE_DETAIL = HOST_API + "dealer/detail";// 店铺页http://doc.xin.com/pages/viewpage.action?pageId=5932400
    public static final String DETAIL_ZZ_TIP = HOST_API + "car/tip";// 引导用户留资提示
    public static final String USER_DIRECT_ISSUBMITDIRECTRENT = HOST_API + "/user_direct/issubmitdirectrent";//是否提交过直租信审
}

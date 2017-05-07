package qg.fangrui.boot.enums;


public enum StatEnum {


    DEFAULT_WRONG(-1,"其他错误"),
    /**
     * 登录板块
     */
    LOGIN_SUCCESS(121,"登录成功"),
    LOGIN_NOT_EXIT_USER(122,"不存在的用户"),
    LOGIN_USER_MISMATCH(123,"用户名或密码错误"),
    /**
     * 忘记密码板块
     */
    PASSWORD_CHANGE_SUCCESS(131,"修改密码成功"),
    PASSWORD_EMPTY_USER(132,"空用户对象"),
    PASSWORD_FAMMTER_FAULT(134,"修改密码格式错误"),
    PASSWORD_MISMATCH(135,"旧密码错误"),
    /**
     * 修改用户信息板块
     */
    INFORMATION_CHANGE_SUCCESS(141,"修改信息成功"),
    INFORMATION_EMPTY_USER(142,"空用户对象"),
    INFORMATION_FORMMATTER_FAULT(143,"修改信息格式错误"),
    INFORMATION_GET(145, "获取用户信息"),
    /**
     * 系统通知信息
     */
    INFO_LIST(151, "获得用户系统通知列表"),
    INFO_INSERT_SUCCESS(152, "插入系统通知成功"),
    INFO_INSERT_FAULT(153, "插入系统通知失败"),
    INFO_DELETE_SUCCESS(154,"删除系统通知成功"),
    INFO_DELETE_FAULT(155,"删除系统通知失败"),
    INFO_UPDATE_SUCCESS(156,"更新系统通知成功"),
    INFO_UPDATE_FAULT(157, "更新系统通知失败"),
    INFO_PARAM_ERROR(158, "非法参数"),
    INFO_NO_EXIT_ERROR(159, "未找到该条系统通知"),
    /**
     * 好友关系列表
     */
    RELATION_LIST(161,"获取好友关系列表"),
    RELATION_ADD_SUCCESS(162, "添加好友成功"),
    RELATION_ADD_FAULT(163, "添加好友失败"),
    RELATION_DELETE_SUCCESS(164, "删除好友成功"),
    RELATION_DELETE_FAULT(165, "删除好友失败"),
    RELATION_IS_EXIT(166, "已经存在了好友关系"),
    RELATION_ISNOT_EXIT(167, "不存在好友关系"),
    /**
     * 聊天记录
     */
    CHAT_LOG_List(171, "获得未读消息记录"),
    CHAT_LOG_CREATE_SUCCESS(172, "创建聊天室成功"),
    CHAT_LOG_ISNOT_EXIT(173,"找不到相应的聊天室"),
    CHAT_LOG_ADD_SUCCESS(174, "加入聊天室成功"),
    CHAT_LOG_DELETE_SUCCESS(175, "退出聊天室成功"),
    CAHT_LOG_ALL_USER(176, "获得聊天室用户列表"),
    CHAT_LOG_ROOM_INFO(177, "获得聊天室信息"),
    CHAT_LOG_HAS_EXIT(178, "用户已经存在聊天室中"),
    /**
     * 文件模块
     */
    SUBMIT_FILE_SUCCESS(181,"文件上传成功"),
    SUBMIT_FILE_FAULT(182, "文件上传失败"),
    SUBMIT_WRONG_FILE(183,"上传文件格式错误"),
    /**
     * 朋友圈模块
     */
    TRENDS_List(191, "获得朋友圈列表"),
    TRENDS_MY_LIST(192, "获得用户自己的朋友圈"),
    TRENDS_ADD_SUCCESS(193, "添加朋友圈成功"),
    TRENDS_ADD_FAULT(194, "添加朋友圈失败"),
    TRENDS_DELETE_SUCCESS(195, "用户删除朋友圈成功"),
    TRENDS_DELETE_FAULT(196, "用户添加朋友圈成功"),
    TRENDS_PHOTO_TOO_MORE(197, "用户上传图片超过9张"),
    TRENDS_ONLY_PHOTO(198, "只允许上传JPG图片"),
    /**
     * 路线模块
     */
    ROUTE_INSERT_SUCCESS(201, "路线保存成功"),
    ROUTE_INSERT_FAULT(202, "路线保存失败"),
    ROUTE_NOT_EXIE(203, "该条路线不存在"),
    ROUTE_DELETE_SUCCESS(204, "路线删除成功"),
    ROUTE_GET_BY_ID(205, "获取一条路线"),
    ROUTE_List(206, "用户获取路线列表"),
    ROUTE_GET_PATH(207, "用户获得匹配路线"),

    /**
     * Other
     */
    ALL_QUANZI(301, "获得用户所有圈子"),
    RECOMMEND_FRIEND(302, "获得推荐好友"),
    /**
     * 商家信息板块
     */
    BUSINESS_LIST(601, "获取商家列表"),
    GOODS_LIST(602, "获取商品列表"),
    ORDER_LIST(603, "获取订单列表"),
    ALL(999,"test");

    private  int state;
    private  String stateInfo;

    StatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }
    public  static  StatEnum statOf(int index) {
        for (StatEnum state : values()) {
            if (state.getState() == index) {
                return  state;
            }
        }
        return  null;
    }
}

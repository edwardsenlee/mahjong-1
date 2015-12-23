package com.ahliu.test.mahjong.util;

public class ResponseCode {

	public static final int SUCCESS = 1;
	public static final int ERR_INVOKE_FAILED = -1;

	public static final int ERR_LOGIN_ALREADY_REGISTERED = 10001;
	public static final int ERR_LOGIN_NOT_EXISTED = 10002;
	public static final int ERR_LOGIN_FAILED = 10003;
	public static final int ERR_ALREADY_LOGIN = 10004;
	public static final int ERR_NOT_LOGGED_IN = 10005;

	public static final int ERR_GAME_NOT_EXISTED = 20001;
	public static final int ERR_GAME_OCCUPIED = 20002;
	public static final int ERR_GAME_NOT_SELECTED = 20003;
	public static final int ERR_GAME_ALREADY_STARTED = 20004;

}

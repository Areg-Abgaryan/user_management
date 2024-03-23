/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.controllers;

public class EndpointsConstants {

    public static final String ALL = "/**";
    public static final String API = "/api";
    public static final String GET_ALL = "/all";

    public static final String AUTH = API + "/auth";
    public static final String USER = API + "/user";

    public static final String USER_GROUP = API + "/user_group";
    public static final String ROLE = API + "/role";

    public static final String SIGNUP = "/signup";
    public static final String VERIFY_EMAIL = "/verify_email";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REFRESH_TOKEN = "/refresh_token";

    public static final String AUTH_SIGNUP_VERIFY_EMAIL = AUTH + SIGNUP + VERIFY_EMAIL;
}

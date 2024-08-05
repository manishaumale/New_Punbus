package com.idms.base.support.rest;

public final class RestConstants {
	
	public static final String API_BASE 			= "/api";
	public static final String API_BASE_STATE 			= "/api";
    public static final String PREAUTH_HEADER_LABEL = "X-USERID";
    public static final String PREAUTH_USER_ROLE 	= "ROLE_USER";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String USER_INFO_URL = "/realms/IDMSPunjab/protocol/openid-connect/userinfo";
    public static final String ROLE_CREATE_URL = "/admin/realms/IDMSPunjab/clients/";
    public static final String GET_GROUPS_URL = "/admin/realms/IDMSPunjab/groups/";
    public static final String USER = "/admin/realms/IDMSPunjab/users";
    public static final String SCHEMA_NAME = "punbus_dev";
    public static final String VTS_TYRE_KMS_API="https://punbus.itracking.in/api_sms/api_day_distance.php";
    
    public static final String CDAC_API_PUSH = "https://msdgweb.mgov.gov.in/esms/sendsmsrequestDLT";
    
    public static final String[] AlertRoles ={"DEPOTADMIN","GM"};

}
package cn.flyexp.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.util.SparseArray;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PermissionInterceptor{
  private static Method CHECKSELFPERMISSION_METHOD;
  private static Method REQUESTPERMISSIONS_METHOD;
  private static Method SHOULDSHOWREQUESTPERMISSIONRATIONALE_METHOD;

  private static final String METHOD_NAME_CHECK_SELF_PERMISSION = "checkSelfPermission";
  private static final String METHOD_NAME_REQUEST_PERMISSIONS = "requestPermissions";
  private static final String METHOD_NAME_SHOULDSHOW_REQUEST_PERMISSION_RATIONALE = "shouldShowRequestPermissionRationale";
  private static boolean MNC_FLAG = false;

  static  {
    MNC_FLAG = "MNC".equals(VERSION.CODENAME)|| VERSION.SDK_INT >= 23;
  }

  /**
   * 是否有权限paramstring
   */
  public static boolean hasPermission(Context paramContext, String paramString) {
    return  PackageManager.PERMISSION_GRANTED == checkSelfPermission(paramContext, paramString);
  }

  /**
   * 系统权限提醒对话框中，是否有不再提醒的框
   * 第一次申请此权限不会有，后面都会有；
   * 但用户勾选不再提醒并拒绝，则也不会有
   */
  public static boolean shouldShowRequestPermissionRationale(Context paramContext, String paramString) {
    if (!isMNC()) {
      return false;
    }
    if (paramContext == null) {
      return false;
    }
    try{
      if (SHOULDSHOWREQUESTPERMISSIONRATIONALE_METHOD == null) {
        SHOULDSHOWREQUESTPERMISSIONRATIONALE_METHOD = paramContext.getClass().getMethod(METHOD_NAME_SHOULDSHOW_REQUEST_PERMISSION_RATIONALE, new Class[] { String.class });
        SHOULDSHOWREQUESTPERMISSIONRATIONALE_METHOD.setAccessible(true);
      }
      return ((Boolean)SHOULDSHOWREQUESTPERMISSIONRATIONALE_METHOD.invoke(paramContext, new Object[] { paramString })).booleanValue();
    }
    catch (Exception localException){
      localException.printStackTrace();
    }
    return false;
  }

  static int checkSelfPermission(Context paramContext, String paramString) {
    if (!isMNC()) {
      return PackageManager.PERMISSION_GRANTED;
    }
    if (paramContext == null) {
      return PackageManager.PERMISSION_GRANTED;
    }
    try{
      if (CHECKSELFPERMISSION_METHOD == null) {
        CHECKSELFPERMISSION_METHOD = paramContext.getClass().getMethod(METHOD_NAME_CHECK_SELF_PERMISSION, new Class[] { String.class });
        CHECKSELFPERMISSION_METHOD.setAccessible(true);
      }
      int i = ((Integer)CHECKSELFPERMISSION_METHOD.invoke(paramContext, new Object[] { paramString })).intValue();
      return i;
    }
    catch (Exception localException){
      localException.printStackTrace();
    }
    return PackageManager.PERMISSION_GRANTED;
  }
  
  private static boolean isMNC(){
    return MNC_FLAG;
  }
  
  public static void requestPermissions(Context paramContext, String[] paramArrayOfString, PermissionHandler handler) {
    REQUESTS.put(generateStringArrayHashCode(paramArrayOfString), handler);
    if (paramContext == null) {
        fail(paramArrayOfString);
        return;
    }
    try {
      if (REQUESTPERMISSIONS_METHOD == null){
        Class localClass = paramContext.getClass();
        Class[] arrayOfClass = new Class[2];
        arrayOfClass[0] = String[].class;
        arrayOfClass[1] = Integer.TYPE;
        REQUESTPERMISSIONS_METHOD = localClass.getMethod(METHOD_NAME_REQUEST_PERMISSIONS, arrayOfClass);
        REQUESTPERMISSIONS_METHOD.setAccessible(true);
      }
      Method localMethod = REQUESTPERMISSIONS_METHOD;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramArrayOfString;
      arrayOfObject[1] = Integer.valueOf("200");
      localMethod.invoke(paramContext, arrayOfObject);
      return;
    }
    catch (Exception localException){
      localException.printStackTrace();
      fail(paramArrayOfString);
    }
  }

    private static void fail(String[] paramArrayOfString){
        int[] re = new int[paramArrayOfString.length];
        for (int i = 0; i < re.length; i++) {
            re[i] = PackageManager.PERMISSION_DENIED;
        }
        requestResult(paramArrayOfString, re);
    }


    private static int generateStringArrayHashCode(String[] paramArrayOfString) {
        if ((paramArrayOfString != null) && (paramArrayOfString.length > 0)){
            int sum = 1;
            for (String s : paramArrayOfString) {
                sum = 101 * sum + s.hashCode();
            }
            return sum;
        }
        return -1;
    }

    static SparseArray<PermissionHandler> REQUESTS = new SparseArray<PermissionHandler>();

    public static void requestResult(String[] pers, int[] re){
        PermissionHandler handler = REQUESTS.get(generateStringArrayHashCode(pers));
        List<String> dined = new ArrayList<String>(re.length);
        if (handler != null) {
            for (int i = 0; i < re.length; i ++) {
                if (re[i] == PackageManager.PERMISSION_DENIED) {
                    dined.add(pers[i]);
                }
            }
            if (dined.isEmpty()) {
                handler.onSuccess();
            } else {
                handler.onFail(dined);
            }
        }
    }
}
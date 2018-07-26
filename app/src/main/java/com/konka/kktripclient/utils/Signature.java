package com.konka.kktripclient.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: Signature
 * @Description: MD5加签，验签
 * @version V1.0
 */
public class Signature {

    /**
     * 对参数进行MD5加签
     *
     * @param sortedParams 需要加签的所有参数
     * @param ParamKey     电视端的业务：该参数使用请求上传的参数randomnum从统一用户系统或者用户中心获取key。
     *                     异步通知第三方业务：该参数来自商户创建时生成的key。
     *                     参数sign、randomnum不参与加签。
     * @param charset
     * @return String
     * @throws
     * @Title: doSign
     * @Description: 对参数进行MD5加签
     */
    public static String doSign(Map<String, String> sortedParams, String ParamKey, String charset) {
        try {
            StringBuffer content = new StringBuffer();
            List<String> keys = new ArrayList<String>(sortedParams.keySet());
            Collections.sort(keys);
            int index = 0;
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = sortedParams.get(key);
                if (areNotEmpty(key, value)) {
                    content.append((index == 0 ? "" : "&") + key + "=" + value);
                    index++;
                }
            }
            String Server_Sign = MD5Util.MD5Encode(content + "&key=" + ParamKey, charset).toUpperCase();
            return Server_Sign;
        } catch (Exception e) {
            //异常处理
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对参数进行验签
     *
     * @param sortedParams 请求传入的所有参数
     * @param ParamKey     电视端的业务：该参数使用请求上传的参数randomnum从统一用户系统或者用户中心获取key。
     *                     异步通知第三方业务：该参数来自商户创建时生成的key。
     *                     参数sign、randomnum不参与验签。
     * @param sortedParams
     * @param charset
     * @return String
     * @throws
     * @Title: verifySign
     * @Description: 对参数进行验签
     */
    public static boolean verifySign(Map<String, String> sortedParams, String ParamKey, String charset){
        try {
            if (sortedParams == null) {
                return false;
            }
            String Client_Sign = sortedParams.get("sign");
            sortedParams.remove("sign");//sign不参与MD5计算
            sortedParams.remove("randomnum");//randomnum不参与MD5计算；第三方业务的验签不用传该参数。
            StringBuffer content = new StringBuffer();
            List<String> keys = new ArrayList<String>(sortedParams.keySet());
            Collections.sort(keys);//排序
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = sortedParams.get(key);
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            }
            String Server_Sign = MD5Util.MD5Encode(content + "&key=" + ParamKey, charset).toUpperCase();
            if (Server_Sign.equals(Client_Sign)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            //异常处理
            e.printStackTrace();
        }
        return false;
    }

    private static boolean areNotEmpty(String key, String value) {
        if(key!=null && value!=null && !key.equals("") && !value.equals(""))
            return true;
        else
            return false;
    }

}

package com.example.channel.utils;

import android.widget.EditText;
import android.widget.TextView;

import com.example.channel.model.impl.MaterialModelImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    public static String getEdit(EditText et){
        if (et == null)
            return "";
        return getStr(et.getText().toString().trim());
    }

    public static String getText(TextView tv){
        if (tv == null)
            return "";
        return getStr(tv.getText().toString().trim());
    }

    public static String getStr(String s){
        if (s == null)
            return "";
        return s;
    }

    //验证手机号是否正确ֻ
    public static boolean isMobileNO(String s) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static String getMaterials(String materialStr){
        if (materialStr == null)
            return "";
        String[] a = materialStr.split(";");
        if (a.length == 0)
            return "";
        List<MaterialModelImpl.Material1> material1List = new ArrayList<>();
        for (String b : a){
            String[] c = b.split(":");
            if (c.length < 3)
                break;
            MaterialModelImpl.Material1 material1 = new MaterialModelImpl.Material1();
            material1.setMaterials_id(c[0]);
            material1.setName(c[1]);
            material1.setMaterialNum(Integer.valueOf(c[2]));
            material1List.add(material1);
        }
        if (material1List.size() == 0)
            return "";
        return GsonUtils.GsonString(material1List);
    }

    public static int getInt(String str){
        if (isNumeric(str))
            return Integer.valueOf(str);
        else
            return 0;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}

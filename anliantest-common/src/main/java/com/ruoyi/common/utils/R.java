package com.ruoyi.common.utils;

import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class R extends HashMap<String, Object> {
    //
    private static final long serialVersionUID = -8157613083634272196L;

    public R() {
        put("code", 200);
        put("msg", "success");
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        r.put("code", 200);
        return r;
    }

    public static R data(Object obj) {
        R r = new R();
        r.put("code", 200);
        r.put("data", obj);
        return r;
    }
    public static R ok(String msg,Object obj) {
        R r = new R();
        r.put("msg", msg);
        r.put("code", 200);
        r.put("data", obj);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static R resultData(List<?> list) {
        PageInfo<?> pageInfo = new PageInfo(list);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("list", list);
        m.put("currPage", pageInfo.getPageNum());
        m.put("pageSize", pageInfo.getPageSize());
        m.put("totalCount", pageInfo.getTotal());
        m.put("totalPage", pageInfo.getPages());
        return R.data(m);
    }

}
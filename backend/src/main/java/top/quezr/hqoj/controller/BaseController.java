package top.quezr.hqoj.controller;

import top.quezr.hqoj.entity.Result;
import top.quezr.hqoj.support.Response;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/11 19:36
 */
public class BaseController {

    protected <T> Response<T> success(T obj){
        return new Response<T>().success(obj);
    }

    protected <T> Response<T> failure(String message,T obj){
        return new Response<T>().failure(message,obj);
    }

    protected  <T> Response<T> returnResult(Result<T> result) {
        Response<T> response = new Response<>();
        if (result.isSuccess()){
            return response.success(result.getData());
        }else {
            return response.failure(result.getMessage(),result.getData());
        }
    }

}

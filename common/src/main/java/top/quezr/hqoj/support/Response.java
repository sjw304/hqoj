package top.quezr.hqoj.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "通用响应体")
public class Response<T> {

    private static final String OK = "ok";
    private static final String ERROR = "error";

    @ApiModelProperty("是否成功")
    private boolean success;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("数据、报错码")
    private T data;

    private static final Response<Void> succInstance = new Response<Void>().success();

    public static Response<Void> staticSuccess(){
        return succInstance;
    }

    public Response<T> success(){
        message=OK;
        success=true;
        return this;
    }

    public Response<T> success(T data){
        message=OK;
        success=true;
        this.data=data;
        return this;
    }

    public Response<T> failure(){
        message=ERROR;
        success=false;
        return this;
    }

    public Response<T> failure(String message){
        this.message=message;
        success=false;
        return this;
    }

    public Response<T> failure(String message, T data){
        this.message=message;
        this.data=data;
        success=false;
        return this;
    }


    public static Response<Integer> failure(ErrorCode errorCode){
        Response<Integer> response = new Response<>();
        response.message=errorCode.message();
        response.data = errorCode.code();
        response.success=false;
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}


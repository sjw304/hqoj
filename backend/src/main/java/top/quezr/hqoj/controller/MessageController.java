package top.quezr.hqoj.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.entity.Message;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.support.Result;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.MessageService;
import top.quezr.hqoj.support.Response;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@RestController
@RequestMapping("/message")
@Slf4j
@Api(tags = "消息服务 Message")
public class MessageController extends BaseController {

    @Autowired
    MessageService messageService;

    @GetMapping
    @Authorization
    @ApiOperation("获取消息分页列表")
    public Response<PageInfo<Message>> getMessagePage(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,PageInfo<Message> pageInfo){
        log.info("user {} get MessagePage .",userId);
        Result<PageInfo<Message>> result = messageService.getMessagePage(pageInfo,userId);
        return returnResult(result);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取某条消息的详情")
    public Response<Message> getMessage(@PathVariable("id") @Validated @Min(0) @NotNull Integer id){
        Message message = messageService.getById(id);
        return new Response<Message>().success(message);
    }

    @GetMapping("/read")
    @ApiOperation("当前用户是否有未读消息")
    public Response<Boolean> hasNoReadMessage(@Validated @Min(0) @NotNull Integer userId){
        Result<Boolean> result =  messageService.getHasNoReadMessage(userId);
        return returnResult(result);
    }

    @PutMapping("/read/{id}")
    @Authorization
    @ApiOperation("将某条消息设为已读")
    public Response<Void> readMessage(@PathVariable("id") Integer id){
        messageService.readMessage(id);
        return Response.staticSuccess();
    }

    @PutMapping("/read")
    @Authorization
    @ApiOperation("一键已读")
    public Response<Void> readAllMessage(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId){
        log.info("user {} read all messages .",userId);
        messageService.readAllMessage(userId);
        return Response.staticSuccess();
    }

    @PutMapping("/unread")
    @Authorization
    @ApiOperation("测试接口：一键未读")
    public Response<Void> unreadAllMessage(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId){
        log.info("user {} read all messages .",userId);
        messageService.unreadAllMessage(userId);
        return Response.staticSuccess();
    }

    @DeleteMapping("/{id}")
    @Authorization
    @ApiOperation("删除某条消息")
    public Response<Void> deleteMessage(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId,@PathVariable("id") @Validated @Min(0) @NotNull Integer id){
        Result<Void> result = messageService.deleteMessage(userId,id);
        return returnResult(result);
    }

    @DeleteMapping
    @Authorization
    @ApiOperation("删除全部已读消息")
    public Response<Void> deleteReadMessages(@RequestAttribute(Authorization.USERID_ATTR) @ApiIgnore Integer userId){
        Result<Void> result = messageService.deleteReadMessages(userId);
        return returnResult(result);
    }

}

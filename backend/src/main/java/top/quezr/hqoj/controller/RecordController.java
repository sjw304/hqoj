package top.quezr.hqoj.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.controller.dto.SubmitDto;
import top.quezr.hqoj.entity.Record;
import top.quezr.hqoj.enums.JudgeStauts;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.RecordService;
import top.quezr.hqoj.support.PageInfo;
import top.quezr.hqoj.support.Response;
import top.quezr.hqoj.support.Result;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/record")
@Api(tags = "记录服务 record")
public class RecordController extends BaseController {

    @Autowired
    RecordService recordService;

    @GetMapping("/{id}")
    @ApiOperation("获取记录详情")
    public Response<Record> getRecordById(@PathVariable("id") Integer id){
        Record record = recordService.getById(id);
        return new Response<Record>().success(record);
    }

    @PutMapping
    @Authorization
    @ApiOperation("提交代码，返回提交id")
    public Response<Integer> submit(@Validated SubmitDto submitDto, @RequestAttribute(Authorization.USERID_ATTR)@ApiIgnore Integer userId){
        Record record = new Record();
        record.setUid(userId);
        record.setCode(submitDto.getCode());
        record.setPid(submitDto.getPid());
        record.setLanguageType(submitDto.getLanguageType());
        record.setState(JudgeStauts.PENDING);
        Result<Integer> result = recordService.submit(record);
        return returnResult(result);
    }

    @GetMapping("/state")
    @ApiOperation("获取提交状态")
    public Response<JudgeStauts> getState(Integer id){
        Result<JudgeStauts> result = recordService.getStateById(id);
        return returnResult(result);
    }

    @GetMapping
    @Authorization
    @ApiOperation("获取用户提交历史（分页）")
    public Response<PageInfo<Record>> getUserRecordPage( @RequestAttribute(Authorization.USERID_ATTR)@ApiIgnore Integer userId,PageInfo<Void> pageInfo){
        PageInfo<Record> pageInfor = new PageInfo<>();
        pageInfor.setHasCount(pageInfo.getHasCount());
        pageInfor.setPageSize(pageInfo.getPageSize());
        pageInfor.setPageNumber(pageInfo.getPageNumber());
        pageInfor.setLastId(pageInfo.getLastId());
        Result<PageInfo<Record>> result = recordService.getUserRecordPage(userId,pageInfor);
        return returnResult(result);
    }


}

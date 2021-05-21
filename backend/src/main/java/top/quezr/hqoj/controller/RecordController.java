package top.quezr.hqoj.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;
import top.quezr.hqoj.controller.dto.SubmitDto;
import top.quezr.hqoj.entity.Record;
import top.quezr.hqoj.security.web.Authorization;
import top.quezr.hqoj.service.RecordService;
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
    @ApiOperation("提交代码")
    public Response<Void> submit(@Validated SubmitDto submitDto, @RequestAttribute(Authorization.USERID_ATTR)@ApiIgnore Integer userId){
        Record record = new Record();
        record.setUid(userId);
        record.setCode(submitDto.getCode());
        record.setPid(submitDto.getPid());
        record.setLanguageType(submitDto.getLanguageType());
        Result<Void> result = recordService.submit(record);
        return returnResult(result);
    }

}

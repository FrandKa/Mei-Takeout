package com.mei.controller.admin;

import com.mei.result.Result;
import com.mei.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 15:31
 **/
@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺接口")
@Slf4j
public class ShopController {
    @Autowired
    private ShopService shopService;

    @ApiOperation("获取店铺状态接口")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = shopService.getStatus();

        return Result.success(status);
    }

    @ApiOperation("设置营业状态接口")
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status) {
        shopService.setStatus(status);
        return Result.success();
    }
}

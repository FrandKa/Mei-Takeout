package com.mei.controller.admin;

import com.mei.dto.SetmealDTO;
import com.mei.result.Result;
import com.mei.service.SetMealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 16:19
 **/
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐接口")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 新建套餐
     * */
    @ApiOperation("新建套餐接口")
    @PostMapping
    public Result addSetMeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新建套餐: {}", setmealDTO);
        setMealService.saveSetzMeal(setmealDTO);

        return Result.success();
    }
}

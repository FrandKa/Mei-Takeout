package com.mei.controller.admin;

import com.mei.dto.DishDTO;
import com.mei.result.Result;
import com.mei.service.DishService;
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
 * @create: 2023-10-23 08:53
 **/
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @ApiOperation("新增菜品接口")
    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);

        dishService.saveWithFlavor(dishDTO);

        return null;
    }
}

package com.mei.controller.user;

import com.mei.constant.CacheConstant;
import com.mei.result.Result;
import com.mei.service.DishService;
import com.mei.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-27 13:11
 **/
@RestController
@RequestMapping("/user/dish")
@Api(tags = "C端查询菜品信息接口")
@Slf4j
public class UserDishController {
    @Autowired
    private DishService dishService;

    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    @Cacheable(value = CacheConstant.DISH_CACHE, key = "#categoryId")
    public Result<List<DishVO>> getDishByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类id查询菜品信息: {}", categoryId);
        List<DishVO> list = dishService.getDishesByCategoryId(categoryId);
        return Result.success(list);
    }
}

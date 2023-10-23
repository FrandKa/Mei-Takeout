package com.mei.controller.admin;

import com.mei.constant.MessageConstant;
import com.mei.dto.DishDTO;
import com.mei.dto.DishPageQueryDTO;
import com.mei.entity.Dish;
import com.mei.result.PageResult;
import com.mei.result.Result;
import com.mei.service.DishService;
import com.mei.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        return Result.success();
    }

    @ApiOperation("菜品的分页查询")
    @GetMapping("/page")
    public Result<PageResult> getPageInf(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品数据");
        PageResult result = dishService.getPageInf(dishPageQueryDTO);

        return Result.success(result);
    }

    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品: {}", ids);
        boolean flag = dishService.delete(ids);
        if(flag) {
            return Result.success();
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);

        return Result.success();
    }

    @ApiOperation("获取菜品信息")
    @GetMapping("/{id}")
    public Result<DishVO> getInfById(@PathVariable Long id) {
        DishVO dishVO = dishService.getInfById(id);
        return Result.success(dishVO);
    }

    @ApiOperation("启用与禁用菜品接口")
    @PostMapping("/status/{status}")
    public Result updateStatus(
            @PathVariable Integer status,
            @RequestParam Long id
    ) {
        dishService.updateStatusById(id, status);

        return Result.success();
    }

    @ApiOperation("根据分类查询菜品接口")
    @GetMapping("/list")
    public Result<List<Dish>> getDishByCategoryId(@RequestParam("categoryId") Long categoryId) {
        List<Dish> dishList = dishService.getDishByCategoryId(categoryId);
        return Result.success(dishList);
    }
}

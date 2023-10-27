package com.mei.controller.admin;

import com.mei.dto.SetmealDTO;
import com.mei.dto.SetmealPageQueryDTO;
import com.mei.result.PageResult;
import com.mei.result.Result;
import com.mei.service.SetMealService;
import com.mei.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation("分页查询套餐信息")
    @GetMapping("/page")
    public Result<PageResult> getPageInf(SetmealPageQueryDTO setmealDTO) {
        log.info("分页查询套餐信息: {}", setmealDTO);
        PageResult page = setMealService.getPage(setmealDTO);

        return Result.success(page);
    }

    @ApiOperation("根据id查套餐信息")
    @GetMapping("/{id}")
    public Result<SetmealVO> getInfById(@PathVariable Long id) {
        log.info("根据id获取套餐信息 id: {}", id);
        SetmealVO setmealVO = setMealService.getInfById(id);

        return Result.success(setmealVO);
    }

    @ApiOperation("修改套餐信息")
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐信息: {}", setmealDTO);
        setMealService.update(setmealDTO);

        return Result.success();
    }

    @ApiOperation("修改套餐状态")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status, @RequestParam Long id) {
        log.info("修改套餐状态: {}", status);
        setMealService.updateStatusById(id, status);

        return Result.success();
    }

    @ApiOperation("批量删除功能")
    @DeleteMapping
    public Result delete(@RequestParam("ids") List<Long> ids) {
        log.info("批量删除套餐: {}", ids);
        setMealService.delete(ids);

        return Result.success();
    }
}

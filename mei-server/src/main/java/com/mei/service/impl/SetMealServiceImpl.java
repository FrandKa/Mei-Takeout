package com.mei.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mei.dto.SetmealDTO;
import com.mei.dto.SetmealPageQueryDTO;
import com.mei.entity.Setmeal;
import com.mei.entity.SetmealDish;
import com.mei.mapper.SetMealDishMapper;
import com.mei.mapper.SetMealMapper;
import com.mei.result.PageResult;
import com.mei.service.SetMealService;
import com.mei.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 16:22
 **/
@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    public void saveSetzMeal(SetmealDTO setmealDTO) {
        // 1. 存储套餐的基本信息:
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.insert(setmeal);
        // 2. 存储套餐中的菜品信息:
        // 修改: 主键回显:
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(Objects.isNull(setmealDishes) || setmealDishes.isEmpty()) {
            return;
        }
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
            setMealDishMapper.insert(setmealDish);
        }
    }

    @Override
    public PageResult getPage(SetmealPageQueryDTO setmealDTO) {
        int pageNum = setmealDTO.getPage();
        int pageSize = setmealDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        Page<SetmealVO> res = setMealMapper.queryList(setmealDTO);
        PageResult pageResult = new PageResult();
        log.info("分页查询套餐信息: {}", res.getResult());
        pageResult.setTotal(res.getTotal());
        pageResult.setRecords(res.getResult());
        return pageResult;
    }
}

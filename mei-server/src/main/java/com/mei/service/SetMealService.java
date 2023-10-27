package com.mei.service;

import com.mei.dto.SetmealDTO;
import com.mei.dto.SetmealPageQueryDTO;
import com.mei.result.PageResult;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 16:22
 **/

public interface SetMealService {
    void saveSetzMeal(SetmealDTO setmealDTO);


    PageResult getPage(SetmealPageQueryDTO setmealDTO);
}

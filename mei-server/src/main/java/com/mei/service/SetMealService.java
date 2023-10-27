package com.mei.service;

import com.mei.dto.SetmealDTO;
import com.mei.dto.SetmealPageQueryDTO;
import com.mei.entity.Setmeal;
import com.mei.result.PageResult;
import com.mei.vo.DishItemVO;
import com.mei.vo.SetmealVO;

import java.util.List;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 16:22
 **/

public interface SetMealService {
    void saveSetzMeal(SetmealDTO setmealDTO);


    PageResult getPage(SetmealPageQueryDTO setmealDTO);

    SetmealVO getInfById(Long id);

    void update(SetmealDTO setmealDTO);

    List<DishItemVO> getDishItemById(Long id);

    List<Setmeal> list(Setmeal setmeal);

    void updateStatusById(Long id, Integer status);

    void delete(List<Long> ids);
}

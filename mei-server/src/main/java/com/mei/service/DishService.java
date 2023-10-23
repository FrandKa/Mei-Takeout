package com.mei.service;

import com.mei.dto.DishDTO;
import com.mei.dto.DishPageQueryDTO;
import com.mei.entity.Dish;
import com.mei.result.PageResult;
import com.mei.vo.DishVO;

import java.util.List;

public interface DishService {

    void saveWithFlavor(DishDTO dishDTO);

    PageResult getPageInf(DishPageQueryDTO dishPageQueryDTO);

    boolean delete(List<Long> list);

    DishVO getInfById(Long id);

    void update(DishDTO dishDTO);
}

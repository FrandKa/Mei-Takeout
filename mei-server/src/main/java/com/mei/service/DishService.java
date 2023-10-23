package com.mei.service;

import com.mei.dto.DishDTO;
import com.mei.dto.DishPageQueryDTO;
import com.mei.result.PageResult;

public interface DishService {

    void saveWithFlavor(DishDTO dishDTO);

    PageResult getPageInf(DishPageQueryDTO dishPageQueryDTO);
}

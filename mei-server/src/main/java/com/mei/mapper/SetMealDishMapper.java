package com.mei.mapper;

import com.mei.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    void insert(SetmealDish setmealDish);

    List<SetmealDish> querySetMealDishBySetMealId(Long id);

    void deleteBySetMealId(Long id);
}

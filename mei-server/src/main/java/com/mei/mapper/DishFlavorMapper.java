package com.mei.mapper;

import com.mei.annotation.AutoFill;
import com.mei.entity.DishFlavor;
import com.mei.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavors);

    @Delete("DELETE FROM mei_take_out.dish_flavor WHERE dish_id = #{dishId}")
    void deleteByDishId(Long dishId);
}

package com.mei.mapper;

import com.github.pagehelper.Page;
import com.mei.annotation.AutoFill;
import com.mei.dto.DishPageQueryDTO;
import com.mei.entity.Dish;
import com.mei.enumeration.OperationType;
import com.mei.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> queryPage(DishPageQueryDTO dishPageQueryDTO);
}

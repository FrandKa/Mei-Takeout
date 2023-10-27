package com.mei.mapper;

import com.github.pagehelper.Page;
import com.mei.annotation.AutoFill;
import com.mei.dto.SetmealDTO;
import com.mei.dto.SetmealPageQueryDTO;
import com.mei.entity.Setmeal;
import com.mei.enumeration.OperationType;
import com.mei.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetMealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> queryList(SetmealPageQueryDTO setmealPageQueryDTO);
}

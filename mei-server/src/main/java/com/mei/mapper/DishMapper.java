package com.mei.mapper;

import com.github.pagehelper.Page;
import com.mei.annotation.AutoFill;
import com.mei.dto.DishPageQueryDTO;
import com.mei.entity.Dish;
import com.mei.enumeration.OperationType;
import com.mei.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

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

    Dish queryById(Long ids);

    @Delete("DELETE FROM mei_take_out.dish WHERE id = #{id}")
    void deleteById(Long id);

    void deleteByIds(List<Long> list);

    @AutoFill(OperationType.UPDATE)
    void updateById(Dish dish);

    @Update("UPDATE mei_take_out.dish SET status = #{status} WHERE id = #{id}")
    void updateStatusById(Long id, Integer status);

    @Select("SELECT * FROM mei_take_out.dish WHERE category_id = #{categoryId}")
    List<Dish> queryByCategoryId(Long categoryId);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}

package com.mei.mapper;

import com.github.pagehelper.Page;
import com.mei.annotation.AutoFill;
import com.mei.dto.SetmealDTO;
import com.mei.dto.SetmealPageQueryDTO;
import com.mei.entity.Setmeal;
import com.mei.enumeration.OperationType;
import com.mei.vo.DishItemVO;
import com.mei.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetMealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from mei_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> queryList(SetmealPageQueryDTO setmealPageQueryDTO);

    Setmeal querySetMealById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    List<Setmeal> list(Setmeal setmeal);

    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from mei_take_out.setmeal_dish sd left join mei_take_out.dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long id);

    @Update("UPDATE mei_take_out.setmeal SET status = #{status} WHERE id = #{id}")
    void updateStatusById(Long id, Integer status);
}

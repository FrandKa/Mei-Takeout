package com.mei.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mei.dto.SetmealDTO;
import com.mei.dto.SetmealPageQueryDTO;
import com.mei.entity.Setmeal;
import com.mei.entity.SetmealDish;
import com.mei.mapper.CategoryMapper;
import com.mei.mapper.SetMealDishMapper;
import com.mei.mapper.SetMealMapper;
import com.mei.result.PageResult;
import com.mei.service.SetMealService;
import com.mei.vo.DishItemVO;
import com.mei.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 16:22
 **/
@Service
@Slf4j
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void saveSetzMeal(SetmealDTO setmealDTO) {
        // 1. 存储套餐的基本信息:
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.insert(setmeal);
        // 2. 存储套餐中的菜品信息:
        // 修改: 主键回显:
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(Objects.isNull(setmealDishes) || setmealDishes.isEmpty()) {
            return;
        }
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
            setMealDishMapper.insert(setmealDish);
        }
    }

    @Override
    public PageResult getPage(SetmealPageQueryDTO setmealDTO) {
        int pageNum = setmealDTO.getPage();
        int pageSize = setmealDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        Page<SetmealVO> res = setMealMapper.queryList(setmealDTO);
        PageResult pageResult = new PageResult();
        log.info("分页查询套餐信息: {}", res.getResult());
        pageResult.setTotal(res.getTotal());
        pageResult.setRecords(res.getResult());
        return pageResult;
    }

    @Override
    public SetmealVO getInfById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setMealMapper.querySetMealById(id);
        Long setMealId = setmeal.getId();
        Long categoryId = setmeal.getCategoryId();
        List<SetmealDish> setMealIdsByDishIds = setMealDishMapper.querySetMealDishBySetMealId(setMealId);
        String categoryName = categoryMapper.queryNameById(categoryId);
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setMealIdsByDishIds);
        setmealVO.setName(categoryName);
        return setmealVO;
    }

    @Override
    public void update(SetmealDTO setmealDTO) {
        Long id = setmealDTO.getId();
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.update(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // TODO KA 2023/10/27 13:46 SQL优化
        if(Objects.isNull(setmealDishes) || setmealDishes.isEmpty()) {
            return;
        }
        setMealDishMapper.deleteBySetMealId(id);
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
            setMealDishMapper.insert(setmealDish);
        }
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setMealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setMealMapper.getDishItemBySetmealId(id);
    }

    @Override
    public void updateStatusById(Long id, Integer status) {
        setMealMapper.updateStatusById(id, status);
    }
}

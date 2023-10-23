package com.mei.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mei.dto.DishDTO;
import com.mei.dto.DishPageQueryDTO;
import com.mei.entity.Dish;
import com.mei.entity.DishFlavor;
import com.mei.mapper.CategoryMapper;
import com.mei.mapper.DishFlavorMapper;
import com.mei.mapper.DishMapper;
import com.mei.result.PageResult;
import com.mei.service.DishService;
import com.mei.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 09:43
 **/
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    /**
     * 新增菜品
     * */
    @Override
    @Transactional // 开启事务: 因为这里涉及多表查询;
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 向菜品表添加一条
        dishMapper.insert(dish);
        Long dishId = dish.getId();

        // 口味添加:
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(Objects.nonNull(flavors) && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult getPageInf(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.queryPage(dishPageQueryDTO);
        log.info("菜品分页查询获得数据: {}", page.getResult());

        return new PageResult(page.getTotal(), page.getResult());
    }
}

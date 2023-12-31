package com.mei.service.impl;

import com.mei.context.BaseContext;
import com.mei.dto.ShoppingCartDTO;
import com.mei.entity.Dish;
import com.mei.entity.Setmeal;
import com.mei.entity.ShoppingCart;
import com.mei.mapper.DishMapper;
import com.mei.mapper.SetMealMapper;
import com.mei.mapper.ShoppingCartMapper;
import com.mei.service.UserShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-27 16:18
 **/
@Service
@Slf4j
public class ShoppingCartServiceImpl implements UserShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void add(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        // 首先需要判断商品是否已经存在了
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if(Objects.isNull(shoppingCartList) || shoppingCartList.isEmpty()) {
            // 不存在商品需要添加:
            Long dishId = shoppingCartDTO.getDishId();
            if(Objects.nonNull(dishId)) {
                // 是一个菜品:
                Dish dish = dishMapper.queryById(dishId);
                // 设置名称:
                shoppingCart.setName(dish.getName());
                // 设置价格:
                shoppingCart.setAmount(dish.getPrice());
                // 设置图片:
                shoppingCart.setImage(dish.getImage());
            } else {
                // 是一个套餐:
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setMealMapper.querySetMealById(setmealId);
                // 设置名称:
                shoppingCart.setName(setmeal.getName());
                // 设置价格:
                shoppingCart.setAmount(setmeal.getPrice());
                // 设置图片:
                shoppingCart.setImage(setmeal.getImage());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            return;
        }
        // 已经存在了: number + 1;
        ShoppingCart first = shoppingCartList.get(0);
        first.setNumber(first.getNumber() + 1);
        shoppingCartMapper.updateNumberById(first);
    }

    @Override
    public List<ShoppingCart> getList() {
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartMapper.queryListByUserId(userId);
        return list;
    }

    @Override
    @Transactional
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);
        // 1. 判断当前商品的数量: 如果只有一件直接删除, 如果有多个number - 1;
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        ShoppingCart data = list.get(0);
        Integer number = data.getNumber();
        if(number > 1) {
            data.setNumber(number - 1);
            shoppingCartMapper.updateNumberById(data);
            return;
        }
        shoppingCartMapper.deleteById(data.getId());
    }
}

package com.mei.service;

import com.mei.dto.ShoppingCartDTO;
import com.mei.entity.ShoppingCart;

import java.util.List;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-27 16:10
 **/

public interface UserShoppingCartService {

    void add(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> getList();

    void cleanShoppingCart();

    void deleteOne(ShoppingCartDTO shoppingCartDTO);
}

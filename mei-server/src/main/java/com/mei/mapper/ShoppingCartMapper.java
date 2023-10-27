package com.mei.mapper;

import com.mei.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart shoppingCart);

    void insert(ShoppingCart shoppingCart);

    @Select("SELECT * FROM mei_take_out.shopping_cart WHERE user_id = #{userId} ;")
    List<ShoppingCart> queryListByUserId(Long userId);

    @Delete("DELETE FROM mei_take_out.shopping_cart WHERE user_id = #{userId}")
    void deleteAllByUserId(Long userId);

    void deleteById(Long id);
}

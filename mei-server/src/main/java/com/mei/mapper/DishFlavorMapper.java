package com.mei.mapper;

import com.mei.annotation.AutoFill;
import com.mei.entity.DishFlavor;
import com.mei.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavors);
}

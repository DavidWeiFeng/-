package com.sky.mapper;

import com.sky.entity.DishFlavor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper

public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根據菜品id刪除口味
     * @param id
     */
    @Delete("delete  from dish_flavor where  dish_id =#{id}")
    void deleteByDishId(Long id);
}

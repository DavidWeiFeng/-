package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    /**
     * 新增菜品和口味
     * @param dishDTO
     */
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    //开启事务注解
    public void saveWithFlavor(DishDTO dishDTO) {
        //TODO 完成新增菜品

        //向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setCreateUser(BaseContext.getCurrentId());
        dish.setUpdateUser(BaseContext.getCurrentId());

        dishMapper.insert(dish);
        //向口味表插入n条数据

        //获取insert语句生成的主键值
        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {dishFlavor.setDishId(id);});
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    @Override
    /**
     * 菜品分页查询
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO>  page= dishMapper.pageQuery(dishPageQueryDTO);
        log.info("查询结果{}",page);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根據菜品主鍵調整菜品狀態
     * @param status
     * @param dishId
     */
    public void startOrStop(Integer status, Long dishId) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(dishId);
        dishMapper.startOrStop(dish);
    }

    /**
     * 菜品批量刪除
     * @param ids
     */
    @Transactional

    public void deleteBatch(List<Long> ids) {
        /**
         * 菜品批量刪除：①查詢菜品是否起售
         *            ②查詢菜品是否包含在套餐中
         *            ③刪除菜品表中的數據
         *
         *            ④刪除菜品關聯的口味
         */

        //①查詢菜品
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus()== StatusConstant.ENABLE)
            {
                //當前菜品正在售賣，不能刪除
                 throw  new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //②查詢菜品是否包含在套餐中
        List<Long> setmealIds = setmealMapper.getSetmealIdsByDishId(ids);
        if(setmealIds!=null &&setmealIds.size()>0)
        {
            //菜品包含在套餐中，不能刪除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //③刪除菜品表中的數據
        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }

    }

    /**
     * 根据id差评菜品并返回口味相关数据
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavors(Long id) {
        DishVO dishVO = new DishVO();
        //查询菜品信息
        Dish dish= dishMapper.getById(id);

        //查询菜品对应口味信息
        List<DishFlavor>  dishFlavors =dishFlavorMapper.getByDishId(id);
        //封装返回
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        //查询菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //删除原有的口味信息
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {dishFlavor.setDishId(dishDTO.getId());});
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }


    }
}

package com.guli.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Alei
 * @create 2019-07-25 15:44
 */
@Component
public class GlobalMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate", new Date(),metaObject);
        this.setFieldValByName("gmtModified", new Date(),metaObject);
        this.setFieldValByName("isDeleted", false ,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}

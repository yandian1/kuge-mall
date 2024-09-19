package com.kuge.mall.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * created by xbxie on 2024/7/8
 */
public class TreeUtils {
    public static <C, E> List<C> genTree(Long pid, List<E> entities, Class<C> clazz, boolean sortable)  {
        List<C> list = new ArrayList<>();

        try {
            for (E entity : entities) {
                Class<?> entityClass = entity.getClass();
                Method getPid = entityClass.getMethod("getPid");
                Method getId = entityClass.getMethod("getId");

                Long entityPid = (Long)getPid.invoke(entity);
                Long entityId = (Long)getId.invoke(entity);
                if (Objects.equals(entityPid, pid)) {
                    C cItem = clazz.getConstructor().newInstance();

                    BeanUtils.copyProperties(entity, cItem);
                    List<C> children = genTree(entityId, entities, clazz, sortable);

                    if (!CollectionUtils.isEmpty(children)) {
                        Method setChildren = clazz.getMethod("setChildren", List.class);
                        setChildren.invoke(cItem, children);
                    }
                    list.add(cItem);
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        if (sortable) {
            list.sort((item1, item2) -> {
                try {
                    Method getSort = clazz.getMethod("getSort");
                    Integer sort1 = (Integer) getSort.invoke(item1);
                    Integer sort2 = (Integer) getSort.invoke(item2);
                    return (sort1 == null ? 1 : sort1) - (sort2 == null ? 1 : sort2);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return 0;
            });
        }

        return list;
    }

    public static <E> List<Long> genPids(Long id, List<E> entities) {
        List<Long> pids = new ArrayList<>();

        try {
            for (E entity : entities) {
                Class<?> entityClass = entity.getClass();
                Method getId = entityClass.getMethod("getId");
                Long entityId = (Long)getId.invoke(entity);
                if (Objects.equals(entityId, id)) {
                    Method getPid = entityClass.getMethod("getPid");
                    Long entityPid = (Long)getPid.invoke(entity);

                    if (entityPid != null) {
                        pids.addAll(genPids(entityPid, entities));
                        pids.add(entityPid);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return pids;
    }

    public static <E> List<Long> genCids(Long pid, List<E> entities) {
        List<Long> list = new ArrayList<>();
        try {
            for (E entity : entities) {
                Class<?> entityClass = entity.getClass();
                Method getPid = entityClass.getMethod("getPid");
                Long entityPid = (Long)getPid.invoke(entity);

                if (Objects.equals(pid, entityPid)) {
                    Method getId = entityClass.getMethod("getId");
                    Long entityId = (Long)getId.invoke(entity);

                    list.add(entityId);
                    list.addAll(genCids(entityId, entities));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}

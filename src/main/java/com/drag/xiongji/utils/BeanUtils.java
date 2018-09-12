package com.drag.xiongji.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public abstract class BeanUtils extends org.springframework.beans.BeanUtils {

	/**
	 * 属性拷贝
	 * @Title: copyProperties 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param source 源对象
	 * @param target 目标对象
	 * @throws BeansException
	 */
	public static void copyProperties(Object source, Object target) throws BeansException {
		copyProperties(source, target, (Collection<String>) null);
	}
	
	/**
	 * 属性拷贝
	 * @Title: copyProperties 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param source 源对象
	 * @param target 目标对象
	 * @param ignoreProperties 需要过滤的属性
	 * @throws BeansException
	 */
	public static void copyProperties(Object source, Object target, String... ignoreProperties) throws BeansException {
		copyProperties(source, target, (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null));
	}
	
	/**
	 * 属性拷贝
	 * @Title: copyProperties 
	 * @author vania
	 * @version 1.0
	 * @see: 
	 * @param source 源对象
	 * @param target 目标对象
	 * @param ignoreProperties 需要过滤的属性
	 * @throws BeansException
	 */
	public static void copyProperties(Object source, Object target, Collection<String> ignoreProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
//		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreProperties == null || !ignoreProperties.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						// 判断value是否为空
						if (value != null) {
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						log.error("【------errorSourcePd-------】:{}",sourcePd.getReadMethod());
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}	
	}
	
	
}

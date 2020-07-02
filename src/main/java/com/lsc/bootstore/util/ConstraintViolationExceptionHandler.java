/**
 * 
 */
package com.lsc.bootstore.util;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * ConstraintViolationException 处理器    持久化层做 bean 验证，验证有问题：
 *比如字段误传、输入格式不正确，就会抛出异常，而异常不止一个（同一时间内有多个异常）
 */
public class ConstraintViolationExceptionHandler {

	/**
	 * 获取批量异常信息
	 * @param e
	 * @return
	 */
	public static String getMessage(ConstraintViolationException e) {
		List<String> msgList = new ArrayList<>();
		for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
			msgList.add(constraintViolation.getMessage());
        }
		String messages = StringUtils.join(msgList.toArray(), ";");
		return messages;
	}

}

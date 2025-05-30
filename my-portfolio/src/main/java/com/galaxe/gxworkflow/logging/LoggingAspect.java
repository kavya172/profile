/*******************************************************************************
 * GalaxE.Healthcare Solutions Inc. ©2019, Confidential and Proprietary - All Rights Reserved.
 * No unauthorized use permitted. The content contained herein may not be reproduced,
 * adapted/modified, published, performed or displayed without the express written 
 * authorization of GalaxE.Healthcare Solutions, Inc..
 ******************************************************************************/
package com.galaxe.gxworkflow.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(0)
public class LoggingAspect {
	private static final Logger LOGGER =LoggerFactory.getLogger(LoggingAspect.class);

	@Around("execution(* com.galaxe.gxworkflow.*.*.*(..))")
	public Object log(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String methodName = null;
		Object[] args = null;
		String methodDetails = null;
		Object returnValue = null;
		String className = null;
		methodName = proceedingJoinPoint.getSignature().getName();
		args = proceedingJoinPoint.getArgs();
		methodDetails = formMethod(methodName, args);
		className = proceedingJoinPoint.getTarget().getClass().toString();
		LOGGER.info("Entered into "+ className+"." + methodDetails);
		returnValue = proceedingJoinPoint.proceed();
		//Class<?> classType = proceedingJoinPoint.getTarget().getClass();
		LOGGER.info("Returned from " + className+"."+methodDetails + " with return value :" + returnValue);
		return returnValue;
	}

	private String formMethod(String methodName, Object[] args) {
		String methodDetails = null;
		methodDetails = methodName + "(";
		for (int i = 0; i < args.length; i++) {
			if (i == 0) {
				methodDetails += args[i];
				continue;
			}
			methodDetails += "," + args[i];
		}
		return methodDetails + ")";
	}
}

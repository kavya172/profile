/*******************************************************************************
 * GalaxE.Healthcare Solutions Inc. ©2019, Confidential and Proprietary - All Rights Reserved.
 * No unauthorized use permitted. The content contained herein may not be reproduced,
 * adapted/modified, published, performed or displayed without the express written 
 * authorization of GalaxE.Healthcare Solutions, Inc..
 ******************************************************************************/
package com.galaxe.gxworkflow.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Order(1)
public class GlobalExceptionAspect{
	private static final Logger LOGGER =LoggerFactory.getLogger(GlobalExceptionAspect.class);

	@AfterThrowing(pointcut="execution(* com.galaxe.gxworkflow.services.*.*(..))",throwing="ex")
	public void exce(JoinPoint jp,Exception ex){
		String methodName = null;
		String className = null;
		className = jp.getTarget().getClass().toString();
		methodName = jp.getSignature().getName();
		LOGGER.error(" method "+ className+"."+ methodName+" is throwing exception with message: ",ex);
		
	}
		
}

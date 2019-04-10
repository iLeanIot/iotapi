package com.leaniot.api.stomp;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;

import com.leaniot.exception.ValueException;

public abstract class AbstractEventSubscriber implements EventSubscriber{
	protected void checkType(Map<String, Object> type) {
		for (Map.Entry<String, Object> entry : type.entrySet()) {
			if(!(entry.getValue() instanceof Class<?>) &&  !(entry.getValue() instanceof ParameterizedTypeReference<?>) )
					throw new ValueException("must be Class<?> or ParameterizedTypeReference<?>");
		}
	}
}
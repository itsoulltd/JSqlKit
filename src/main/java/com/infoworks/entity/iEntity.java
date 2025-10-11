package com.infoworks.entity;

import java.lang.reflect.Field;
import java.util.Map;

public interface iEntity {
    Field[] getDeclaredFields(boolean inherit);
	Map<String, Object> marshalling(boolean inherit);
    void unmarshalling(Map<String, Object> data, boolean inherit);
}

package com.cruder.service.repo;

public interface CruderRepo {
	boolean create(String data);
	String retrieve(String fieldName, String value);
}

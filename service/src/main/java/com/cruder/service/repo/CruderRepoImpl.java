package com.cruder.service.repo;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class CruderRepoImpl implements CruderRepo {
	static final Logger logger = LogManager.getLogger(CruderRepoImpl.class);
	
	Path store;
	
	@PostConstruct
	public void initialize() {
		try {
			Files.deleteIfExists(Paths.get("store.dat"));
			store = Files.createFile(Paths.get("store.dat"));
		} 
		catch(Exception excep) {
			logger.error("Exception happened during file initialization", excep);
		}
	}
	
	private String getDataByIndex(byte[] storeData, int index) {
		int position = 0;
		for (int i = 0; i < index+1 && position < storeData.length; i++) {
			
			byte[] size = ArrayUtils.subarray(storeData, position, position + 4);
			int s = (int)(
		            (0xff & size[0]) << 24  |
		            (0xff & size[1]) << 16  |
		            (0xff & size[2]) << 8   |
		            (0xff & size[3]) << 0
		            );
			position+=4;
			String dataJson = new String(ArrayUtils.subarray(storeData, position, position + s));
			if (index == i) {
				return dataJson;
			}
			position+=s;
		}
		return null;
	}
	
	private String getData(String fieldName, String value) {
		ObjectMapper om = new ObjectMapper();
		try {
			byte[] storeData = Files.readAllBytes(store);
			for (int i = 0; i < 9999; i++) {
				String jsonData = getDataByIndex(storeData, i);
				if (StringUtils.isEmpty(jsonData)) {
					return null;
				}
				try {
					JsonNode jn = om.readTree(jsonData);
					if (jn.has(fieldName)) {
						JsonNode fjn = jn.get(fieldName);
						if (fjn.asText().equals(value)) {
							return jsonData;
						}
					}
					
				} catch (JsonProcessingException excep) {
					logger.error("Invalid json data", excep);
					return null;
				}
			}
			
		} catch (IOException ioExcep) {
			logger.error("Error in reading data file", ioExcep);
			return null;
		}
		return null;
	}
	
	@Override
	public boolean create(String data) {
		byte[] dataBuffer = data.getBytes();
		Header header = new Header(dataBuffer.length);
		try {
			byte[] size = new byte[] {
			        (byte)((header.size >> 24) & 0xff),
			        (byte)((header.size >> 16) & 0xff),
			        (byte)((header.size >> 8) & 0xff),
			        (byte)((header.size >> 0) & 0xff),
			    };
			Files.write(store,  size, StandardOpenOption.APPEND);
			Files.write(store, dataBuffer, StandardOpenOption.APPEND);
			return true;
		} catch (IOException ioExcep) {
			logger.error("Error in writing to data file", ioExcep);
		}
		return false;
	}
	
	@Override
	public String retrieve(String fieldName, String value) {
		String jsonData = getData(fieldName, value);
		return jsonData;
	}

	class Header {
		int size;
		Header(int s) {
			size = s;
		}
	}
}

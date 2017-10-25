package core;

import java.util.LinkedHashMap;
import java.util.List;

public interface IGetData {
	
	public LinkedHashMap<Integer, LinkedHashMap<String, String>> getTestDataMap(String tcClassPkgNm, String tcName);
	public LinkedHashMap<Integer, List<LinkedHashMap<String, String>>> getTestDataMapExtrnlItr(String tcClassPkgNm, String tcName);
	
}

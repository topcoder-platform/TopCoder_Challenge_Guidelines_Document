package wrappers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Data {

	private static Map<Long, HashMap<String, String>> globalTestdata = 
			               new HashMap<Long, HashMap<String, String>>();
	
	private static Map<Long, List<LinkedHashMap<String, String>>> globalTestdataExtItr = 
            new HashMap<Long, List<LinkedHashMap<String, String>>>();
	
	private static LinkedHashMap<Integer, LinkedHashMap<String, String>> allItrTestdata = 
            			   new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
	
	@SuppressWarnings("unused")
	private static void setVal(HashMap<String, String> td){
		globalTestdata.put(Thread.currentThread().getId(), td);
	}
	
	@SuppressWarnings("unused")
	private static void setValExtItr(List<LinkedHashMap<String, String>> td){
		globalTestdataExtItr.put(Thread.currentThread().getId(), td);
	}
	
	@SuppressWarnings("unused")
	private static void setAllItrData(LinkedHashMap<Integer, LinkedHashMap<String, String>> td){
		allItrTestdata = td;	
	}
	
	public static String getVal(String paramName){
		String value = "";
		try{
			if (globalTestdata.get(Thread.currentThread().getId()).containsKey(paramName.toUpperCase().trim())) {
				if (globalTestdata.get(Thread.currentThread().getId()).get(paramName.trim().toUpperCase()).length() > 0)
					value = globalTestdata.get(Thread.currentThread().getId()).get(paramName.trim().toUpperCase());
			} 
		}catch(Exception e){
			// do nothing
		}
		return value.trim();
	}
	
	public static List<LinkedHashMap<String, String>> getDataExtItr(){
		try{
			if (globalTestdataExtItr.get(Thread.currentThread().getId())!=null) {
				return globalTestdataExtItr.get(Thread.currentThread().getId());
			} 
		}catch(Exception e){
			// do nothing
		}
		return null;
	}
	
	public static Map<String,String> getItrDataMap(){
		if(!globalTestdata.isEmpty()){
			return globalTestdata.get(Thread.currentThread().getId());
		}
		return null;
	}
	
	public static LinkedHashMap<Integer, LinkedHashMap<String, String>> getAllItrDataMap() {
		if(!allItrTestdata.isEmpty()){
			return allItrTestdata;
		}
		return null;
	}
	
	
	
}

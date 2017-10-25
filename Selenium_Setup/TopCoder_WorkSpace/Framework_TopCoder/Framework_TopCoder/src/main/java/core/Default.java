package core;

import wrappers.Common;

public class Default {

	private static String GC_XL_EXTN = Common.getGlobalParam("TESTDATA_FORMAT").isEmpty() ? ".xlsx" : 
        							   "."+Common.getGlobalParam("TESTDATA_FORMAT").toLowerCase().trim();
	
	private static String GC_RPT_TYPE = Common.getGlobalParam("REPORTING_TYPE").isEmpty() ?
										"HTML" : Common.getGlobalParam("REPORTING_TYPE").toUpperCase().trim();
	
	public static String getGC_XL_EXTN() {
		return GC_XL_EXTN;
	}
	
	public static String getGC_RPT_TYPE() {
		return GC_RPT_TYPE;
	}
	
}

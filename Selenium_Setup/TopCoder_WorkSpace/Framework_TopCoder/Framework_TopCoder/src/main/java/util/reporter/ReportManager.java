package util.reporter;

import core.Default;

public class ReportManager {

	private static Reporter report  = new Reporter(Default.getGC_RPT_TYPE());
	
	public synchronized static IReporter GetReporter() {	
		return report;
	}
}

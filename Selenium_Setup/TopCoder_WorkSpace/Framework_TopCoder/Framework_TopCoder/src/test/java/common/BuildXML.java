package common;

import util.DriverXMLGenerator;

public class BuildXML {

	public static void main(String[] args) {
		try{
			 new DriverXMLGenerator();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

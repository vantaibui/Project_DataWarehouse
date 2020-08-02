package Test;

import Constrants.Status;
import Model.Log;
import Model.Staging;

public class Test {

	public static void main(String[] args) {
		Log log = new Log();
		log.getLog(Status.ER);
		Staging.loadToStaging(log);

	}

}

/*******************************************************************************
 * GalaxE.Healthcare Solutions Inc. Â©2019, Confidential and Proprietary - All Rights Reserved.
 * No unauthorized use permitted. The content contained herein may not be reproduced,
 * adapted/modified, published, performed or displayed without the express written
 * authorization of GalaxE.Healthcare Solutions, Inc..
 ******************************************************************************/
package com.galaxe.gxworkflow.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.function.Supplier;

public class TimestampUtil {

	public static Timestamp getCurrentTimestamp() {
		Supplier<Timestamp> time =() -> new Timestamp(new Date().getTime());
		return time.get();
	}
}

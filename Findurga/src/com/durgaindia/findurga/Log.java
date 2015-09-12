/* BORQS Software Solutions Pvt Ltd. CONFIDENTIAL
 * Copyright (c) 2012 All rights reserved.
 *
 * The source code contained or described herein and all documents
 * related to the source code ("Material") are owned by BORQS Software
 * Solutions Pvt Ltd. No part of the Material may be used,copied,
 * reproduced, modified, published, uploaded,posted, transmitted,
 * distributed, or disclosed in any way without BORQS Software
 * Solutions Pvt Ltd. prior written permission.
 *
 * No license under any patent, copyright, trade secret or other
 * intellectual property right is granted to or conferred upon you
 * by disclosure or delivery of the Materials, either expressly, by
 * implication, inducement, estoppel or otherwise. Any license
 * under such intellectual property rights must be express and
 * approved by BORQS Software Solutions Pvt Ltd. in writing.
 *
 */
package com.durgaindia.findurga;

public class Log {

	private static final boolean DEBUG_FLAG = true;

	public static void i(String tag, String msg) {
		if (DEBUG_FLAG) {
			android.util.Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG_FLAG) {
			android.util.Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		// Error log should always be displayed.
		android.util.Log.e(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (DEBUG_FLAG) {
			android.util.Log.d(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (DEBUG_FLAG) {
			android.util.Log.v(tag, msg);
		}
	}

}

package com.th3md.chat.server;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
	private static List<Integer> ids;
	private static int index =0;
	private static final int RANGE = 100;
	
	static {
		ids = (List<Integer>) ThreadLocalRandom.current().ints(0, RANGE).distinct().limit(5);
		Collections.shuffle(ids);
	}
	
	public Random() {}
	
	public static int getRandom() {
		if(index > ids.size() - 1) index = 0;
		return ids.get(index++);
	}
	
}

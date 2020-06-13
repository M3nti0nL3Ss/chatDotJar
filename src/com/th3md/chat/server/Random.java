package com.th3md.chat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Random {
	private static List<Integer> ids = new ArrayList<Integer>();
	private static int index =0;
	private static final int RANGE = 100;
	
	static {
		for(int i=0;i<RANGE;i++) ids.add(i);
		Collections.shuffle(ids);
	}
	
	public Random() {}
	
	public static int getRandom() {
		if(index > ids.size() -1 ) index =0;
		return ids.get(index++);
	}
	
}

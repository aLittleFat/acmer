package cn.edu.scau.acm.acmer.util;

import java.util.Collections;
import java.util.List;

public class ScoreUtil {

//	public static void calcScore(List<ContestRecord> records) {
//		Collections.sort(records, (o1, o2) -> {
//            if (o1.getSolved().length() == o2.getSolved().length())
//                return o1.getPenalty() - o2.getPenalty();
//            else
//                return o2.getSolved().length() - o1.getSolved().length();
//        });
//
//		int N = records.size(), n;
//		int[] a = new int[N + 1];
//		for (n = 0; n < N; ++n) a[n] = records.get(n).getSolved().length(); //题数
//		while (n != 0 && a[n - 1] == 0) --n;
//		a[n++] = 0;
//
//		double[] d = new double[N + 1], e = new double[N + 1];
//		for (int i = 0; i < n; ++i)
//			e[i] = d[i] = Math.sqrt(i + 1.0) - 1;
//		int j = 0;
//		for(int i = 0; i < n; ++i){
//	        if (i + 1 < n && a[i] == a[i + 1]) {
//	            d[i + 1] += d[i];
//	        }
//	        else{
//	            int cnt = i - j + 1;
//	            double even = d[i] / cnt;
//	            double left  = 0.0;
//	            for(int t = j; t <= i; ++t){
//	                left = (even - e[t]) / 2;
//	                e[t] += (even - e[t]) / 2;
//	            }
//	            left /= cnt;
//	            for (int t = j; t <= i; ++t) e[t] += left;
//	            j = i + 1;
//			}
//		}
//		for (int i = 0; i < N; ++i)
//			records.get(i).setScore(e[Math.min(i, n - 1)]);
//	}
}

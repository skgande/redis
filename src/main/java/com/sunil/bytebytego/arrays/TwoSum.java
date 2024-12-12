package com.sunil.bytebytego.arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoSum {
    public static void main(String[] args) {
        int[] a = {1, 3, 4, 5, 6, 7};
        int sum = 7;
        List<int[]> result = findPair(a, sum);
        for(int[] pair: result) {
            System.out.println(pair[0] + " " + pair[1]);
        }
    }

    private static List<int[]> findPair(int[] a, int sum) {
        List<int[]> list = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < a.length; i++) {
            if(map.containsKey(a[i])) {
                list.add(new int[]{map.get(a[i]), i});
            } else {
                map.put(sum - a[i], i);
            }
        }
        if(list.isEmpty()) {
            list.add(new int[] {-1, -1});
        }
        return list;
    }
}

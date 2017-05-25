package com.logn.yunupan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OurEDA on 2017/4/15.
 */

public class T {
    public int FirstNotRepeatingChar(String str) {
        if (str.length() == 0) {
            return -1;
        }
        char[] cs = str.toCharArray();
        List<Character> list = new ArrayList<>();
        List<Integer> listCount = new ArrayList<>();
        for (char c : cs) {
            if (!list.contains(c)) {
                list.add(c);
                listCount.add(1);
            } else {
                int p = list.indexOf(c);
                listCount.set(p, listCount.get(p) + 1);
            }
        }
        int p = listCount.indexOf(1);
        if (p < 0) {
            return -1;
        }
        p = str.indexOf(list.get(p));
        return p;
    }

    public int GetUglyNumber_Solution(int index) {
        List<Integer> list = new ArrayList<>();
        int t2 = 0, t3 = 0, t5 = 0;
        if (index <= 0) {
            return 0;
        }
        if (index == 1) {
            return 1;
        }
        list.add(1);
        for (int i = 1; i < index; i++) {
            int s = Math.min(list.get(t2) * 2, list.get(t3) * 3);
            s = Math.min(s, list.get(t5) * 5);
            if (s == list.get(t2) * 2) {
                t2++;
            }
            if (s == list.get(t3) * 3) {
                t3++;
            }
            if (s == list.get(t5) * 5) {
                t5++;
            }
        }
        return list.get(list.size() - 1);
    }

    public class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        ListNode fn = pHead1;
        ListNode sn = pHead2;
        if (pHead1 == null || pHead2 == null) {
            return null;
        }
        while (fn != null && sn != null) {
            if (fn == sn) {
                return fn;
            }
            fn = fn.next;
            sn = sn.next;
        }
        if (fn != null) {
            sn = pHead1;
            while (fn != null) {
                fn = fn.next;
                sn = sn.next;
            }
            fn = pHead2;
        }else if (sn != null) {
            fn = pHead2;
            while (sn != null) {
                fn = fn.next;
                sn = sn.next;
            }
            sn = pHead1;
        }
        while (sn != fn) {
            fn = fn.next;
            sn = sn.next;
        }

        return fn;
    }

}

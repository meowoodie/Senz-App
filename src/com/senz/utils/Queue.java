package com.senz.utils;

import java.util.LinkedList;

/***********************************************************************************************************************
 * @ClassName:   Queue
 * @Author:      Woodie
 * @CreateAt:    Sat, Nov 20, 2014
 * @Description:
 ***********************************************************************************************************************/

public class Queue<E> extends LinkedList<E> {
    private int limit;

    public Queue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }

}
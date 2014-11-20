package com.senz.utils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/***********************************************************************************************************************
 * @ClassName:   Queue
 * @Author:      Woodie
 * @CreateAt:    Sat, Nov 20, 2014
 * @Description:
 ***********************************************************************************************************************/

interface FIFO<T> extends List<T>, Deque<T>, Cloneable, java.io.Serializable {

    // Add a new element to the last of list,
    // If there is already full, then poll an element out.
    T addLastSafe(T addLast);

    // Poll the head of list, if there is not exist, then return null
    T pollSafe();

    // Get the max size of list
    int getMaxSize();

    // Set the max size of list, if the new size is smaller then the number of existing elements, then poll then out
    List<T> setMaxSize(int maxSize);

}

class Queue<T> extends LinkedList<T> implements FIFO<T> {

    private int maxSize = Integer.MAX_VALUE;
    private final Object synObj = new Object();

    public Queue() {
        super();
    }

    public Queue(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public T addLastSafe(T addLast) {
        synchronized (synObj) {
            T head = null;
            while (size() >= maxSize) {
                head = poll();
            }
            addLast(addLast);
            return head;
        }
    }

    @Override
    public T pollSafe() {
        synchronized (synObj) {
            return poll();
        }
    }

    @Override
    public List<T> setMaxSize(int maxSize) {
        List<T> list = null;
        if (maxSize < this.maxSize) {
            list = new ArrayList<T>();
            synchronized (synObj) {
                while (size() > maxSize) {
                    list.add(poll());
                }
            }
        }
        this.maxSize = maxSize;
        return list;
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }
}

package p12.exercise;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    private Map<Q, Queue<T>> map = new HashMap<>();
    
    @Override
    public Set<Q> availableQueues() {
        Set<Q> avQueues = new TreeSet<>();
        for(Q q : map.keySet()) {
            avQueues.add(q);
        }
        return avQueues;
    }

    @Override
    public void openNewQueue(Q queue) {
        if(map.containsKey(queue)) {
            final String msg = queue + "is already available!";
            throw new java.lang.IllegalArgumentException(msg);
        }
        Queue<T> newQueue = new LinkedList<>();
        map.put(queue, newQueue);
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        if(map.containsKey(queue)) {
            if(map.get(queue).isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
        final String msg = queue + "is not available!";
        throw new java.lang.IllegalArgumentException(msg);
    }

    @Override
    public void enqueue(T elem, Q queue) {
        if(!map.containsKey(queue)) {
            final String msg = queue + "is not available!";
            throw new java.lang.IllegalArgumentException(msg);
        }
        map.get(queue).add(elem);
    }

    @Override
    public T dequeue(Q queue) {
        if(!map.containsKey(queue)) {
            final String msg = queue + "is not available!";
            throw new java.lang.IllegalArgumentException(msg);
        }
        return map.get(queue).poll();
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> deqMap = new TreeMap<>();
        for(Q queue : map.keySet()) {
            deqMap.put(queue, map.get(queue).poll());
        }
        return deqMap;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> enqSet = new TreeSet<>();
        for(Q queue : map.keySet()) {
            for(T elem : map.get(queue)) {
                enqSet.add(elem);
            }
        }
        return enqSet;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if(!map.containsKey(queue)) {
            final String msg = queue + "is not available!";
            throw new java.lang.IllegalArgumentException(msg);
        }
        List<T> deqList = new LinkedList<>();
        deqList.addAll(map.get(queue));
        map.get(queue).clear();
        return deqList;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        if(!map.containsKey(queue)) {
            final String msg = queue + "is not available!";
            throw new java.lang.IllegalArgumentException(msg);
        } else if(map.size() == 1) {
            final String msg = queue + "there's no alternative queue for moving elements to";
            throw new java.lang.IllegalStateException(msg);
            
        }
        
        Set<Q> avQueues = availableQueues();
        Iterator<Q> it = avQueues.iterator();
        while(it.hasNext()) {
            if(!it.next().equals(queue)) {
                map.get(it.next()).addAll(dequeueAllFromQueue(queue));
                break;
            }
        }
        map.remove(queue);
    }

}

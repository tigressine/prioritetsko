// Heap Implementation used by Priority Queue
// Written by Ben Faria

package Prioritetsko;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Heap
{
    private AtomicReference<ArrayList<Element>> minHeap;

    public Heap()
    {
        minHeap = new AtomicReference<ArrayList<Element>>(new ArrayList<Element>());
    }

    // Insert Element by priority into minheap
    public boolean insert(Element e)
    {
        ArrayList<Element> currentHeap = minHeap.get();
        ArrayList<Element> updatedHeap = currentHeap;

        // Insert element into currentHeap
        updatedHeap.add(e);
        updatedHeap = percolateUp(updatedHeap, updatedHeap.size() - 1);

        return minHeap.compareAndSet(updatedHeap, updatedHeap);
    }

    // Remove min Element by priority from minheap
    public Element removeMin()
    {
        ArrayList<Element> currentHeap;
        ArrayList<Element> updatedHeap;
        Element min;

        // Continue until successful
        do
        {
            currentHeap = minHeap.get();
            updatedHeap = currentHeap;

            min = updatedHeap.get(0);
            updatedHeap.set(0, updatedHeap.remove(updatedHeap.size()));
            updatedHeap = percolateDown(updatedHeap, 0);
        } while (!minHeap.compareAndSet(currentHeap, updatedHeap));

        return min;
    }

    // Get min Element without removing
    public Element getMin()
    {
        return minHeap.get().get(0);
    }

    // Percolate Element up through minheap
    private ArrayList<Element> percolateUp(ArrayList<Element> heap, int idx)
    {
        int parent = 0;
        
        // Move element up in minheap based on priority level
        if (idx != 0)
        {
            parent = (idx - 1)/2;
            if (heap.get(idx).priority < heap.get(parent).priority)
            {
                heap = swap(heap, parent, idx);
                percolateUp(heap, parent);
            }
        }

        // Make sure that any duplicate priorities are stored in left child
        if (leftOrRight(parent, idx))
        {
            if (heap.get(parent).priority == heap.get(idx).priority)
                heap = swap(heap, idx, idx-1);
        }

        return heap;
    }

    // Percolate Elementdown through minheap
    private ArrayList<Element> percolateDown(ArrayList<Element> heap, int idx)
    {
        Element child = getMinChild(heap, idx);
        int childIdx = heap.indexOf(child);

        while (child.priority < heap.get(idx).priority)
        {
            heap = swap(heap, idx, childIdx);
            idx = childIdx;
            child = getMinChild(heap, idx);
            childIdx = heap.indexOf(child);
        }

        // Make sure that any duplicate priorities are stored in left child
        if (leftOrRight(idx, childIdx))
        {
            if (heap.get(idx).priority == child.priority)
                heap = swap(heap, childIdx, childIdx-1);
        }
        
        return heap;
    }

    // Swap to values in heap
    private ArrayList<Element> swap(ArrayList<Element> heap, int first, int second)
    {
        Element temp = heap.get(first);
        heap.set(first, heap.get(second));
        heap.set(second, temp);

        return heap;
    }

    // Return true if it is a right child or false if it is a left child
    private boolean leftOrRight(int parent, int child)
    {
        // int left = (parent * 2) + 1;
        int right = (parent * 2) + 2;

        if (child == right)
            return true;

        return false;
    }

    // Get the min priority child for parent
    private Element getMinChild(ArrayList<Element> heap, int parent)
    {
        int left = (parent * 2) + 1;
        int right = (parent * 2) + 2;
        
        if (heap.get(left).priority > heap.get(right).priority)
            return heap.get(right);

        return heap.get(left);
    }
}
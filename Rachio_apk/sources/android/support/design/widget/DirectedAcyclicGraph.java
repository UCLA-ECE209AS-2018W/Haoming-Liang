package android.support.design.widget;

import android.support.v4.util.Pools.Pool;
import android.support.v4.util.Pools.SimplePool;
import android.support.v4.util.SimpleArrayMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

final class DirectedAcyclicGraph<T> {
    private final SimpleArrayMap<T, ArrayList<T>> mGraph = new SimpleArrayMap();
    private final Pool<ArrayList<T>> mListPool = new SimplePool(10);
    private final ArrayList<T> mSortResult = new ArrayList();
    private final HashSet<T> mSortTmpMarked = new HashSet();

    DirectedAcyclicGraph() {
    }

    final void addNode(T node) {
        if (!this.mGraph.containsKey(node)) {
            this.mGraph.put(node, null);
        }
    }

    final boolean contains(T node) {
        return this.mGraph.containsKey(node);
    }

    final void addEdge(T node, T incomingEdge) {
        if (this.mGraph.containsKey(node) && this.mGraph.containsKey(incomingEdge)) {
            ArrayList<T> edges = (ArrayList) this.mGraph.get(node);
            if (edges == null) {
                ArrayList<T> arrayList = (ArrayList) this.mListPool.acquire();
                if (arrayList == null) {
                    edges = new ArrayList();
                } else {
                    edges = arrayList;
                }
                this.mGraph.put(node, edges);
            }
            edges.add(incomingEdge);
            return;
        }
        throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
    }

    final List getIncomingEdges(T node) {
        return (List) this.mGraph.get(node);
    }

    final List getOutgoingEdges(T node) {
        ArrayList<T> result = null;
        int size = this.mGraph.size();
        for (int i = 0; i < size; i++) {
            ArrayList<T> edges = (ArrayList) this.mGraph.valueAt(i);
            if (edges != null && edges.contains(node)) {
                if (result == null) {
                    result = new ArrayList();
                }
                result.add(this.mGraph.keyAt(i));
            }
        }
        return result;
    }

    final boolean hasOutgoingEdges(T node) {
        int size = this.mGraph.size();
        for (int i = 0; i < size; i++) {
            ArrayList<T> edges = (ArrayList) this.mGraph.valueAt(i);
            if (edges != null && edges.contains(node)) {
                return true;
            }
        }
        return false;
    }

    final void clear() {
        int size = this.mGraph.size();
        for (int i = 0; i < size; i++) {
            ArrayList<T> edges = (ArrayList) this.mGraph.valueAt(i);
            if (edges != null) {
                edges.clear();
                this.mListPool.release(edges);
            }
        }
        this.mGraph.clear();
    }

    final ArrayList<T> getSortedList() {
        this.mSortResult.clear();
        this.mSortTmpMarked.clear();
        int size = this.mGraph.size();
        for (int i = 0; i < size; i++) {
            dfs(this.mGraph.keyAt(i), this.mSortResult, this.mSortTmpMarked);
        }
        return this.mSortResult;
    }

    private void dfs(T node, ArrayList<T> result, HashSet<T> tmpMarked) {
        if (!result.contains(node)) {
            if (tmpMarked.contains(node)) {
                throw new RuntimeException("This graph contains cyclic dependencies");
            }
            tmpMarked.add(node);
            ArrayList<T> edges = (ArrayList) this.mGraph.get(node);
            if (edges != null) {
                int size = edges.size();
                for (int i = 0; i < size; i++) {
                    dfs(edges.get(i), result, tmpMarked);
                }
            }
            tmpMarked.remove(node);
            result.add(node);
        }
    }
}

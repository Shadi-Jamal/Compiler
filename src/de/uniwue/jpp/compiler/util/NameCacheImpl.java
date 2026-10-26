package de.uniwue.jpp.compiler.util;

import de.uniwue.jpp.compiler.syntax.Expression;

import java.util.HashMap;
import java.util.Map;

public class NameCacheImpl implements NameCache{

    String name;
    int index;
    Map<Integer,String> map;
    Map<String, Integer> stringToIndex;

    public NameCacheImpl() {
        this.map=new HashMap<>();
        this.stringToIndex = new HashMap<>();
        this.index = 0;
    }

    @Override
    public int cacheName(String name) {
        if (stringToIndex.containsKey(name)) {
            return stringToIndex.get(name);
        }
        int currentindex=index;
        map.put(currentindex,name);
        stringToIndex.put(name, currentindex);
        index++;
        return currentindex;

    }

    @Override
    public String getName(int id) {
        return map.get(id);
    }
    public static NameCache create() {
        return new NameCacheImpl();
    }
}

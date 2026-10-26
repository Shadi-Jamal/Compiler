package de.uniwue.jpp.compiler.util;

public interface NameCache {
    public static NameCache create() {
        return new NameCacheImpl();
    }

    public int cacheName(String name);
    public String getName(int id);
}

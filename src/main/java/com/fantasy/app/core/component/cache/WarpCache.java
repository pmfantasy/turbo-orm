package com.fantasy.app.core.component.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

public class WarpCache implements Cache {

  public WarpCache(String cacheName) {
    this.cacheName = cacheName;
  }

  private final ConcurrentMap<String, Object> _cache = new ConcurrentHashMap<String, Object>();
  private String cacheName;

  @Override
  public String getName() {
    return cacheName;
  }

  @Override
  public Object getNativeCache() {
    return _cache;
  }

  @Override
  public ValueWrapper get(Object key) {
    return new SimpleValueWrapper(_cache.get(key));
  }

  @Override
  public void put(Object key, Object value) {
    _cache.put(key.toString(), value);
  }

  @Override
  public void evict(Object key) {
    _cache.remove(key);
  }

  @Override
  public void clear() {
    _cache.clear();
  }

  public Collection<Object> getAll() {
    return _cache.values();
  }

  public Set<String> getAllKeys() {
    return _cache.keySet();
  }

  @Override
  public <T> T get(Object key, Class<T> type) {
    //TODO not supoort
    return null;
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {
    //TODO not supoort
    return null;
  }
  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    //TODO not supoort
    return null;
  }
}

package com.fantasy.app.core.component.compress;

public interface CompressFilter<T> {

	public  boolean exclude(T toCompressObj);
}

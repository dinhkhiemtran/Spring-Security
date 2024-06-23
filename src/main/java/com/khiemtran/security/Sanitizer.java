package com.khiemtran.security;

public interface Sanitizer<T> {
  T sanitize(T t);
}

/*
 * Copyright (c) pakoito 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pacoworks.rxmemoization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.functions.FuncN;

/**
 * Helper class to memoize Functions to enable caching of results for same parameters.
 * <p/>
 * Every function wrapped adds a map check + one if branch for cache hit; or map check + if branch +
 * store value for a cache miss.
 *
 * @author pakoito
 */
public final class RxMemoization {
    private RxMemoization() {
        // No instances
    }

    /**
     * Return a new version of the function that caches results
     * 
     * @param func0 function to wrap
     * @return function caching results
     */
    public static <R> Func0<R> memoize(final Func0<R> func0) {
        final R value = func0.call();
        return new Func0<R>() {
            @Override
            public R call() {
                return value;
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func1 function to wrap
     * @return function caching results
     */
    public static <A, R> Func1<A, R> memoize(final Func1<A, R> func1) {
        final Map<A, R> results = new HashMap<A, R>();
        return new Func1<A, R>() {
            @Override
            public R call(A a) {
                final R cached = results.get(a);
                if (null == cached) {
                    final R result = func1.call(a);
                    results.put(a, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func2 function to wrap
     * @return function caching results
     */
    public static <A, B, R> Func2<A, B, R> memoize(final Func2<A, B, R> func2) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new Func2<A, B, R>() {
            @Override
            public R call(A a, B b) {
                final ArgStorage args = new ArgStorage(a, b);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func2.call(a, b);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func3 function to wrap
     * @return function caching results
     */
    public static <A, B, C, R> Func3<A, B, C, R> memoize(final Func3<A, B, C, R> func3) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new Func3<A, B, C, R>() {
            @Override
            public R call(A a, B b, C c) {
                final ArgStorage args = new ArgStorage(a, b, c);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func3.call(a, b, c);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func4 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, R> Func4<A, B, C, D, R> memoize(final Func4<A, B, C, D, R> func4) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new Func4<A, B, C, D, R>() {
            @Override
            public R call(A a, B b, C c, D d) {
                final ArgStorage args = new ArgStorage(a, b, c, d);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func4.call(a, b, c, d);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func5 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, R> Func5<A, B, C, D, E, R> memoize(
            final Func5<A, B, C, D, E, R> func5) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new Func5<A, B, C, D, E, R>() {
            @Override
            public R call(A a, B b, C c, D d, E e) {
                final ArgStorage args = new ArgStorage(a, b, c, d, e);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func5.call(a, b, c, d, e);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func6 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, F, R> Func6<A, B, C, D, E, F, R> memoize(
            final Func6<A, B, C, D, E, F, R> func6) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new Func6<A, B, C, D, E, F, R>() {
            @Override
            public R call(A a, B b, C c, D d, E e, F f) {
                final ArgStorage args = new ArgStorage(a, b, c, d, e, f);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func6.call(a, b, c, d, e, f);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func7 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, F, G, R> Func7<A, B, C, D, E, F, G, R> memoize(
            final Func7<A, B, C, D, E, F, G, R> func7) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new Func7<A, B, C, D, E, F, G, R>() {
            @Override
            public R call(A a, B b, C c, D d, E e, F f, G g) {
                final ArgStorage args = new ArgStorage(a, b, c, d, e, f, g);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func7.call(a, b, c, d, e, f, g);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func8 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, F, G, H, R> Func8<A, B, C, D, E, F, G, H, R> memoize(
            final Func8<A, B, C, D, E, F, G, H, R> func8) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new Func8<A, B, C, D, E, F, G, H, R>() {
            @Override
            public R call(A a, B b, C c, D d, E e, F f, G g, H h) {
                final ArgStorage args = new ArgStorage(a, b, c, d, e, f, g, h);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func8.call(a, b, c, d, e, f, g, h);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param func9 function to wrap
     * @return function caching results
     */
    public static <A, B, C, D, E, F, G, H, I, R> Func9<A, B, C, D, E, F, G, H, I, R> memoize(
            final Func9<A, B, C, D, E, F, G, H, I, R> func9) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new Func9<A, B, C, D, E, F, G, H, I, R>() {
            @Override
            public R call(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
                final ArgStorage args = new ArgStorage(a, b, c, d, e, f, g, h, i);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = func9.call(a, b, c, d, e, f, g, h, i);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    /**
     * Return a new version of the function that caches results
     *
     * @param funcN function to wrap
     * @return function caching results
     */
    public static <R> FuncN<R> memoize(final FuncN<R> funcN) {
        final Map<ArgStorage, R> results = new HashMap<ArgStorage, R>();
        return new FuncN<R>() {
            @Override
            public R call(Object... objects) {
                ArgStorage args = new ArgStorage(objects);
                final R cached = results.get(args);
                if (null == cached) {
                    final R result = funcN.call(args.storage);
                    results.put(args, result);
                    return result;
                } else {
                    return cached;
                }
            }
        };
    }

    private static final class ArgStorage {
        private final Object[] storage;

        ArgStorage(Object... storage) {
            this.storage = storage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            ArgStorage that = (ArgStorage)o;
            return Arrays.equals(storage, that.storage);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(storage);
        }
    }
}

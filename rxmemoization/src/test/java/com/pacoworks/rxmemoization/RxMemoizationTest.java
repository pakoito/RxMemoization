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

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
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
import rx.schedulers.Schedulers;

public class RxMemoizationTest {
    private static final MyObject INSTANCE = new MyObject();

    private static final List<MyObject> INSTANCES = rx.Observable.range(0, 1000)
            .map(new Func1<Integer, MyObject>() {
                @Override
                public MyObject call(Integer integer) {
                    return new MyObject(integer);
                }
            }).toList().toBlocking().first();

    @Test
    public void testMemoize() {
        final AtomicInteger count = new AtomicInteger(0);
        final Func0<MyObject> memoized = RxMemoization.memoize(new Func0<MyObject>() {
            @Override
            public MyObject call() {
                count.incrementAndGet();
                return INSTANCE;
            }
        });
        /* Extra tests on memoize for func0 to check the double locking implementation is solid */
        final int threadCount = 10000;
        final int maxDelay = 100;
        Iterable<Observable<Long>> observableList = Observable.range(0, threadCount)
                .map(new Func1<Integer, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(final Integer integer) {
                        return Observable
                                .timer((long)(Math.random() * maxDelay), TimeUnit.MILLISECONDS)
                                .doOnNext(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        memoized.call();
                                        memoized.call();
                                        memoized.call();
                                        memoized.call();
                                        memoized.call();
                                    }
                                }).doOnCompleted(new Action0() {
                                    @Override
                                    public void call() {
                                        System.out.println("Test-" + integer);
                                    }
                                }).subscribeOn(Schedulers.newThread());
                    }
                }).toList().toBlocking().first();
        Observable.merge(observableList).toBlocking().forEach(new Action1<Long>() {
            @Override
            public void call(Long o) {
            }
        });
        Assert.assertEquals(1, count.get());
    }

    @Test
    public void testMemoize1() {
        final AtomicInteger count = new AtomicInteger(0);
        Func1<Integer, MyObject> memoized = RxMemoization.memoize(new Func1<Integer, MyObject>() {
            @Override
            public MyObject call(Integer integer) {
                count.incrementAndGet();
                return INSTANCES.get(integer);
            }
        });
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(1));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(30));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(30));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(30));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize2() {
        final AtomicInteger count = new AtomicInteger(0);
        Func2<Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Func2<Integer, Integer, MyObject>() {
                    @Override
                    public MyObject call(Integer integer, Integer integer2) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(15, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(15, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(15, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize3() {
        final AtomicInteger count = new AtomicInteger(0);
        Func3<Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Func3<Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject call(Integer integer, Integer integer2, Integer integer3) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize4() {
        final AtomicInteger count = new AtomicInteger(0);
        Func4<Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Func4<Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject call(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize5() {
        final AtomicInteger count = new AtomicInteger(0);
        Func5<Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Func5<Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject call(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize6() {
        final AtomicInteger count = new AtomicInteger(0);
        Func6<Integer, Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Func6<Integer, Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject call(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5, Integer integer6) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5
                                + integer6);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize7() {
        final AtomicInteger count = new AtomicInteger(0);
        Func7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Func7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject call(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5, Integer integer6, Integer integer7) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5
                                + integer6 + integer7);
                    }
                });
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 0, 15, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(3, count.get());
    }

    @Test
    public void testMemoize8() {
        final AtomicInteger count = new AtomicInteger(0);
        Func8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Func8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject call(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5, Integer integer6, Integer integer7,
                            Integer integer8) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5
                                + integer6 + integer7 + integer8);
                    }
                });
        // +1
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0));
        // +1
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 0, 1));
        // +1
        Assert.assertEquals(INSTANCES.get(45), memoized.call(15, 0, 15, 0, 0, 0, 0, 15));
        // +1
        Assert.assertEquals(INSTANCES.get(45), memoized.call(0, 15, 15, 0, 0, 0, 0, 15));
        // +1
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 0, 15, 0, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(5, count.get());
    }

    @Test
    public void testMemoize9() {
        final AtomicInteger count = new AtomicInteger(0);
        Func9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject> memoized = RxMemoization
                .memoize(new Func9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, MyObject>() {
                    @Override
                    public MyObject call(Integer integer, Integer integer2, Integer integer3,
                            Integer integer4, Integer integer5, Integer integer6, Integer integer7,
                            Integer integer8, Integer integer9) {
                        count.incrementAndGet();
                        return INSTANCES.get(integer + integer2 + integer3 + integer4 + integer5
                                + integer6 + integer7 + integer8 + integer9);
                    }
                });
        // +1
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0));
        // +1
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 1));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 1));
        // +1
        Assert.assertEquals(INSTANCES.get(30), memoized.call(15, 0, 0, 0, 0, 0, 0, 0, 15));
        // +1
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 15, 0, 0, 0, 0, 0, 0, 15));
        // +1
        Assert.assertEquals(INSTANCES.get(30), memoized.call(0, 0, 15, 0, 0, 0, 0, 0, 15));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(5, count.get());
    }

    @Test
    public void testMemoizeN() {
        final AtomicInteger count = new AtomicInteger(0);
        FuncN<MyObject> memoized = RxMemoization.memoize(new FuncN<MyObject>() {
            @Override
            public MyObject call(Object... args) {
                count.incrementAndGet();
                return INSTANCES.get(args.length);
            }
        });
        Assert.assertEquals(INSTANCES.get(0), memoized.call());
        Assert.assertEquals(INSTANCES.get(0), memoized.call());
        Assert.assertEquals(INSTANCES.get(0), memoized.call());
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0));
        Assert.assertEquals(INSTANCES.get(1), memoized.call(0));
        Assert.assertEquals(INSTANCES.get(5), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(5), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(5), memoized.call(0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(12), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(12), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(12), memoized.call(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(INSTANCES.get(0), memoized.call());
        Assert.assertEquals(INSTANCES.get(0), memoized.call());
        Assert.assertEquals(INSTANCES.get(0), memoized.call());
        Assert.assertEquals(4, count.get());
    }

    private static final class MyObject {
        private final int number;

        public MyObject() {
            number = Integer.MIN_VALUE;
        }

        public MyObject(int number) {
            this.number = number;
        }
    }
}

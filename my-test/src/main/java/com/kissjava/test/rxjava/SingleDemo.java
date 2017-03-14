package com.kissjava.test.rxjava;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import org.junit.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Single.OnSubscribe;
import rx.Subscription;
import rx.exceptions.*;
import rx.functions.*;
import rx.observers.*;
import rx.plugins.RxJavaHooks;
import rx.schedulers.*;
import rx.singles.BlockingSingle;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

public class SingleDemo {
	@SuppressWarnings("rawtypes")
	private Func1<Single.OnSubscribe, Single.OnSubscribe> onCreate;

	@SuppressWarnings("rawtypes")
	private Func2<Single, Single.OnSubscribe, Single.OnSubscribe> onStart;

	private Func1<Subscription, Subscription> onReturn;

	@SuppressWarnings("rawtypes")
	@Before
	public void setUp() throws Exception {
		onCreate = spy(new Func1<Single.OnSubscribe, Single.OnSubscribe>() {
			@Override
			public Single.OnSubscribe call(Single.OnSubscribe t) {
				return t;
			}
		});
		RxJavaHooks.setOnSingleCreate(onCreate);

		onStart = spy(new Func2<Single, Single.OnSubscribe, Single.OnSubscribe>() {
			@Override
			public Single.OnSubscribe call(Single t1, Single.OnSubscribe t2) {
				return t2;
			}
		});

		RxJavaHooks.setOnSingleStart(onStart);

		onReturn = spy(new Func1<Subscription, Subscription>() {
			@Override
			public Subscription call(Subscription t) {
				return t;
			}
		});

		RxJavaHooks.setOnSingleReturn(onReturn);
	}

    @After
    public void after() {
        RxJavaHooks.reset();
    }
    @Test
    public void testHelloWorld() {
        TestSubscriber<String> ts = new TestSubscriber<String>();
        Single.just("Hello World!")
        	  .subscribe(ts)
        	  ;
        ts.assertReceivedOnNext(Arrays.asList("Hello World!"));
    }

    @Test
    public void testHelloWorld2() {
        final AtomicReference<String> v = new AtomicReference<String>();
        Single.just("Hello World!").subscribe(new SingleSubscriber<String>() {

            @Override
            public void onSuccess(String value) {
                v.set(value);
            }

            @Override
            public void onError(Throwable error) {

            }

        });
        assertEquals("Hello World!", v.get());
    }
    @Test
    public void testMap() {
        TestSubscriber<String> ts = new TestSubscriber<String>();
        Single.just("A")
                .map(new Func1<String, String>() {

                    @Override
                    public String call(String s) {
                        return s + "B";
                    }

                })
                .subscribe(ts);
        ts.assertReceivedOnNext(Arrays.asList("AB"));
    }

    @Test
    public void zip2Singles() {
        TestSubscriber<String> ts = new TestSubscriber<String>();
        Single<Integer> a = Single.just(1);
        Single<Integer> b = Single.just(2);

        Single.zip(a, b, new Func2<Integer, Integer, String>() {

            @Override
            public String call(Integer a, Integer b) {
                return "" + a + b;
            }

        }).subscribe(ts);

        ts.assertValue("12");
        ts.assertCompleted();
        ts.assertNoErrors();
    }
    
    @Test
    public void zipIterableShouldZipListOfSingles() {
        TestSubscriber<String> ts = new TestSubscriber<String>();
        @SuppressWarnings("unchecked")
        Iterable<Single<Integer>> singles = Arrays.asList(Single.just(1), Single.just(2), Single.just(3));

        Single.zip(singles, new FuncN<String>() {
                    @Override
                    public String call(Object... args) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Object arg : args) {
                            stringBuilder.append(arg);
                        }
                        return stringBuilder.toString();
                    }
                }).subscribe(ts);

        ts.assertValue("123");
        ts.assertNoErrors();
        ts.assertCompleted();
    }
    
    @Test
    public void testCreateSuccess() {
       // TestSubscriber<String> ts = new TestSubscriber<String>();
        Single.create(new OnSubscribe<String>() {

            @Override
            public void call(SingleSubscriber<? super String> s) {
                s.onSuccess("Hello");
            }

        }).subscribe(new Subscriber<String>() {

			@Override
			public void onCompleted() {
				
			}

			@Override
			public void onError(Throwable e) {
				
			}

			@Override
			public void onNext(String t) {
				System.out.println(t);
			}
		});
       // ts.assertReceivedOnNext(Arrays.asList("Hello"));
    }

    @Test
    public void testAsync() {
        TestSubscriber<String> ts = new TestSubscriber<String>();
        Single.just("Hello")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {

                    @Override
                    public String call(String v) {
                        System.out.println("SubscribeOn Thread: " + Thread.currentThread());
                        return v;
                    }

                })
                .observeOn(Schedulers.computation())
                .map(new Func1<String, String>() {

                    @Override
                    public String call(String v) {
                        System.out.println("ObserveOn Thread: " + Thread.currentThread());
                        return v;
                    }

                })
                .subscribe(ts);
        ts.awaitTerminalEvent();
        ts.assertReceivedOnNext(Arrays.asList("Hello"));
    }
}

package com.example.myapplication111;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaTest {

    /**
     * 首先加入依赖
     * implementation 'io.reactivex.rxjava2:rxjava:2.1.16'
     * implementation 'io.reactivex.rxjava2:rxandroid:2.0.2'
     */


    //创建被观察者，其中String可以换成各种封装类
    Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter emitter) throws Exception {
            emitter.onNext("first line");//此处对应上面的String
            emitter.onNext("second line");
            emitter.onNext("third line");
            emitter.onNext("fourth line");
            emitter.onComplete();   //不管是complete还是error，发完之后观察者就不会响应，可以接着发但是没必要
            emitter.onError(new NullPointerException());  //error只能发一次，如果error和complete都有，error写前面才不报错
        }
    });

    Observer<String> observer = new Observer<String>() {

        Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            //该方法最先执行
            Log.d("szd", "onSubscribe " + d);
            disposable = d;
        }

        @Override
        public void onNext(String s) {
            //被观察者动一下，这里就能收到
            Log.d("szd", "onNext " + s);
            if (s.equals("third line")) {
                disposable.dispose();//如果发来一个third line，就不再接收之后的
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.d("szd", "onError " + e);
        }

        @Override
        public void onComplete() {
            Log.d("szd", "onComplete");
        }
    };

    public void shiyong() {
        observable.subscribe(observer);
        //   observable.just("first line","second line","third line").subscribe(observer);
        //使用just就不用一直调用onnext了
    }

    //上面是普通的用法，还有一个用法就是链式调用

    public void lianshi() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onNext(111);//此处对应上面的Integer
                emitter.onNext(222);
                emitter.onNext(333);
                emitter.onNext(444);
                emitter.onComplete();  //可以不写complete
            }
        }).subscribe(new Observer<Integer>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                //该方法最先执行
                Log.d("szd", "onSubscribe " + d);
                disposable = d;
            }

            @Override
            public void onNext(Integer s) {
                //被观察者动一下，这里就能收到
                Log.d("szd", "onNext " + s);
                if (s == 333) {
                    disposable.dispose();//如果发来一个333，就不再接收之后的
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("szd", "onError " + e);
            }

            @Override
            public void onComplete() {
                Log.d("szd", "onComplete ");
            }
        });
    }

    /**
     * 线程切换，使用Schedulers，只要在最后一步subscribe的时候加参数
     */

    public void qiehuan() {
        observable.subscribeOn(Schedulers.newThread())  //表示被观察者提交是在子线程，
//        .subscribeOn(Schedulers.io())         可以多用这个，重用空闲线程
                .observeOn(AndroidSchedulers.mainThread())      //表示观察者在主线程,可以多次调用
                .subscribe();
    }
}

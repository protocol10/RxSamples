package com.protocol10.rx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOnCreate, btnJust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOnCreate = (Button) findViewById(R.id.btn_on_create);
        btnJust = (Button) findViewById(R.id.btn_on_just);
        btnOnCreate.setOnClickListener(this);
        btnJust.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_on_create:
                callOnCreateOperator();
                break;
            case R.id.btn_on_just:
                callOnJustOperator();
                break;
        }
    }

    /**
     * onCreate is a basic operator where we create an observable from scratch. Here we pass
     * observable as a parameter, perform the necessary logic and call onNext if the result is appropriate,
     * and onCompleted and onError must be called only once in the entire lifetime of onbservable.
     */
    private void callOnCreateOperator() {
        Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    //makes a good practice
                    if (!subscriber.isUnsubscribed()) {
                        for (int i = 0; i < 5; i++) {
                            subscriber.onNext(i);
                        }

                        Random random = new Random();
                        subscriber.onNext(random.nextInt());
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
        observable.subscribeOn(AndroidSchedulers.mainThread());
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(MainActivity.class.getName(), "OnCompleted Called");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(MainActivity.class.getName(), "onError Called " + e);
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(MainActivity.class.getName(), "Integer is " + integer);
            }
        };
        observable.subscribe(subscriber);
    }


    /**
     * OnJust is an operator that passes arguments into an observable and returns that arguments that emits them.
     * It can pass upto 9 arguments, and emits those group of object. If pass null as an argument it will return as null.
     * Its the shortest way to create an observable
     */
    private void callOnJustOperator() {
        Observable.just(1, "ABC", 22.34).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Serializable>() {
            @Override
            public void onCompleted() {
                Log.i(MainActivity.class.getName(), "OnCompeted Called for OnJustOperator");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(MainActivity.class.getName(), "onError Called " + e);
            }

            @Override
            public void onNext(Serializable serializable) {
                Log.i(MainActivity.class.getName(), serializable.toString());
            }
        });
    }


}

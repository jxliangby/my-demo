package com.kissjava.test.rxjava;

import rx.Observable;
import rx.functions.Action1;
import rx.observables.BlockingObservable;

public class Demo {
	
	public static void hello(String ...names){
		Observable.from(names).subscribe(new Action1<String>() {
			@Override
			public void call(String s){
				System.out.println("Hello " + s + "!");
			}
		});
		
		//Observable.from(names).subscribe(s->System.out.println(s));
		//Observable.from(names)
	}
	
	public static void main(String[] args) {
		hello("Ben", "Geogre");
	}

}

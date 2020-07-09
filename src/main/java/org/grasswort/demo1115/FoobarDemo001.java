package org.grasswort.demo1115;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xuliangliang
 * @Description Lock & Condition
 * @Date 2020/7/9
 */
public class FoobarDemo001 {
    private int n;
    private final Lock lock = new ReentrantLock();
    private final Condition printFooCondition = lock.newCondition();
    private final Condition printBarCondition = lock.newCondition();
    private volatile boolean startPrintFoo = false;

    public FoobarDemo001(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        while (! startPrintFoo) {};
        lock.lock();
        try {
            for (int i = 0; i < n; i++) {
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                printBarCondition.signal();
                printFooCondition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        lock.lock();
        try {
            this.startPrintFoo = true;
            for (int i = 0; i < n; i++) {
                printBarCondition.await();
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                printFooCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        FoobarDemo001 foobarDemo001 = new FoobarDemo001(10);
        new Thread(() -> {
            try {
                foobarDemo001.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                foobarDemo001.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

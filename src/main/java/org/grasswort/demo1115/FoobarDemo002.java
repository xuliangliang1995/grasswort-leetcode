package org.grasswort.demo1115;

/**
 * @author xuliangliang
 * @Description volatile
 * @Date 2020/7/9
 */
public class FoobarDemo002 {
    private int n;
    private volatile int state = 0;

    public FoobarDemo002(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            if (state == 0) {
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                state = 1;
            } else {
                i--;
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            if (state == 1) {
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                state = 0;
            } else {
                i--;
            }
        }
    }

    public static void main(String[] args) {
        FoobarDemo002 foobarDemo002 = new FoobarDemo002(10);
        new Thread(() -> {
            try {
                foobarDemo002.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                foobarDemo002.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

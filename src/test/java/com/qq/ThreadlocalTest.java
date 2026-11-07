package com.qq;

import org.junit.jupiter.api.Test;


public class ThreadlocalTest {
    @Test
    public void Treadtest() {
        ThreadLocal tl = new ThreadLocal();
        new Thread(() -> {
            tl.set("小小");
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());
        }, "蓝色").start();


        new Thread(() -> {
            tl.set("大大");
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());
        }, "绿色").start();
    }
}



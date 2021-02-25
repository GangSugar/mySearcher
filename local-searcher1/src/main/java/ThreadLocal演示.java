import java.sql.Connection;
import java.text.DateFormat;
import java.util.Random;

class MyThreadLocal extends ThreadLocal<Random> {
    @Override
    protected Random initialValue() {
        return new Random();
    }
}
//就是每一个对象都有自己的线程
public class ThreadLocal演示 {
    // 线程 + 本地
    // 实现每个线程有自己的对象
    public static void main(String[] args) {
        // 以下对象都不是线程安全的
        Random random = new Random();
        Connection connection = null;
        DateFormat dateFormat = null;

        // 每个线程必须有自己的独立的对象
        // 办法1：每次需要的时候创建对象，用完销毁。使用局部变量的形式
        // 线程安全了，遗憾：如果一个线程中多次用到该对象
        // 需要多次创建+多次销毁
        // 办法2：为每个线程各自准备一个全局的对象
        // 整体看，有很多对象
        // 每个线程看，只有一个对象

        ThreadLocal<Random> tl1 = new ThreadLocal<>();

        // 虽然每个线程中调用的是同一个方法，但实际上每个线程中拿到的是不同的对象
        Random r = tl1.get();

        // 虽然每个线程中调用的是同一个方法，但实际上设置到每个线程中了
        tl1.set(new Random());

        // 可以通过继承 ThreadLocal 类，并且重写其 initialValue 方法
        // 设置默认值
        //ThreadLocal相当于维护一个全局的map
        ThreadLocal<Random> tl2 = new MyThreadLocal();
        tl2.get();//这样每个线程都可以去拿，这样就把获取到的对象设置奥每一个对象中去了

        ThreadLocal<Random> tl3 = new ThreadLocal<Random>() {
            @Override
            protected Random initialValue() {
                return new Random();
            }
        };
        Random r1 = tl3.get();
        Random r2 = tl3.get();
        System.out.println(r1 == r2);//拿到的是同一个对象

        ThreadLocal<Random> tl4 = ThreadLocal.withInitial(() -> new Random());

        ThreadLocal<Random> tl5 = ThreadLocal.withInitial(Random::new);
    }
}

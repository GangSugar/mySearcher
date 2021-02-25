import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class 流式写法演示 {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("W.exe", "W.jpg", "W.png", "a.png", "a.hello", "a.txt", "a.exe", "z.exe", "zb.exe");
        List<String> resultList = new ArrayList<>();
        for (String item : list) {
            if (item.length() > 5) {//如果长度超出，我就不处理，
                continue;
            }

            if (!Character.isLowerCase(item.charAt(0))) {
                continue;
            }

            if (!item.endsWith(".exe")) {//如果结尾不是以exe结尾的就不处理
                continue;
            }

            String name = item.substring(0, item.length() - 4);

            String result = "Hello:" + name;

            resultList.add(result);
        }
        System.out.println(resultList);//打印一下

        // Hello:a   Hello:z(这部分就是流式写法，和上面的逻辑是一样的)
        List<String> collect = list.stream()
                .filter(item -> item.length() <= 5)
                .filter(item -> Character.isLowerCase(item.charAt(0)))
                .filter(item -> item.endsWith(".exe"))
                .map(item -> item.substring(0, item.length() - 4))
                .map(item -> "Hello:" + item)
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    public static void main2(String[] args) {
        List<String> list = Arrays.asList("Hello", "World", "你好", "世界");

        // 针对 list 中的每一个元素，获取字符串长度，并且保存到一个新的 List<Integer> 中
        List<Integer> result = new ArrayList<>();
        for (String item : list) {
            int length = item.length();
            result.add(length);
        }

        // 流式写法 —— 默认针对容器处理
        // 容器中的每个元素都要经过一个固定的操作 —— 我们传入
        // 针对结果 collect
        Stream<String> stream = list.stream();
        // map：每个元素都要经过一个接口操作(Function中参数是String类型的，结果是Integer类型的)
//        Stream<Integer> integerStream = stream.map(new Function<String, Integer>() {
//            @Override
//            public Integer apply(String item) {
//                return item.length();
//            }
//        });

        // 从匿名类到 lambda 表达式
        // Stream<Integer> integerStream = stream.map(item -> item.length());

        // 提换为 Method Reference
        Stream<Integer> integerStream = stream.map(String::length);
        List<Integer> collect = integerStream.collect(Collectors.toList());

        // 变成链式写法(collect是用来收集的)
        List<Integer> collect1 = list.stream().map(String::length).collect(Collectors.toList());
    }
}

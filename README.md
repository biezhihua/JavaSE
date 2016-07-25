# JavaSE

## 基本类型

byte    1字节     Byte
short   2字节     Short
int     4字节     Integer
long    8字节     Long
float   4字节     Float
double  8字节     Double
boolean 1字节     Boolean

## switch()中能否使用string作为参数

JavaSE5之前：byte,short,int,char
JavaSE5时：增加enum类型
JavaSE7时：增加string

目前始终不支持的long

## Java的四中引用

- 强引用（Strong Reference） 最常用的引用类型，如Object obj = new Object(); 。只要强引用存在则GC时则必定不被回收。
- 软引用（Soft Reference） 用于描述还有用但非必须的对象，当堆将发生OOM（Out Of Memory）时则会回收软引用所指向的内存空间，若回收后依然空间不足才会抛出OOM。一般用于实现内存敏感的高速缓存。 当真正对象被标记finalizable以及的finalize()方法调用之后并且内存已经清理, 那么如果SoftReference object还存在就被加入到它的 ReferenceQueue.只有前面几步完成后,Soft Reference和Weak Reference的get方法才会返回null
- 弱引用（Weak Reference） 发生GC时必定回收弱引用指向的内存空间。 和软引用加入队列的时机相同.
- 虚引用（Phantom Reference) 又称为幽灵引用或幻影引用，虚引用既不会影响对象的生命周期，也无法通过虚引用来获取对象实例，仅用于在发生GC时接收一个系统通知。 当一个对象的finalize方法已经被调用了之后，这个对象的幽灵引用会被加入到队列中。通过检查该队列里面的内容就知道一个对象是不是已经准备要被回收了. 虚引用和软引用和弱引用都不同,它会在内存没有清理的时候被加入引用队列.虚引用的建立必须要传入引用队列,其他可以没有

错误的使用方式，常常使用Handler时会报警告，为了不让内部类隐式引用外部类对象而把Handler变成静态的，而一般来说其内部我们都会使用到Context对象来引用上下文，同时为了不内存泄露，常常会把Context变成弱引用（按照网上的例子），这个时候如果我们利用Handler的延迟效果来做一些事情，当GC发生是就会出现一些意外的情况，因为Context在GC发生是被释放掉了。

## == 和 equals()

- ==侧重于比较原生类型（byte/short/int/long..），而equals()侧重于检查对象的相等性。
- ==是操作符，而equals()是方法
- ==如果用于比较对象，比较的是内存地址，而equals()分为两种情况，如果没有重写Object的equals()方法，则是比较内存地址，如果重写了则是按照具体的业务逻辑来比较，例如String就是比较实际的内容。

正确的equals()方法必须满足以下5个条件:

1. 自反性.对任意x,x.equals(x)一定返回true.
2. 对称性.对任意x/y,如果y.equals(x)返回true,则x.equals(y)也返回true
3. 传递性.对任意x/y/z,如果有x.equals(y)返回true,y.equals(z)返回true,则x.equals(z)一定返回true.
4. 一致性.对任意x/y,如果对象中用于等价比较的信息没有改变,那么无论如何调用x.equals(y)多少次,返回的结果应该保持一致,那么一直是true,要么一直是false
5. 对任何不是null的x,x.equals(null)一定返回false.

## HashCode（散列码）的作用

说到HashCode就必须提到Java中的集合。
例如Map，对于集合操作一般分为存和取，因为Map的key不保存重复的元素，所以加入Map中的元素必须定义equals()方法，以确定对象的唯一性（是否相等）。

在取的情况下，如果遍历去拿指定的key，无意很耗费效率。而HashCode()就是为了速度而生。
例如HashMap，就必须同时定义equals()和hashCode()方法。

hashCode称为散列码，生成散列码的函数叫做散列函数，而一般使用散列码的原因在于想使用一个对象去查找另一个对象（HashMap）。线性查询的瓶颈在于查询速度。

散列将键的信息保存在某处,以便能快速找到(数组是存储元素最快的数据结构).通过键对象生成一个数字,将其作为数组的下标,这个数字就是散列码,由散列函数(Object.hashCode())生成.

为了解决数组容量被固定的问题,不同的键可以产生相同的下标.也就是说可能会有冲突,因此数组多大也就不重要了,任何键都能在数组中找到他的位置.

查询一个值的过程首先就是计算散列码,然后使用散列码查询数组,如果能够保证没有冲突(如果值的数量是固定的,那么就有可能冲突),那么就有了一个完美的散列函数.

通常,冲突由外部链接处理:数组并不直接保存值,而是保存值的list.然后对list中的值使用equals()方法进行线性的查询.

如果散列函数比较好,数组的每个list就会有比较少的值.

因此不是查询整个list,而是快速的跳到数组的某个位置,只对很少的元素进行比较.这就是HashMap会如此快的原因.

由于散列表中的"槽位 slot"通常称为捅位(bucket),为了使散列均匀分布,桶的数量通常使用2的整数次方,因为除法和求余是最慢的操作,可以用掩码来替代.

怎么重写hasCode()函数:

1. 给int变量result赋予某个非零值常量
2. 为对象内有意义的域(即每个可以做equals()的域)计算出一个int散列码c
4. 合并计算的得到的散列码 result = 31 * result + c;
4. 返回result

|域类型|计算|
| --- | --- |
| boolean               | c = f ? 0 : 1|
| byte/char/short/int   | c = (int)f;  |
| long                  | c = (int)(f ^ f>>>32)  |
| float                 | c = Float.floatToInBits(f);  |
| double                | long l = Double.doubleToLongBits(f); c = (int)(l ^ (l>>>32));  |
| Object                | c = f.hashCode();  |
| 数组                  |  对每个元素应用应用以上规则  |


```java
public int hashCode() {
    int h = hash;
    if (h == 0 && value.length > 0) {
        char val[] = value;

        for (int i = 0; i < value.length; i++) {
            h = 31 * h + val[i];
        }
        hash = h;
    }
    return h;
}
```

计者选择 31 这个值是因为它是一个奇质数。如果它是一个偶数，在使用乘法当中产生数值溢出时，原有数字的信息将会丢失，因为乘以二相当于位移。
选择质数的优势不是那么清晰，但是这是一个传统。31 的一个优良的性质是：乘法可以被位移和减法替代：

31 * i == (i << 5) - i

现代的 VM 可以自行完成这个优化。

可以手工调整HashMap来提高性能: 

1. 容量：桶位数 
2. 初始容量：在创建是所拥有的捅位数．HashMap和HashSet都允许设置． 
3. 尺寸：表中当前存储的项数． 
4. 负载因子：尺寸／容量．空表的负载因子是0,而半满的负载因子是0.5.负载轻的表,出产生冲突的可能性小,对于插入和查找都是最理想的. 
HashMap和HashSet都允许指定负载因子,当达到负载因子的水平时,容器将自动增加其容量(桶位数),实现方式是容量大致加倍,并重新将现有的对象分部到新的捅位中,称之为再散列. 
A` HashMap使用的负载因子是0.75(只有当表达到四分之三时,才进行散列),这个因子在时间和空间代价中达到了平衡,更高的负载因子可以降低表所需的空间,但是会增加查找的代价.

HashMap不是线程安全的，在被多线程共享操作时，会有问题，什么问题呢？ 
主要是多线程同时put时，如果同时触发了再散列操作，会导致HashMap中的链表中出现循环节点，进一步是的后面get的时候，会死循环。

线程不安全的HashMap
因为多线程环境下，使用Hashmap进行put操作会引起死循环，导致CPU利用率接近100%，所以在并发情况下不能使用HashMap。

效率低下的HashTable容器
 HashTable容器使用synchronized来保证线程安全，但在线程竞争激烈的情况下HashTable的效率非常低下。
 因为当一个线程访问HashTable的同步方法时，其他线程访问HashTable的同步方法时，可能会进入阻塞或轮询状态。
 如线程1使用put进行添加元素，线程2不但不能使用put方法添加元素，并且也不能使用get方法来获取元素，所以竞争越激烈效率越低。

JDK1.6
ConcurrentHashMap的锁分段技术
HashTable容器在竞争激烈的并发环境下表现出效率低下的原因，是因为所有访问HashTable的线程都必须竞争同一把锁，那假如容器里有多把锁，每一把锁用于锁容器其中一部分数据，
那么当多线程访问容器里不同数据段的数据时，线程间就不会存在锁竞争，从而可以有效的提高并发访问效率，这就是ConcurrentHashMap所使用的锁分段技术，
首先将数据分成一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问。
     
http://ifeve.com/concurrenthashmap/
     
在JDK1.6中，HashMap采用Node数组+链表实现，即使用链表处理冲突，同一hash值的链表都存储在一个链表里。
但是当位于一个桶中的元素较多，即hash值相等的元素较多时，通过key值依次查找的效率较低。
而JDK1.8中，HashMap采用Node数组+链表+红黑树实现，当链表长度超过阈值（8）时，将链表转换为红黑树，这样大大减少了查找时间。

## HashMap工作原理及实现


## String/StringBuilder/StringBuffer

String是不可变类型的。
StringBuilder是线程不安全的可变类型。
StringBuffer是线程安全的可变类型。

在使用StringBuilder和StringBuffer的时候是有陷阱的，如果在append("a"+"b")这样使用就掉入了陷阱，因为"a+"b"会生成新的字符串。

## try catch finally，try里有return，finally还执行么？

会执行，在方法 返回调用者前执行。Java允许在finally中改变返回值的做法是不好的，
因为如果存在finally代码块，try中的return语句不会立马返回调用者，而是记录下返回值，待finally代码块执行完毕之后再向调用者返回其值，
然后如果在finally中修改了返回值，这会对程序造成很大的困扰。

结论：
1.return语句并不是函数的最终出口，如果有finally语句，这在return之后还会执行finally（return的值会暂存在栈中，等待finally执行后再返回）
2.finally里面是不建议放return语句的，根据需要，return语句可以放在try和catch里面和函数的最后。可行的做法有四个：
    1. return语句只在函数最后出现一次
    2. return语句仅在try和catch里面都出现
    3. return语句仅在try和函数的最后都出现
    4. return语句仅在catch和函数的最后都出现

## Excption与Error区别

Error表示系统级的错误和程序不必处理的异常，是恢复不是不可能但很困难的情况下的一种严重问题；比如内存溢出，不可能指望程序能处理这样的状况；Exception表示需要捕捉或者需要程序进行处理的异常，是一种设计或实现问题；也就是说，它表示如果程序运行正常，从不会发生的情况。

## Java1.7和1.8的新特性


## Java CopyOnWriteArrayList工作原理及实现
并发优化的ArrayList。用CopyOnWrite策略，在修改时先复制一个快照来修改，改完再让内部指针指向新数组。
因为对快照的修改对读操作来说不可见，所以只有写锁没有读锁，加上复制的昂贵成本，典型的适合读多写少的场景。如果更新频率较高，或数组较大时，还是Collections.synchronizedList(list)，对所有操作用同一把锁来保证线程安全更好。
增加了addIfAbsent(e)方法，会遍历数组来检查元素是否已存在，性能可想像的不会太好。
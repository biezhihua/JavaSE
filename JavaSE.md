## 基本类型

| 基本类型 | 字节 | 包装类型 |
| ---- | ---- | ---- |
|byte |1字节| Byte |
|short  | 2字节  |   Short|
|int   |  4字节   |  Integer|
|long  |  8字节    | Long|
|float |  4字节   |  Float|
|double | 8字节    | Double|
|boolean |1字节    | Boolean|
|char |1字节    | Character|

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

区别:
1. ==侧重于比较原生类型（byte/short/int/long..），而equals()侧重于检查对象的相等性。
2. ==是操作符，而equals()是方法
3. ==如果用于比较对象，比较的是内存地址，而equals()分为两种情况，如果没有重写Object的equals()方法，则是比较内存地址，如果重写了则是按照具体的业务逻辑来比较，例如String就是比较实际的内容。

正确的equals()方法必须满足以下5个条件:
1. 自反性.对任意x,x.equals(x)一定返回true.
2. 对称性.对任意x/y,如果y.equals(x)返回true,则x.equals(y)也返回true
3. 传递性.对任意x/y/z,如果有x.equals(y)返回true,y.equals(z)返回true,则x.equals(z)一定返回true.
4. 一致性.对任意x/y,如果对象中用于等价比较的信息没有改变,那么无论如何调用x.equals(y)多少次,返回的结果应该保持一致,那么一直是true,要么一直是false
5. 对任何不是null的x,x.equals(null)一定返回false.

## HashCode（散列码）的作用
说到HashCode就必须提到Java中的集合。例如Map，对于集合操作一般分为存和取，因为Map的key不保存重复的元素，所以加入Map中的元素必须定义equals()方法，以确定对象的唯一性（是否相等）。

在取的情况下，如果遍历去拿指定的key，无意很耗费效率。而HashCode()就是为了速度而生。例如HashMap，就必须同时定义equals()和hashCode()方法。

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

String类的hashCode如下:
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

设计者选择 31 这个值是因为它是一个奇质数。如果它是一个偶数，在使用乘法当中产生数值溢出时，原有数字的信息将会丢失，因为乘以二相当于位移。
选择质数的优势不是那么清晰，但是这是一个传统。31 的一个优良的性质是：乘法可以被位移和减法替代：

```java
31 * i == (i << 5) - i
```

现代的 VM 可以自行完成这个优化。

此外,可以手工调整HashMap来提高性能: 
1. 容量：桶位数 
2. 初始容量：在创建是所拥有的捅位数．HashMap和HashSet都允许设置． 
3. 尺寸：表中当前存储的项数． 
4. 负载因子：尺寸／容量．空表的负载因子是0,而半满的负载因子是0.5.负载轻的表,出产生冲突的可能性小,对于插入和查找都是最理想的. 
HashMap和HashSet都允许指定负载因子,当达到负载因子的水平时,容器将自动增加其容量(桶位数),实现方式是容量大致加倍,并重新将现有的对象分部到新的捅位中,称之为再散列. 
A` HashMap使用的负载因子是0.75(只有当表达到四分之三时,才进行散列),这个因子在时间和空间代价中达到了平衡,更高的负载因子可以降低表所需的空间,但是会增加查找的代价.

HashMap不是线程安全的，在被多线程共享操作时，会有问题，什么问题呢: 
主要是多线程同时put时，如果同时触发了再散列操作，会导致HashMap中的链表中出现循环节点，进一步是的后面get的时候，会死循环。

线程不安全的HashMap:
因为多线程环境下，使用HashMap进行put操作会引起死循环，导致CPU利用率接近100%，所以在并发情况下不能使用HashMap。

效率低下的HashTable容器:
HashTable容器使用synchronized来保证线程安全，但在线程竞争激烈的情况下HashTable的效率非常低下。
因为当一个线程访问HashTable的同步方法时，其他线程访问HashTable的同步方法时，可能会进入阻塞或轮询状态。
如线程1使用put进行添加元素，线程2不但不能使用put方法添加元素，并且也不能使用get方法来获取元素，所以竞争越激烈效率越低。

ConcurrentHashMap的锁分段技术:
HashTable容器在竞争激烈的并发环境下表现出效率低下的原因，是因为所有访问HashTable的线程都必须竞争同一把锁，那假如容器里有多把锁，每一把锁用于锁容器其中一部分数据，
那么当多线程访问容器里不同数据段的数据时，线程间就不会存在锁竞争，从而可以有效的提高并发访问效率，这就是ConcurrentHashMap所使用的锁分段技术，
首先将数据分成一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问。
     
HashMap的基本概念:
在JDK1.6中，HashMap采用Node数组+链表实现，即使用链表处理冲突，同一hash值的链表都存储在一个链表里。
但是当位于一个桶中的元素较多，即hash值相等的元素较多时，通过key值依次查找的效率较低。
而JDK1.8中，HashMap采用Node数组+链表+红黑树实现，当链表长度超过阈值（8）时，将链表转换为红黑树，这样大大减少了查找时间。

## String/StringBuilder/StringBuffer

String是不可变类型的。
StringBuilder是线程不安全的可变类型。
StringBuffer是线程安全的可变类型。
在使用StringBuilder和StringBuffer的时候是有陷阱的，如果在append("a"+"b")这样使用就掉入了陷阱，因为"a+"b"会生成新的字符串。

## try catch finally，try里有return，finally还执行么？

会执行，在方法 返回调用者前执行。Java允许在finally中改变返回值的做法是不好的，
因为如果存在finally代码块，try中的return语句不会立马返回调用者，而是记录下返回值，待finally代码块执行完毕之后再向调用者返回其值，
然后如果在finally中修改了返回值，这会对程序造成很大的困扰。

## Exception与Error区别

Error表示系统级的错误和程序不必处理的异常，是恢复不是不可能但很困难的情况下的一种严重问题；比如内存溢出，不可能指望程序能处理这样的状况；Exception表示需要捕捉或者需要程序进行处理的异常，是一种设计或实现问题；也就是说，它表示如果程序运行正常，从不会发生的情况。

## JVM区域划分
JVM所管理的内存分为以下几个运行时数据区：程序计数器、Java虚拟机栈、本地方法栈、Java堆、方法区。

Java堆:
Java Heap是Java虚拟机所管理的内存中最大的一块，它是所有线程共享的一块内存区域。几乎所有的对象实例和数组都在这类分配内存。Java Heap是垃圾收集器管理的主要区域，因此很多时候也被称为“GC堆”。

方法区:
是各个线程共享的内存区域，它用于存储已经被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。方法区域又被称为“永久代”.

Java堆中各代分部 yong(年轻代)  old(老年代) permanent(方法区):
1. Young：主要是用来存放新生的对象。
2. Old：主要存放应用程序中生命周期长的内存对象。
3. Permanent：是指内存的永久保存区域，主要存放Class和Meta的信息,Class在被 Load的时候被放入PermGen space区域. 它和和存放Instance的Heap区域不同,GC(Garbage Collection)不会在主程序运行期对PermGen space进行清理，所以如果你的APP会LOAD很多CLASS的话,就很可能出现PermGen space错误。

##  双亲委派模型：Bootstrap ClassLoader、Extension ClassLoader、ApplicationClassLoader。

双亲委派模型:
1. 启动类加载器，负责将存放在\lib目录中的，或者被-Xbootclasspath参数所指定的路径中，并且是虚拟机识别的（仅按照文件名识别，如rt.jar，名字不符合的类库即时放在lib目录中也不会被加载）类库加载到虚拟机内存中。启动类加载器无法被java程序直接引用。
2. 扩展类加载器：负责加载\lib\ext目录中的，或者被java.ext.dirs系统变量所指定的路径中的所有类库，开发者可以直接使用该类加载器。
3. 应用程序类加载器：负责加载用户路径上所指定的类库，开发者可以直接使用这个类加载器，也是默认的类加载器。 三种加载器的关系：启动类加载器->扩展类加载器->应用程序类加载器->自定义类加载器。

这种关系即为类加载器的双亲委派模型。其要求除启动类加载器外，其余的类加载器都应当有自己的父类加载器。这里类加载器之间的父子关系一般不以继承关系实现，而是用组合的方式来复用父类的代码。

双亲委派模型的工作过程：如果一个类加载器接收到了类加载的请求，它首先把这个请求委托给他的父类加载器去完成，每个层次的类加载器都是如此，因此所有的加载请求都应该传送到顶层的启动类加载器中，只有当父加载器反馈自己无法完成这个加载请求（它在搜索范围中没有找到所需的类）时，子加载器才会尝试自己去加载。

好处：java类随着它的类加载器一起具备了一种带有优先级的层次关系。例如类java.lang.Object，它存放在rt.jar中，无论哪个类加载器要加载这个类，最终都会委派给启动类加载器进行加载，因此Object类在程序的各种类加载器环境中都是同一个类。相反，如果用户自己写了一个名为java.lang.Object的类，并放在程序的Classpath中，那系统中将会出现多个不同的Object类，java类型体系中最基础的行为也无法保证，应用程序也会变得一片混乱。

实现：在java.lang.ClassLoader的loadClass()方法中，先检查是否已经被加载过，若没有加载则调用父类加载器的loadClass()方法，若父加载器为空则默认使用启动类加载器作为父加载器。如果父加载失败，则抛出ClassNotFoundException异常后，再调用自己的findClass()方法进行加载。

## GC

CG:
1. 标记－清扫，所依据的思路是从堆栈和静态存储区出发，遍历所有的引用，进而找出所有存活的对象．每当它找到一个存活对象，就会给对象设一个标记，
这个过程中不会回收任何对象．只有标记工作完成的时候，清理动作才会开始．在清理的过程中，没有标记的对象将会被释放，不会发生任何复制动作．
所以剩下的堆空间是不连续的，垃圾回收器要是希望得到连续的空间的话，就的重新整理剩下的对象．
2. 停止－复制，的意思是这种垃圾回收动作不是在后台进行的，相反，垃圾回收动作发生的同时，程序将会被暂停．停止－复制要求在释放旧有对象之前，
必须先把所有存活的对象从旧堆复制到新堆，这将导致大量的复制行为．
3. Java虚拟机会进行监视，如果所有对象都很稳定，垃圾回收期的效率低的话，会切换到标记－清扫方式，根据效果，要是堆空间出现很多碎片，就会切换到停止－复制．

# 集合

Java集合工具包位于Java.util包下，包含了很多常用的数据结构，如数组、链表、栈、队列、集合、哈希表等。

学习Java集合框架下大致可以分为如下五个部分：List列表、Set集合、Map映射、迭代器（Iterator、Enumeration）、工具类（Arrays、Collections）。

Collection是List、Set等集合高度抽象出来的接口，它包含了这些集合的基本操作，它主要又分为两大部分：List和Set。

List接口通常表示一个列表（数组、队列、链表、栈等），其中的元素可以重复，常用实现类为ArrayList和LinkedList，另外还有不常用的Vector。另外，LinkedList还是实现了Deque(Deque继承自Queue)接口，因此也可以作为队列使用。

Set接口通常表示一个集合，其中的元素不允许重复（通过hashcode和equals函数保证），常用实现类有HashSet和TreeSet，HashSet是通过Map中的HashMap实现的，而TreeSet是通过Map中的TreeMap实现的。另外，TreeSet还实现了SortedSet接口，因此是有序的集合（集合中的元素要实现Comparable接口，并覆写Compartor函数才行）。

可以看到，抽象类AbstractCollection、AbstractList和AbstractSet分别实现了Collection、List和Set接口，这就是在Java集合框架中用的很多的模板（而非适配器）设计模式，用这些抽象类去实现接口，在抽象类中实现接口中的若干或全部方法，这样下面的一些类只需直接继承该抽象类，并实现自己需要的方法即可，而不用实现接口中的全部抽象方法。

Map是一个映射接口，其中的每个元素都是一个key-value键值对，同样抽象类AbstractMap通过模板模式实现了Map接口中的大部分函数，TreeMap、HashMap、WeakHashMap等实现类都通过继承AbstractMap来实现，另外，不常用的HashTable继承自Dictionary并直接实现了Map接口，此外它还是线程安全的（简单粗暴的使用synchronized，所以效率较低），它和Vector都是JDK1.0就引入的集合类。

Iterator是遍历集合的迭代器（不能遍历Map，只用来遍历Collection），Collection的实现类都实现了iterator()函数，它返回一个Iterator对象，用来遍历集合，ListIterator则专门用来遍历List。而Enumeration则是JDK1.0时引入的，作用与Iterator相同，但它的功能比Iterator要少，它只能再Hashtable、Vector和Stack中使用。

Arrays和Collections是用来操作数组、集合的两个工具类，例如在ArrayList和Vector中大量调用了Arrays.Copyof()方法，而Collections中有很多静态方法可以返回各集合类的synchronized版本，即线程安全的版本，当然了，如果要用线程安全的结合类，首选Concurrent并发包下的对应的集合类。

## HashMap

1. 什么时候会使用HashMap？他有什么特点？
是基于Map接口的实现，存储键值对时，它可以接收null的键值，是非同步的，HashMap存储着Entry(hash, key, value, next)对象。
2. 你知道HashMap的工作原理吗？
通过hash的方法，通过put和get存储和获取对象。存储对象时，我们将K/V传给put方法时，它调用hashCode计算hash从而得到bucket位置，进一步存储，HashMap会根据当前bucket的占用情况自动调整容量(超过Load Facotr则resize为原来的2倍)。获取对象时，我们将K传给get，它调用hashCode计算hash从而得到bucket位置，并进一步调用equals()方法确定键值对。如果发生碰撞的时候，Hashmap通过链表将产生碰撞冲突的元素组织起来，在Java 8中，如果一个bucket中碰撞冲突的元素超过某个限制(默认是8)，则使用红黑树来替换链表，从而提高速度。
3. 你知道get和put的原理吗？equals()和hashCode()的都有什么作用？
通过对key的hashCode()进行hashing，并计算下标( n-1 & hash)，从而获得buckets的位置。如果产生碰撞，则利用key.equals()方法去链表或树中去查找对应的节点
4. 你知道hash的实现吗？为什么要这样实现？
在Java 1.8的实现中，是通过hashCode()的高16位异或低16位实现的：(h = k.hashCode()) ^ (h >>> 16)，主要是从速度、功效、质量来考虑的，这么做可以在bucket的n比较小的时候，也能保证考虑到高低bit都参与到hash的计算中，同时不会有太大的开销。
5. 如果HashMap的大小超过了负载因子(load factor)定义的容量，怎么办？
如果超过了负载因子(默认0.75)，则会重新resize一个原来长度两倍的HashMap，并且重新调用hash方法。

## JavA ConcurrentHashMap

在以前的版本貌似ConcurrentHashMap引入了一个“分段锁”的概念，具体可以理解为把一个大的Map拆分成N个小的HashTable，根据key.hashCode()来决定把key放到哪个HashTable中。在ConcurrentHashMap中，就是把Map分成了N个Segment，put和get的时候，都是现根据key.hashCode()算出放到哪个Segment中。

通过把整个Map分为N个Segment（类似HashTable），可以提供相同的线程安全，但是效率提升N倍。

## Java LinkList

LinkedList是基于双向链表（从源码中可以很容易看出）实现的，除了可以当做链表来操作外，它还可以当做栈、队列和双端队列来使用。

LinkedList同样是非线程安全的，只在单线程下适合使用。

以双向链表实现。链表无容量限制，但双向链表本身使用了更多空间，也需要额外的链表指针操作。

按下标访问元素—get(i)/set(i,e) 要悲剧的遍历链表将指针移动到位(如果i>数组大小的一半，会从末尾移起)。

插入、删除元素时修改前后节点的指针即可，但还是要遍历部分链表的指针才能移动到下标所指的位置，只有在链表两头的操作—add(), addFirst(),removeLast()或用iterator()上的remove()能省掉指针的移动。

## Java ArrayList

以数组实现。节约空间，但数组有容量限制。超出限制时会增加50%容量，用System.arraycopy()复制到新的数组，因此最好能给出数组大小的预估值。默认第一次插入元素时创建大小为10的数组。
按数组下标访问元素—get(i)/set(i,e) 的性能很高，这是数组的基本优势。
直接在数组末尾加入元素—add(e)的性能也高，但如果按下标插入、删除元素—add(i,e), remove(i), remove(e)，则要用System.arraycopy()来移动部分受影响的元素，性能就变差了，这是基本劣势。

## Java CopyOnWriteArrayList
并发优化的ArrayList。用CopyOnWrite策略，在修改时先复制一个快照来修改，改完再让内部指针指向新数组。
因为对快照的修改对读操作来说不可见，所以只有写锁没有读锁，加上复制的昂贵成本，典型的适合读多写少的场景。如果更新频率较高，或数组较大时，还是Collections.synchronizedList(list)，对所有操作用同一把锁来保证线程安全更好。
增加了addIfAbsent(e)方法，会遍历数组来检查元素是否已存在，性能可想像的不会太好。

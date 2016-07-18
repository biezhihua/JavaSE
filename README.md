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
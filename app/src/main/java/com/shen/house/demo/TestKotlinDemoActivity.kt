package com.shen.house.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TextView
import junit.framework.Assert.*
import java.io.File

/**
 * 语法简洁，支持lambda表达式，强大的when语法，不用写分号结
 */
class TestKotlinDemoActivity : AppCompatActivity() {
    //    private lateinit var button1: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//    每个abc!! 转码后都会 if (abc==null)  Intrinsics.throwNpe()
//    每个abc? 转码后都会 加上非空if判断

//        button1 = findViewById<Button>(R.id.Button1)
//        var button2 = findViewById<Button>(R.id.Button2)
//        button1.setOnClickListener(View.OnClickListener { testOne() })
//        button1.setOnClickListener({ testOne() })
//        button1.setOnClickListener() { testOne() }
    }

    //1.12 定义函数
    private fun testOne() {

    }

    //函数带有两个int类型参数，并且返回int类型值：
    fun sum(a: Int, b: Int): Int {
        return a + b
    }

    //函数体可以是个表达式，并可以从中推断出返回值类型：
    fun sum(a: Int, s: String): String = s + a

    fun sum2(a: Int, s: String) = s + a

    //函数也可以返回无意义的值：
    fun printSomething(str: String): Unit {
        println(str)
    }

    //Unit返回值类型可以省略：
    fun pringSomething2(str1: String) {
        println(str1)
    }

    // 1.1.3 定义局部变量
    fun method(): Unit {
        //一次赋值（只读）局部变量：
        val a: Int = 0
        val b = 1//自动判断为Int类型
        val c: Int
        c = 1
//        c = 2
        var d: Int//可变变量
        d = 1
        d = 2
        d += 1
    }

    //两种代码注释方式：
    /*感受感受*/
    /*
     */
    /**
     *
     */
    /***
     *
     */
    /****
     *
     */

    //1.1.4 使用字符串模板
    open fun main(args: Array<String>) {
        if (args.size == 0) return
        print("First argu${args}ment: ${args[0]}")
        print("First argument:${args[0]}dd" + args[0])
    }

    //1.1.5 使用条件表达式
    fun max(x: Int, y: Int): Int {
        if (x > y) {
            return x
        } else if (x == y) {
            return y
        } else {
            return y
        }
    }

    //使用if表达式：
    fun max2(x: Int, y: Int) = if (x > y) x else y

    //    1.1.6使用nullable值检测空（null）值
    //    当空值可能出现时，引用必须明确标记出可null值。
    fun parseInt(str: String): Int? {
        // ...
        return null
    }

    //    1.1.7 使用类型检查和自动类型转换
//    is操作符检查表达式是否类型实例/
    fun method(any: Any): Int? {
        if (any is String) {
            return any.length
        }
//        或
        if (any !is String) {
            return null
        } else {
            return any.length
        }
        // 在`&&`右手边条件成立时，`obj`自动转换到`String`
        if (any is String && any.length > 0)
            return any.length
        return null
    }

    //    1.1.8 使用for循环
    fun method2(lists: ArrayList<String>) {
        for (list in lists) {
            print(list)
        }
//        或者
        for (i in lists.indices) {
            print(lists[i])
        }
    }
//    1.1.9 使用while循环

    fun method3(args: Array<String>) {
        var i = 0
        while (i < args.size)
            print(args[i++])
    }

    //    1.1.11 使用范围/
//    使用in操作符检查一个数字是否在一个范围内：
    fun method5(x: Any) {
//        检查一个数字是否超出范围：
        if (x in 1..10) print("OK")
        if (x !in 46..55) print("")
//        遍历整个范围：
        for (x in 1..22) {

        }
    }

    //   1.1.12 使用集合

    fun method6(nas: ArrayList<String>): Unit {
        //    遍历一个集合：
        for (abc in nas) {
        }
        //使用in操作符检查一个集合是否包含一个对象：
        if ("" in nas) {//等同于
        }
        if (nas.contains("")) {

        }
    }

    fun method7(names: ArrayList<String>) {
//        使用Lambda表达式过滤和映射集合：
        names.filter { it.startsWith("A") }
                .sortedBy { it }
                .map { it.toUpperCase() }
                .forEach { print(it) }

    }

    //data 数据类!!!
    //    1.2 在Kotlin中，一个类有一个primary constructor,一个或多个secondary constructors。primary constructor 属于类头部分，它在类名之后
    data class Customerrr0 constructor(val name: String, val email: String)

    //如果primary constructor没有任何注解或者指示符，constructor关键字可以被省略
    //也可以直接把primary constructor中的参数直接声明成为类的属性，定义的方法是在参数名前加上 var 或者 val 关键字，
// val 是代表属性是常量。在创建类的时候，调用构造函数就直接把它们进行了初始化，这样就不用在类中单独声明类的属性了。
    data class Customerrr2(val name: String, val email: String) {}

    //相反，如果primary constructory有注解或者可见的指示符,constructor是必须的。
    data class Customerrr3 @SuppressWarnings constructor(val name: String, val email: String) {}

    //primary constructor不能包含任何的代码，初始代码可以放在初始块中，初始化代码块以init关键字开头。
    data class Customerrr4(val name: String, val email: String) {
        init { /**/
        }
    }

    //可以直接用这些参数变量赋值给类的属性，或者用构造代码块来实现初始化。
    class Customerrr5(name: String, email: String) {
        var name: String
        var email: String

        init {
            this.name = name
            this.email = email
        }
    }

    data class Constomerrr6(val name: String, val email: String) {
        //除了primary constructory，还有secondary constructors，
        //如果primary constructory和secondary constructors同时存在，每个secondary constructor需要委托到primary constructor中去
        constructor(name: String) : this(name, "") {
            //do something
        }

    }


    fun method8() {
        var c = Customerrr0("", "")
        var name = c.component1()
        name = c.name
        var copy = c.copy("1", "")
        val list = listOf(1, 2, 3, 4, 5, 6)
        assertTrue(list.any { it % 2 == 0 })
        assertFalse(list.any { it > 10 })
    }

    //list一些操作
    fun method9() {
        val list = listOf(1, 2, 3, 4, 5, 6)
        assertTrue(list.any { it % 2 == 0 })
        assertFalse(list.any { it > 10 })
        assertTrue(list.all { it < 10 })
        assertFalse(list.all { it % 2 == 0 })
        assertEquals(3, list.count { it % 2 == 0 })
        assertEquals(25, list.fold(4) { total, next -> total + next })
        list.forEachIndexed { index, i -> print(i) }
        assertEquals(1, list.maxBy { -it })
    }

    //1.2.2 函数参数的默认值
    fun method10(str: String? = "a", str1: String? = "b") {
//        1.2.3 过滤列表
        val list = listOf(1, 2, 3, 4, 5, 6)
        var list2 = list.filter { x -> x > 9 }
//        或者更简洁的写法：/
        var list1 = list.filter { it > 0 }
    }

    //    1.2.6 遍历键值对/列表对
    fun method11(map: HashMap<Int, String>): Unit {
        for ((key, value) in map) {
            print(key)
            print(value)
        }
        for ((a, b) in map) {
        }
//        1.2.10 访问键值对
        println(map[1])
        map[2] = "b"
    }

    //1.2.11 Lazy属性
    fun method12(): Unit {
        //得到lazy对象
        val init: Lazy<TextView> = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            TextView(this).apply {
                textSize = 10f
            }
        }

//或者 不设置参数
        val init2 = lazy {
            TextView(this).apply {
                textSize = 10f
            }
        }

//等同于
        val init3 by lazy {
            TextView(this).apply {
                textSize = 10f
            }
        }

//使用这个lazy<TextView>对象
        init.value.gravity = Gravity.CENTER

        val p: String by lazy {
            //计算串
            "abc"
        }
    }

    //1.2.14 if非空简写
    fun method13(any: Any): Unit {
        var files = File("test").listFiles()
        print(files?.size)
        //1.2.15 if非空和else简写
        print(files?.size ?: "empty")
        //1.2.16 if空，执行语句
        files ?: throw Exception("files is empty")
        //1.2.17 if非空，执行语句
        files?.let { print("files is not empty") }
//        1.2.18 when语句返回值
        when (any) {
            true -> print("true")
            2 -> print(2)
            else -> throw ExceptionInInitializerError("")
        }
//        1.2.19 try/catch表达式
        val result = try {
            print(2 / 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 和result一起工作
    }

    //1.2.20 if表达式
    fun method14(i: Int): String {
        val result = if (i == 1) {
            println("")
            "one"//此值最后一行,生效
        } else if (i == 2) {
            "two"
        } else {
            "other"
        }
        return result
    }

    //1.2.21 返回Unit类型的方法生成器风格用法---apply
    fun arrayOfMinusOnes(size: Int): IntArray {
        return IntArray(size).apply {
            fill(-1)
        }
    }

    //1.2.23 在对象实例中（‘with’）调用多方法
    class Turtle {
        //    constructor()
        fun penDown() {}

        fun penUp() {}
        fun turn(degrees: Double) {}
        fun forward(pixels: Double) {}
    }

    fun method15(): Unit {
        val myTurtle = Turtle()
        with(myTurtle) {
            //绘制100px正方形
            penDown()
            for (i in 1..4) {
                forward(100.0)
                turn(90.0)
            }
            penUp()
        }
    }

    interface Fooo {
        fun method()
    }

    //1.3 编码约定
//    如有疑问，默认的是Java编码约定，如：
//    ——名称用驼峰式拼写法（并避免在名称中用下划线）
//    ——以大写字母开始
//    ——方法和属性名称以小写字母开始
//    ——用4空格缩进
//    —— public函数应有文档化，使其能在Kotlin文档中显示
    interface Foo<out T : Any> : Fooo {
        fun foo(a: Int): T
    }

    //Kotlin还可以定义局部函数：函数的参数还可以指明默认值
    fun method16(start: Int = 0, end: Int = 10): List<Int> {
        var list = mutableListOf<Int>()
        //        list=ArrayList(10)
//        list = arrayListOf(start,end)
        fun method1() {
            for (num in start..end) {
                list.add(num)
            }
        }

        method1()

        return list
    }

    fun method17(): Unit {
        method16(1, 5)
        //传递参数时可以使用参数名进行赋值，这样不必保证传递参数的顺序
        method16(end = 5, start = 0)
        method16(5)//如果没有传递相应的参数，则会使用默认值
        method16(end = 5)//如果没有传递相应的参数，则会使用默认值
        method16(start = 0)
    }

    //Kotlin支持高阶函数，所谓的高阶函数，就是可以把函数当做参数和返回值使用。下面我们会通过一个使用高阶函数的例子，进一步引入Lambda表达式。看一下例子
    fun dealWithInt(index: Int, arg2: (element: Int) -> Int): Int {
        return arg2(index)
    }

    fun dealFunction(element: Int): Int {
        return element + 1
    }

    fun method18() {
//        println(dealWithInt(1, dealFunction(1)))
        println(dealWithInt(1, fun(element: Int): Int {
            return element + 1
        }))
//        更加简单了，Lambda的完整语法为：
//
//        {
//            参数1, 参数2... ->
//            函数体
//        }
        println(dealWithInt(1, { it + 1 }))
    }

    fun method20(): Unit {
        //这是些伪代码，不能编译的
//        val a: Int? =1 //一个装箱过的 Int (java.lang.Integer)
//        val b: Long? = a?.toLong() // 一个隐式装箱的 Long (java.lang.Long)
//        print( a == b )// 很惊讶吧　这次打印出的是 'false' 这是由于 Long 类型的 equals() 只有和 Long 比较才会相同

    }

    fun method21(): Unit {
//        运算符
//        提供了可以叫中缀形式的方法
        val x = (1 shl 2) and 0x123456

    }
//    final int tableSizeFor(int cap) {
//    int n = cap - 1;
//    n |= n >> 1;
//    n |= n >>> 2;
//    n |= n << 3;
//    return 1;
//}

    internal fun tableSizeFor(cap: Int): Int {
        var n = cap - 1
        n = n or (n shr 1)//shr(bits) – 带符号右移 (相当于 Java’s >>)
        n = n or n.ushr(2)//ushr(bits) – 无符号右移 (相当于 Java’s >>>)
        n = n or (n shl 3)//shl(bits) – 带符号左移 (相当于 Java’s <<)
        return 1
        //and(bits) – 按位与 or(bits) – 按位或 xor(bits) – 按位异或 inv(bits) – 按位翻转
    }

    fun method22(): Unit {
        // 创建一个 Array<String>  内容为 ["0", "1", "4", "9", "16"]
        val asc: Array<String> = Array(5, { i -> (i * i).toString() })
//        var listAnt :Array<Any>=asc//kotlin 不允许我们把 Array<String> 转为 Array<Any>

        val x: IntArray = intArrayOf(1, 2, 3)//Kotlin 有专门的类来表示原始类型从而避免过度装箱： ByteArray, ShortArray, IntArray 等等
        x[0] = x[1] + x[2]
        val s = "Hello World!\n"//可以带分割符
        val text = """//整行String 是由三个引号包裹的("*3),不可以包含分割符但可以包含其它字符：${"abc"}
        for (c in "foo\n")
            print(c)
        """
        val i = 10
        val s2 = "i = $i" // 识别为 "i = 10":模板表达式。一个模板表达式由一个 $ 开始并包含另一个简单的名称
        val s3 = "abc"
        val str = "$s3.length is ${s3.length}" //识别为 "abc.length is 3"

//        如果命名有冲突，我们可以使用 as 关键字局部重命名解决冲突
//        import foo.Bar // Bar 可以使用
//                import bar.Bar as bBar // bBar 代表 'bar.Bar'
//        if 是表达式，比如它可以返回一个值
        val a = 5
        val b = 5
        val max = if (a > b) {
            print("Choose a")
            a//最后一个表达是是该块的值：
            ""//最后一个表达是是该块的值：
        } else {
            print("Choose b")
            b
        }
        //使用when表达式
        val any: Any = ";"
        val t = when (any) {
            0 -> "zero"
            1, 2 -> "1,2"
            parseInt("3") -> "three"//可以用任意表达式作为分支的条件
            true -> ""
            in 1..10 -> print("x is in the range")
            is String -> print("x is String")
            else -> "else: none of the abov"

        }
        //如果没有任何参数提供，那么分支的条件就是简单的布尔表达式，当条件为真时执行相应的分支：
        when {
            any is Int -> print("x is int")
            any is String -> print("x is string")
            else -> print("x is Any")
        }
        loop1@ for (i in 1..100) {//在 Kotlin 中表达式可以添加标签。标签通过 @ 结尾来表示
            loop2@ for (j in i..100) {
                if (i <= j) {
                    print("I:${i};J:${j}")
                    break@loop2
                } else {
                    break@loop1
                }
            }
        }
    }

    fun method23(): Unit {
        var ints: List<Int> = arrayListOf(2, 2, 2)
        val b = 2
//        return 允许我们返回到外层函数
        ints.forEach {
            if (it == 0) return@method23
            if (it == 0) return
            print(it)
        }
        //return 表达式返回到最近的闭合函数
        ints.forEach lit@{
            if (it == 0) return@lit
            print(it)
        }
        //经常用一种更方便的含蓄的标签：比如用和传入的 lambda 表达式名字相同的标签。
        ints.forEach {
            if (it == 0) return@forEach
            print(it)
        }
        fun innermethod() {
            return@innermethod//可以内可以外层函数
        }
        //我们可以用函数表达式替代字面函数。在函数表达式中使用 return 语句可以从函数表达式中返回。
        ints.forEach(fun(value: Int) {
            if (value == 0) return
            print(value)
        })
    }

    //默认情形下，kotlin 中所有的类都是 final ,想要被extend是需要加open关键词
    open class A1 {
        open val a = 0
        open var b = 0
        open fun fun1(): Unit {//默认都是 final
        }

        fun fun2(): Unit {
        }
    }

    interface A2 {
        //接口不需要open修饰
        fun fun1(): Unit {//接口的成员变量默认是 open 的
            print("<A2>.fun1()")
        }
    }

    //extends(),implements 无()
    open class A3 : A1(), A2 {
        override var a = 0//父类val 可以复写成var
        //        override val b=0//var复写成val不行...
        final override fun fun1(): Unit {//标记为override的成员是open的，它可以在子类中被复写。如果你不想被重写就要加 final:
            fun2()//调用父类方法
        }

        fun funInA3(): Unit {
            super<A1>.fun1()//表示使用父类中提供的方法我们用 super<Base>表示:
            super<A2>.fun1()//如果父类和接口中有两个同名方法,可以这样区分调哪一个
        }
    }

    class A4 : A3() {
        //        override fun fun1(): Unit {
//        }
    }

    fun method27(): Unit {
    }

    //我们可以用一个抽象成员去复写一个带 open 注解的非抽象方法。
    open class Base {
        open fun f() {}
    }

    abstract class Deri : Base() {
        override abstract fun f()
    }

    //伴随对象类似于静态常量
    companion object {
        val str: String = "111"
    }

//    代理模式 给实现继承提供了很好的代替方式， Kotlin 在语法上支持这一点，所以并不需要什么样板代码。Derived 类可以继承 Base 接口并且指定一个对象代理它全部的公共方法：

    interface Base2 {
        fun print()
    }

    class BaseImpl(val x: Int) : Base2 {
        override fun print() {
            print(x)
        }
    }

    class Derived(b: Base2) : Base2 by b

    fun main() {
        val b = BaseImpl(10)
//        Derived(b).print()
    }
//    在 Derived 的父类列表中的 by 从句会将 b 存储在 Derived 内部对象，并且编译器会生成 Base 的所有方法并转给 b。

}























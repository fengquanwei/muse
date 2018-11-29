package com.fengquanwei.muse.guava.basic;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

/**
 * Objects 用法
 *
 * @author fengquanwei
 * @create 2018/11/28 10:28
 **/
public class ObjectsUsage {
    public static void main(String[] args) {
        System.out.println(Objects.equal(null, "a"));

        A a1 = new A("Lask", 26);
        System.out.println(a1.hashCode());

        System.out.println(a1.toString());

        A a2 = new A("Lynn", 26);
        System.out.println(a1.compareTo(a2));
    }

    public static class A implements Comparable<A> {
        private String name;
        private int age;

        public A(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.name, this.age);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("name", this.name)
                    .add("age", this.age)
                    .toString();
        }

        @Override
        public int compareTo(A that) {
            return ComparisonChain.start()
                    .compare(this.name, that.name)
                    .compare(this.age, that.age)
                    .result();
        }
    }
}

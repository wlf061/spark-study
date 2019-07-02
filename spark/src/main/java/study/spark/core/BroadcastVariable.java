package study.spark.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;

import java.util.Arrays;
import java.util.List;


/**
 * @ClassName: BroadcastVariable
 * @Author: nancy
 * @Date: 2019/7/1 10:34
 * @Description:
 */
public class BroadcastVariable {
    public static void main(String[] args){
        SparkConf conf = new SparkConf()
                .setAppName("BroadcastVariable")
                .setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        final int factor = 3;
        final Broadcast<Integer> factorBroadCast = sc.broadcast(factor);
        List<Integer> numberList = Arrays.asList(1,2,3,4,5);
        JavaRDD<Integer> numbers = sc.parallelize(numberList);
        JavaRDD<Integer> multipleNumbers = numbers.map(new Function<Integer, Integer>(){

            @Override
            public Integer call(Integer v1) throws Exception {
                int factor = factorBroadCast.value();
                return v1 * factor;
            }
        });
        multipleNumbers.foreach(new VoidFunction<Integer>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void call(Integer t) throws Exception {
                System.out.println(t);
            }

        });

        sc.close();
    }
}

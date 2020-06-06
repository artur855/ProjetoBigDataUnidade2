package org.example;

import static org.apache.spark.sql.functions.*;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.graphx.Graph;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.graphframes.GraphFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("BigData");
        sparkConf.set("spark.network.timeout", "100001s");
        sparkConf.set("spark.storage.blockManagerSlaveTimeoutMs", "100001s");
        sparkConf.set("spark.executor.heartbeatInterval", "100000s");
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
//        ctx.setLogLevel("ERROR");
        SparkSession sparkSession = SparkSession.builder()
                .config(sparkConf).getOrCreate();

//        JavaRDD<String> nodesF = ctx.textFile("C:\\Users\\artur\\Downloads\\example_index");
//        JavaRDD<String> edgesF = ctx.textFile("C:\\Users\\artur\\Downloads\\example_arcs");
//        JavaRDD<Vertice> verticesRDD = nodesF.map((str) -> new Vertice(Long.parseLong(str.split("\t")[1]), str.split("\t")[0]));

        JavaRDD<String> f = ctx.textFile("C:\\Users\\artur\\Downloads\\web-Stanford.txt\\web-Stanford.txt");
        JavaRDD<Vertice> verticesRDD = f.flatMap((str) -> Arrays.asList(str.split("\t")).iterator()).distinct().map((str)->new Vertice(Long.parseLong(str), str));
        JavaRDD<Edge> edgesRDD = f.map((str) -> new Edge(str.split("\t")[0], str.split("\t")[1]));
        Dataset<Row> vertices = sparkSession.createDataFrame(verticesRDD, Vertice.class);
        Dataset<Row> edges = sparkSession.createDataFrame(edgesRDD, Edge.class);


        //Dataset<Row> vertices = sparkSession.read().format("csv").option("header", true).option("delimiter", ";").csv("D:\\Workspace\\projetos\\Unit\\BigData\\dataset\\definicao_tec.csv");
        //Dataset<Row> edges = sparkSession.read().format("csv").option("header", true).option("delimiter", ";").csv("D:\\Workspace\\projetos\\Unit\\BigData\\dataset\\conecao_tec.csv");
        GraphFrame graphFrame = new GraphFrame(vertices, edges);
        long start = System.currentTimeMillis();
        GraphFrame pageRank = graphFrame.pageRank().maxIter(10).run();
        long end = System.currentTimeMillis();
        pageRank.vertices().select("id", "pagerank").show();
        List<Row> v = pageRank.vertices().collectAsList();
        v.sort((r1, r2) -> {
            double x1 = r1.getDouble(2);
            double x2 = r2.getDouble(2);
            if (x1 == x2)
                return 0;
            else if (x1 > x2) {
                return -1;
            } else {
                return 1;
            }
        });

        v.subList(0, 10).forEach((o) -> System.out.println(o.get(0) +"\t"+ o.get(1)+"\t"+o.get(2)));
        System.out.println((end - start) / 1000 + "s");
        /*
        List<Vertice> users = new ArrayList<>();
        users.add(new Vertice(1L, "site1"));
        users.add(new Vertice(2L, "site2"));
        users.add(new Vertice(3L, "site3"));

        List<Edge> relationships = new ArrayList<>();
        relationships.add(new Edge("1", "2", 2));
        relationships.add(new Edge("2", "3", 5));

        Dataset<Row> verDF = sparkSession.createDataFrame(users, Vertice.class);
        Dataset<Row> edgDF = sparkSession.createDataFrame(relationships, Edge.class);

        GraphFrame graphFrame = new GraphFrame(verDF, edgDF);
        graphFrame.inDegrees().show();
        GraphFrame paths = graphFrame.pageRank().maxIter(10).run();
        paths.vertices().show();
        paths.edges().show();
        */


    }
}


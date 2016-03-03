package storm;

import java.io.Serializable;

import com.google.common.collect.ImmutableList;

import storm.bolt.FPGrowthBolt;
import storm.bolt.PatternTreeBolt;
import storm.bolt.PrintBolt;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.spout.InputTimeSpout;
import storm.spout.ReadLogSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

public class Topology implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	// 实例化TopologyBuilder类。
	private static TopologyBuilder builder = new TopologyBuilder();

	public static void main(String[] args) throws InterruptedException,
			AlreadyAliveException, InvalidTopologyException {
		   String kafkaZookeeper = "10.20.100.5:2181";

			BrokerHosts brokerHosts = new ZkHosts(kafkaZookeeper);
			
			SpoutConfig inConfig = new SpoutConfig(brokerHosts, "fpstreamin0",
					"/fpstreamin0", "id");
			inConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
			inConfig.zkServers = ImmutableList.of("10.20.100.5");
			Integer port=new Integer(2181);
			inConfig.zkPort = port;
			
			SpoutConfig dataConfig = new SpoutConfig(brokerHosts, "fpstreamdata0",
					"/fpstreamdata0", "id");
			dataConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
			dataConfig.zkServers = ImmutableList.of("10.20.100.5");
			dataConfig.zkPort = port;
			
		Config config = new Config();

		// 设置喷发节点并分配并发数，该并发数将会控制该对象在集群中的线程数。
		builder.setSpout("readlog", new KafkaSpout(dataConfig),1);
		//builder.setSpout("readlog", new ReadLogSpout(), 1);
		// 设置数据处理节点并分配并发数。指定该节点接收喷发节点的策略为随机方式。
		//builder.setSpout("systemin", new InputTimeSpout(), 1);	
		builder.setSpout("systemin", new KafkaSpout(inConfig),1);
		//创建FPGrowth节点
//		builder.setBolt("FPGrowth", new FPGrowthBolt(), 1).shuffleGrouping("readlog");
		builder.setBolt("FPGrowth", new FPGrowthBolt(), 1).shuffleGrouping("readlog");//多线程
		builder.setBolt("PatternTree", new PatternTreeBolt(),1).shuffleGrouping("FPGrowth").shuffleGrouping("systemin");
		
		//创建print数据输出节点
//		builder.setBolt("print", new PrintBolt(), 3).shuffleGrouping("PatternTree").shuffleGrouping("systemin"); 		
		

		config.setDebug(false);

		if (args != null && args.length > 0) {
			config.setNumWorkers(1);
			StormSubmitter.submitTopology(args[0], config,
					builder.createTopology());
		} else {
			// 这里是本地模式下运行的启动代码。
//			config.setMaxTaskParallelism(1);
			config.setMaxTaskParallelism(1);//多线程
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("simple", config, builder.createTopology());
		}

	}

}
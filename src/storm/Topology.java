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
	// ʵ����TopologyBuilder�ࡣ
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

		// �����緢�ڵ㲢���䲢�������ò�����������Ƹö����ڼ�Ⱥ�е��߳�����
		builder.setSpout("readlog", new KafkaSpout(dataConfig),1);
		//builder.setSpout("readlog", new ReadLogSpout(), 1);
		// �������ݴ���ڵ㲢���䲢������ָ���ýڵ�����緢�ڵ�Ĳ���Ϊ�����ʽ��
		//builder.setSpout("systemin", new InputTimeSpout(), 1);	
		builder.setSpout("systemin", new KafkaSpout(inConfig),1);
		//����FPGrowth�ڵ�
//		builder.setBolt("FPGrowth", new FPGrowthBolt(), 1).shuffleGrouping("readlog");
		builder.setBolt("FPGrowth", new FPGrowthBolt(), 1).shuffleGrouping("readlog");//���߳�
		builder.setBolt("PatternTree", new PatternTreeBolt(),1).shuffleGrouping("FPGrowth").shuffleGrouping("systemin");
		
		//����print��������ڵ�
//		builder.setBolt("print", new PrintBolt(), 3).shuffleGrouping("PatternTree").shuffleGrouping("systemin"); 		
		

		config.setDebug(false);

		if (args != null && args.length > 0) {
			config.setNumWorkers(1);
			StormSubmitter.submitTopology(args[0], config,
					builder.createTopology());
		} else {
			// �����Ǳ���ģʽ�����е��������롣
//			config.setMaxTaskParallelism(1);
			config.setMaxTaskParallelism(1);//���߳�
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("simple", config, builder.createTopology());
		}

	}

}
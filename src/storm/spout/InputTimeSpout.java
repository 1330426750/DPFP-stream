package storm.spout;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

//import redis.clients.jedis.Jedis;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class InputTimeSpout implements IRichSpout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	FileInputStream fis;
	InputStreamReader isr;
	BufferedReader br;
	String inputtime;
	int i;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
	
//		this.collector = collector;		
//		 Jedis jedis = new Jedis("10.20.100.5",6379,1000);
//		   inputtime=jedis.get("inputtime");
		
			this.collector = collector;		
			br=new BufferedReader(new InputStreamReader(System.in));		
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextTuple() {
	
//		String str = "";
//		try {
//			while ((str = inputtime) != null) {
//			//	System.out.println("str--"+str);
//				this.collector.emit(new Values(str));
//				Thread.sleep(20000);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		String str = "";
		try {
			while ((str = this.br.readLine()) != null) {
				this.collector.emit(new Values(str));
				Thread.sleep(100);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Object msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("systemin"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}

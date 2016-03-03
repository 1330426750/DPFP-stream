package storm.spout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class ReadLogSpout implements IRichSpout {

	/**
	 * @param args
	 */

	private SpoutOutputCollector collector;
	FileInputStream fis;
	InputStreamReader isr;
	BufferedReader br;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		// TODO Auto-generated method stub

		this.collector = collector;

		try {

//			this.fis = new FileInputStream("test.txt");
			this.fis = new FileInputStream("desc1.txt");
//			this.fis = new FileInputStream("/public/home/dsj/Public/sundujing/fpgrowth/desc1.txt");
//			this.fis = new FileInputStream("/home/s/Desktop/desc1.txt");

			this.isr = new InputStreamReader(fis, "UTF-8");
			this.br = new BufferedReader(isr);

		} catch (Exception e) {
			e.printStackTrace();
		}

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
		// TODO Auto-generated method stub
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
		declarer.declare(new Fields("readlog"));

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
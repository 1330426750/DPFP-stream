package storm.bolt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import FPGrowth.SecPackData;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class PrintBolt extends BaseBasicBolt {

	/**
	 * @param args 输入的参数
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
//		try {
//			SecPackData mesg = (SecPackData) input.getValueByField("fpg");
//			if (mesg != null)
//				System.out.println("Tuple：" + mesg);
//			// 打印数据
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
//		try {
//			String mesg=(String) input.getValueByField("PatternTree");
//			//String mesg = input.getString(0);
//			if (mesg != null)
//				System.out.println(mesg);
//			// 打印数据
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	
	
		  try{
				File file = new File("d://text.txt");
		//		File file = new File("/public/home/dsj/Public/sundujing/fpgrowth/fpout.txt");

				if(file.createNewFile()){
					System.out.println("Create file successed");
				}
				String mesg=(String) input.getValueByField("PatternTree");
				if (mesg != null)
				{
			    BufferedWriter out = null;   
		        try {   
		            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));  
		            out.write(mesg+"\n");   
		        } catch (Exception e) {   
		            e.printStackTrace();   
		        } finally {   
		            try {   
		            	if(out != null){
		            		out.close();   
		                }
		            } catch (IOException e) {   
		                e.printStackTrace();   
		            }   
		        } 
				}
			//	method1("d://text.txt", "123");
			//	method2("d://text.txt", "123");
				//method3("d://text.txt", "123");
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
	

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("mesg"));

	}

}

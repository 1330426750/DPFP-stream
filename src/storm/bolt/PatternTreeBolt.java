package storm.bolt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;








import redis.clients.jedis.Jedis;
import FPGrowth.SecPackData;
import FPGrowth.frequent;
import PatternTree.AssociaRules;
import PatternTree.AssociationRules;
import PatternTree.AssociationRules2;
import PatternTree.ModifyPatternTree;
import PatternTree.PatternTree;
import PatternTree.SelectFromPatternTree;
import PatternTree.SelectFromPatternTree2;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

//import storm.bolt.SelectResultBolt;

public class PatternTreeBolt  implements IRichBolt,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	static double support,supportDegree,supportCount;
	private  static PatternTree pttree ;
	boolean tag;       //tag为true时代表使用支持度，为false的时候代表使用支持度计数
	Jedis jedis;
	String time;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
		pttree = new PatternTree();
		pttree.setSupport(0.5);
		pttree.setMistakeRate(0.5);
		tag=true;
		support =0.5;
		supportDegree = 0.5;
		supportCount = 100;
		String host = "10.20.100.5";
		int port = 6379;
		jedis = new Jedis(host, port);
		
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub

		
		if (input.getFields().contains("FPGrowth"))
		{
			 ArrayList<frequent> afr = new ArrayList<frequent>();// fre
			 ArrayList<frequent> afr1 = new ArrayList<frequent>();
			SecPackData mesg = (SecPackData) input.getValueByField("FPGrowth");
			System.out.println(mesg);
			SecPackData mesg1 = null;

			afr = mesg.getItem();// fre
			double count = mesg.getCount();// count
			if(mesg.getTime()!=null)
			{
			time = mesg.getTime();// time
			}
//			System.out.println("time"+time);
//
//			System.out.println("pttree.getPresentTimeWindow()"+pttree.getPresentTimeWindow());
//			System.out.println("Integer.parseInt(time)"+Integer.parseInt(time));
			if(pttree.getPresentTimeWindow()!=Integer.parseInt(time))
			{
				pttree.setChangeTag(1);
			}else {
				pttree.setChangeTag(0);
			}
			//修改时间标记，表示时间是否发生变化

			ModifyPatternTree.modify(afr, count, time, pttree);
			
			String str1;
//			str1= pttree.iteratorTree(pttree.getRoot(), pttree);
//			 System.out.println("modified one sec"+str1);
				ModifyPatternTree.setZero(pttree.getRoot(), pttree.getPresentTimeWindow(), pttree);
		//		str1= pttree.iteratorTree(pttree.getRoot(), pttree);
		//		 System.out.println("after set zero"+str1);
				ModifyPatternTree.deleteNode(pttree.getRoot(), pttree);
//				str1= pttree.iteratorTree(pttree.getRoot(), pttree);
//				 System.out.println("after delete node"+str1);
				pttree.resetCreateTag(pttree.getRoot());
//				str1= pttree.iteratorTree(pttree.getRoot(), pttree);
//				 System.out.println("after reset create tag"+str1);
				ModifyPatternTree.modifyTree(pttree, count,Integer.parseInt(time));
//				str1= pttree.iteratorTree(pttree.getRoot(), pttree);
//				 System.out.println("after modify tree"+str1);
				
				ModifyPatternTree.modifyCount(count, Integer.parseInt(time), pttree); 
				
				//打印出count总数的滑动窗口
//				System.out.println(pttree.get1secTupleNum(0));
//				 System.out.println(pttree.get1secTupleNum(1));
//				 System.out.println(pttree.get1secTupleNum(2));
//				 System.out.println(pttree.get1secTupleNum(3));
//				 System.out.println(pttree.get1secTupleNum(4));
//					System.out.println(pttree.get5secTupleNum(0));
//				System.out.println(pttree.get1secMidTupleNum(0));
//				System.out.println(pttree.get1secMidTupleNum(1));
//				System.out.println(pttree.get1secMidTupleNum(2));
//				System.out.println(pttree.get1secMidTupleNum(3));
				
//				System.out.println(pttree.getRoot().get1secWindow(0));
//				System.out.println(pttree.getRoot().get1secWindow(1));
//				System.out.println(pttree.getRoot().get1secWindow(2));
//				System.out.println(pttree.getRoot().get1secWindow(3));
//				System.out.println(pttree.getRoot().get1secWindow(4));
//				System.out.println(pttree.getRoot().get5secWindow(0));
				
				
				str1= pttree.iteratorTree(pttree.getRoot(), pttree);
				 System.out.println("after modify tree"+str1);
//				  try{
//					  File file = new File("/public/home/dsj/Public/sundujing/fpgrowth/fpout.txt");
//
//						if(file.createNewFile()){
//							System.out.println("Create file successed");
//						}
//						
//						if (str1 != null)
//						{
//					    BufferedWriter out = null;   
//				        try {   
//				            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));  
//				            out.write(str1+"\n");   
//				        } catch (Exception e) {   
//				            e.printStackTrace();   
//				        } finally {   
//				            try {   
//				            	if(out != null){
//				            		out.close();   
//				                }
//				            } catch (IOException e) {   
//				                e.printStackTrace();   
//				            }   
//				        } 
//						}
//					}catch(Exception e){
//						System.out.println(e);
//					}
//			
		
			
			
			
		}
// else {
//	String str;
//	str = input.getString(0);
//	System.out.println("strgetbypattertree --"+str);
//	String str_out = null;
//	int time;
//	time = Integer.parseInt(str)-1;
//	str_out = SelectFromPatternTree.selectFromPatternTree(pttree, time);
//	System.out.println("haha"+str_out);
//	this.collector.emit(new Values(str_out));
//	collector.ack(input);
//}
		
		 else{	 

				String str;
				str = input.getString(0);
				System.out.println(str);
				String[] s = str.split(":");
				System.out.println("s[0]"+s[0]);
//				if(s[0].equals("parameter"))
//				{
//					pttree.setSupport(Double.parseDouble(s[2]));
//					System.out.println("s[2]setSupport"+s[2]);
//					pttree.setMistakeRate(Double.parseDouble(s[1]));
//					System.out.println("s[1]setMistakeRate"+s[1]);
//				}
				if(s[0].equals("support"))
				{
					pttree.setSupport(Double.parseDouble(s[1]));
					System.out.println("s[2]setSupport"+s[1]);
					support = Double.parseDouble(s[1]);
					tag = true;
				}
				else if(s[0].equals("supportcount")) {
					supportCount = Double.parseDouble(s[1]);
					tag = false;
				}
				else if(s[0].equals("select")) {
					if(tag==true) {
     			    	String str_out = "";
	     		    	int time;
	     		    	time = Integer.parseInt(s[1]);
		    	     	System.out.println("time"+time);
		     	    	str_out = SelectFromPatternTree.selectFromPatternTree(pttree, time);
//			         	System.out.println(str_out);
		     	    	System.out.println("selectFromPatternTree"+str_out);
		     	    	String str_key = "FPStream_result";
			    	
	    				jedis.set(str_key, str_out);
//			    	this.collector.emit(new Values(str_out));
					}
					
					if(tag==false) {
						String str_out = "";
	     		    	int time;
	     		    	time = Integer.parseInt(s[1]);
		    	     	System.out.println("time"+time);
		     	    	str_out = SelectFromPatternTree2.select(pttree, time,supportCount);
//			         	System.out.println(str_out);
		     	    	System.out.println("selectFromPatternTree"+str_out);
		     	    	String str_key = "FPStream_result";
			    	
	    				jedis.set(str_key, str_out);
//			    	this.collector.emit(new Values(str_out));
					}
					
			    	collector.ack(input);
				}
				if(s[0].equals("association")) {
					int time;
			    	time = Integer.parseInt(s[1]);
			    	double confidence;
			    	confidence = Double.parseDouble(s[2]);
			    	if(tag==true) {
	     				ArrayList<String> list;
		    			list = AssociationRules.stringArrayList(pttree, time);
		     			ArrayList<AssociaRules> arList;
		    			arList = new ArrayList<AssociaRules>();
			    		for(String str1: list) {
			    			AssociaRules ar = new AssociaRules(str1);
			    			arList.add(ar);
		    			}
		    			String str_out = "";
					
			    		for(int i=0;i<arList.size();i++) {
		    				for(int j=0;j<arList.size();j++) {
				    			if(i!=j) {
					    			str_out = str_out+arList.get(i).isAssociated(arList.get(j), confidence);
				    			}
				    		}
				    	}					
			     		System.out.println("FPStream_association"+str_out);					
			     		String str_key = "FPStream_association";								    	
			    		jedis.set(str_key, str_out);	
			     	}
			    	
			    	if(tag==false) {
			    		ArrayList<String> list;
		    			list = AssociationRules2.stringArrayList(pttree, time,supportCount);
		     			ArrayList<AssociaRules> arList;
		    			arList = new ArrayList<AssociaRules>();
			    		for(String str1: list) {
			    			AssociaRules ar = new AssociaRules(str1);
			    			arList.add(ar);
		    			}
		    			String str_out = "";
					
			    		for(int i=0;i<arList.size();i++) {
		    				for(int j=0;j<arList.size();j++) {
				    			if(i!=j) {
					    			str_out = str_out+arList.get(i).isAssociated(arList.get(j), confidence);
				    			}
				    		}
				    	}					
			     		System.out.println("FPStream_association"+str_out);					
			     		String str_key = "FPStream_association";
			     		jedis.set(str_key, str_out);	
			    	}
			    	
			    	collector.ack(input);
					
//				else if(s[0].equals("select")) {
//					
//			    	String str_out = "";
//			    	int time;
//			    	time = Integer.parseInt(s[1]);
//			     	System.out.println("time"+time);
//			    	str_out = SelectFromPatternTree.selectFromPatternTree(pttree, time);
//			    	System.out.println("selectFromPatternTree"+str_out);
//			    	String str_key = "FPStream_result";
//			    	String host = "10.20.100.5";
//					int port = 6379;
//					Jedis jedis = new Jedis(host, port);
//					jedis.set(str_key, str_out);		
//			    	collector.ack(input);
//				}
//				if(s[0].equals("association")) {
//					int time;
//			    	time = Integer.parseInt(s[1]);
//			    	double confidence;
//			    	confidence = Double.parseDouble(s[2]);
//					ArrayList<String> list;
//					list = AssociationRules.stringArrayList(pttree, time);
//					ArrayList<AssociaRules> arList;
//					arList = new ArrayList<AssociaRules>();
//					for(String str1: list) {
//						AssociaRules ar = new AssociaRules(str1);
//						arList.add(ar);
//					}
//					String str_out = "";
//					
//					for(int i=0;i<arList.size();i++) {
//						for(int j=0;j<arList.size();j++) {
//							if(i!=j) {
//								str_out = str_out+arList.get(i).isAssociated(arList.get(j), confidence);
//							}
//						}
//					}
//					
//					System.out.println("FPStream_association"+str_out);
//					
//					String str_key = "FPStream_association";
//					
//			    	String host = "10.20.100.5";
//					int port = 6379;
//					Jedis jedis = new Jedis(host, port);
//					jedis.set(str_key, str_out);					
//			    	collector.ack(input);				
				}
		 }
		
// Jedis jedis = new Jedis("10.20.100.5",6379,1000);
//while(jedis.get("a")){
//	String str;
//	str = input.getString(0);
//	System.out.println("hha");
//	System.out.println("strgetbypattertree --"+str);
//	String str_out = null;
//	int time;
//	time = Integer.parseInt(str)-1;
//	str_out = SelectFromPatternTree.selectFromPatternTree(pttree, time);
////	 System.out.println("str_out"+str_out);
//	this.collector.emit(new Values(str_out));
//	collector.ack(input);
//}
}
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("PatternTree"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
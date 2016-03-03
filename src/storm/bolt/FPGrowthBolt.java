package storm.bolt;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import redis.clients.jedis.Jedis;

import clj_time.format__init;

//import mysequence.machineleaning.association.fpgrowth.Myfptree3;

import FPGrowth.SecPackData;
import FPGrowth.TreeNode2;
import FPGrowth.frequent;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class FPGrowthBolt implements IRichBolt,Serializable{
	
//	SimpleDateFormat df = new SimpleDateFormat("ss");//&&&&&&&&&
//	String t=df.format(new Date());//��ʼʱ��&&&&&&&&&&&&&&&&&&&&&&&
	String t = null, time1 = null;
	int flag = 0;
	double countsecnum=0.0;
	private OutputCollector collector;
	private LinkedList<LinkedList<String>> records = new LinkedList<LinkedList<String>>();
	public frequent fr = new frequent();
	public SecPackData spd = new SecPackData();
	public String time=null;
	//public static long startTime = System.currentTimeMillis();
	int support;

double supportDegree ;      //�������֧�ֶ����

	public static Map<String, Integer> ordermap = new HashMap<String, Integer>();
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		System.out.println("FPGrowthBolt	--	Start!");
		this.collector = collector;
		supportDegree = 0.5;      //�������֧�ֶ����
	}

	@Override
	public void execute(Tuple input) {
	
//		 Jedis jedis = new Jedis("10.20.100.5",6379,1000);
//		 String sd=jedis.get("  ");
//		 supportDegree=Double.parseDouble(sd);
		
		
				//String line = (String)input.getValueByField("readlog");
				String line = input.getString(0);
		//		System.out.println("line"+line);
		countsecnum++;
		String[] str = line.split(" ");
		LinkedList<String> litm = new LinkedList<String>();
		for (int i = 0; i < str.length - 1; i++) 
		{
			litm.add(str[i].trim());
		}
		LinkedList<String> record1 = new LinkedList<String>();
		record1.addAll(removeDuplicateWithOrder(litm));
		records.add(record1);
		time = str[str.length - 1];
		if (flag == 0) {
			t = time;
			flag = 1;
		}
		if (!t.equals(time)) 
		{
			support =(int) (records.size() * supportDegree);
			spd.clear();
			spd.setCount(countsecnum);
			LinkedList<TreeNode2> F1 = buildHeaderLink(records);
			LinkedList<TreeNode2> orderheader = buildHeaderLink(records);
			orderF1(orderheader);
			System.out.println("start"+ t);
			spd.setTime(t);
			printF1(F1);
			SecPackData s=	fpgrowth(records, null, F1);
			this.collector.emit(new Values(s));
			countsecnum=0;
			records.clear();
		}
		t = time;
		 
	}  

	@Override
	public void cleanup() {
	
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("FPGrowth"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/** 
     * ȥ��ÿ��Ԫ���е��ظ��� 
     *  
     * @param list �����ظ����List���� 
     * @return ����ȥ���ظ����List����
     */  
 
    public List<String> removeDuplicateWithOrder(LinkedList<String> list) {
    	Set set = new HashSet();
    	LinkedList<String> newList = new LinkedList<String>();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Object element = iter.next();
    		if (set.add(element))
    			newList.add((String) element);
    	} 
    	list.clear();
    	list.addAll(newList);
    	//System.out.println( " remove duplicate " + list);
		return list;
    } 

    /** 
     * ������ͷ���������ڵ���ڣ���������1,����ʹ���һ���½ڵ����
     *  
     * @param records ���LinkedList<String>�����LinkedList����
     * @return ���ش�����ڵ��LinkedList����
	 *
     */
	public LinkedList<TreeNode2> buildHeaderLink(
			LinkedList<LinkedList<String>> records) {
		LinkedList<TreeNode2> header = null;
		if (records.size() > 0) {
			header = new LinkedList<TreeNode2>();
		} else {
			return null;
		}
		Map<String, TreeNode2> map = new HashMap<String, TreeNode2>();
		for (LinkedList<String> items : records) {

			for (String item : items) {
				// �������������1��������������
				if (map.containsKey(item)) {
					map.get(item).Sum(1.0);
				} else {
					TreeNode2 node = new TreeNode2();
					node.setName(item);
					node.setCount(1.0);
					map.put(item, node);
				}
			}
		}
		// ��֧�ֶȴ��ڣ�����ڣ�minSup������뵽F1��
		Set<String> names = map.keySet();
		for (String name : names) {
			TreeNode2 tnode = map.get(name);
			if (tnode.getCount() >= support) {
				header.add(tnode);
			}
		}
		sort(header);

		String test = "ddd";
		return header;
	}
	/** 
	   * ѡ������,���������ȣ�����������,�ֵ�˳��,��Сд���д
	   *  
	   * @param list ��������ڵ��List���� 
	   * @return  ���ش�����ڵ��List����
	   */ 
	public List<TreeNode2> sort(List<TreeNode2> list) {
		int len = list.size();
		for (int i = 0; i < len; i++) {

			for (int j = i + 1; j < len; j++) {
				TreeNode2 node1 = list.get(i);
				TreeNode2 node2 = list.get(j);
				if (node1.getCount() < node2.getCount()) {
					TreeNode2 tmp = new TreeNode2();
					tmp = node2;
					list.remove(j);
					// listָ��λ�ò��룬ԭ����>=jԪ�ض��������ƣ�����ɾ��,���Բ���ǰҪɾ����ԭ����Ԫ��
					list.add(j, node1);
					list.remove(i);
					list.add(i, tmp);
				}
				// ���������ȣ�����������,�ֵ�˳��,��Сд���д
				if (node1.getCount() == node2.getCount()) {
					String name1 = node1.getName();
					String name2 = node2.getName();
					int flag = name1.compareTo(name2);
					if (flag > 0) {
						TreeNode2 tmp = new TreeNode2();
						tmp = node2;
						list.remove(j);
						// listָ��λ�ò��룬ԭ����>=jԪ�ض��������ƣ�����ɾ��,���Բ���ǰҪɾ����ԭ����Ԫ��
						list.add(j, node1);
						list.remove(i);
						list.add(i, tmp);
					}

				}
			}
		}

		return list;
	}
	 /** 
	   *   ѡ�����򣬽���,���ͬ����L�еĴ�������(��header����Ԫ�ص�˳���������)
	   *  
	   * @param lis �����String���͵�LinkedList����
	   * @param header ��������ڵ��List����
	   * @return ���ش��String���͵�List����
	   */
	public List<String> itemsort(LinkedList<String> lis, List<TreeNode2> header) {
		// List<String> list=new ArrayList<String>();
		// ѡ������
		int len = lis.size();
		for (int i = 0; i < len; i++) {
			for (int j = i + 1; j < len; j++) {
				String key1 = lis.get(i);
				String key2 = lis.get(j);
				Double value1 = findcountByname(key1, header);
				if (value1 == -1)
					continue;
				Double value2 = findcountByname(key2, header);
				if (value2 == -1)
					continue;
				if (value1 < value2) {
					String tmp = key2;
					lis.remove(j);
					lis.add(j, key1);
					lis.remove(i);
					lis.add(i, tmp);
				}
				if (value1 == value2) {
					int v1 = ordermap.get(key1);
					int v2 = ordermap.get(key2);
					if (v1 > v2) {
						String tmp = key2;
						lis.remove(j);
						lis.add(j, key1);
						lis.remove(i);
						lis.add(i, tmp);
					}
				}
			}
		}
		return lis;
	}
	/** 
	   *   ͨ���������ҵ����Ӧ�ĸ���
	   *  
	   * @param itemname ������ 
	   * @param header ��������ڵ��List����
	   * @return ���������ƶ�Ӧ������
	   */
	public static Double findcountByname(String itemname, List<TreeNode2> header) {
		double count = -1;
		for (TreeNode2 node : header) {
			if (node.getName().equals(itemname)) {
				count = node.getCount();
			}
		}
		return count;
	}

	/**
	   * 
	   * @param records �������ļ�¼,��I1,I2,I3
	   * @param header ��ͷ
	   * @return ���ع����õ���
	   */
	public TreeNode2 builderFpTree(LinkedList<LinkedList<String>> records,
			List<TreeNode2> header) {

		TreeNode2 root;
		if (records.size() <= 0) {
			return null;
		}
		root = new TreeNode2();
		for (LinkedList<String> items : records) {
			itemsort(items, header);
			addNode(root, items, header);
		}
		String dd = "dd";
		String test = dd;
		return root;
	}
	/**
	* 
	* @param root ���Ѿ��з�֦���ڵ�ʱ���ж������Ľڵ��Ƿ����ڸ÷�֦��ĳ���ڵ㣬��ȫ���غϣ��ݹ�
	* @param items �
	* @param header ��ͷ
	* @return TreeNode2 �������ĸ��ڵ�
*/
	public TreeNode2 addNode(TreeNode2 root, LinkedList<String> items,
			List<TreeNode2> header) {
		if (items.size() <= 0)
			return null;
		String item = items.poll();
		// ��ǰ�ڵ�ĺ��ӽڵ㲻�����ýڵ㣬��ô���ⴴ��һ֧��֧��
		TreeNode2 node = root.findChild(item);
		if (node == null) {
			node = new TreeNode2();
			node.setName(item);
			node.setCount(1.0);
			node.setParent(root);
			root.addChild(node);

			// �ӽ������ڵ�ӵ���ͷ��
			for (TreeNode2 head : header) {
				if (head.getName().equals(item)) {
					while (head.getNextHomonym() != null) {
						head = head.getNextHomonym();
					}
					head.setNextHomonym(node);
					break;
				}
			}
			// �ӽ������ڵ�ӵ���ͷ��
		} else {
			node.setCount(node.getCount() + 1);
		}

		addNode(node, items, header);
		return root;
	}
	/**
	* ��Ҷ���ҵ����ڵ㣬�ݹ�֮
	* @param node �ڵ�
	* @param newrecord �½ڵ�
	* 
	*/
	public void toroot(TreeNode2 node, LinkedList<String> newrecord) {
		if (node.getParent() == null)
			return;
		String name = node.getName();
		newrecord.add(name);
		toroot(node.getParent(), newrecord);
	}
	 /**
	* ������FP-tree��������ϣ������Ƶ���
	* @param node ���ڵ�
	* @param newrecord �洢String���͵�LinkedList����
	* @param item �
*/
	public void combineItem(TreeNode2 node, LinkedList<String> newrecord,
			String Item) {
		if (node.getParent() == null)
			return;
		String name = node.getName();
		newrecord.add(name);
		toroot(node.getParent(), newrecord);
	}
	/**
	* fp-growth
	* @param records
	* @param item
*/  
//	public void fpgrowth(LinkedList<LinkedList<String>> records, List<String> items,
//			LinkedList<TreeNode2> F1,int time ) {
//		// �����µ�����ģʽ���ĸ�����¼�������¹���FP-tree
//		String str = "";
//		LinkedList<LinkedList<String>> newrecords = new LinkedList<LinkedList<String>>();
//		// ������ͷ
//		LinkedList<TreeNode2> header = buildHeaderLink(records);
//		// ����FP-Tree
//		TreeNode2 fptree = builderFpTree(records, header);
//		int countNum = 0;
//		// �����ݹ������
//		if (header.size() <= 0 || fptree == null) {
//			//System.out.println("-----------------");
//			return;
//		}
//		// ��ӡ���,���Ƶ���
//		if (items != null) {
//			// Ѱ������ģʽ��,����β��ʼ
//			for (int i = header.size() - 1; i >= 0; i--) {
//				TreeNode2 head = header.get(i);
//				String itemname = head.getName();
//				Integer count = 0;
//				while (head.getNextHomonym() != null) {
//					head = head.getNextHomonym();
//					// Ҷ��count���ڶ��٣������������¼
//					count = count + head.getCount();
//				}
//				LinkedList<String> list = new LinkedList<String>();
//				list.add(head.getName());
//				list.addAll(items);
//				
//				//System.out.println(list);
//				List<String> list1 = itemsort(list, F1);
//				//System.out.println(list1);
//				for (int j = 0; j < list1.size() - 1; j++) {
//					String item2 = list1.get(j);
//			//		System.out.print(item2 + ",");
//					str=str+item2 + ",";
//				}
//				String item3 = list1.get(list1.size() - 1);
//				str=str+list1.get(list1.size() - 1)+"\t" + count + "\t" + time;
//		//		System.out.println(item3 + "\t" + count + "\t" + "12");  
//				this.collector.emit(new Values(str));
//				str="";
//			} 
//		}
//		// Ѱ������ģʽ��,����β��ʼ
//		for (int i = header.size() - 1; i >= 0; i--) {
//			TreeNode2 head = header.get(i);
//			List<String> newitems = new LinkedList<String>();
//			newitems.add(head.getName());
//			// �����
//			if (items != null) {
//				newitems.addAll(items);
//			} 
//
//			while (head.getNextHomonym() != null) {
//				head = head.getNextHomonym();
//				// Ҷ��count���ڶ��٣������������¼
//				Integer count = head.getCount();
//				for (int n = 0; n < count; n++) {
//					LinkedList<String> record = new LinkedList<String>();
//					toroot(head.getParent(), record);
//					newrecords.add(record);
//				}
//			}
//			// System.out.println("-----------------");
//			// �ݹ�֮,������FP-Tree
//			fpgrowth(newrecords, newitems,F1,time);
//		}
//	}

	/**
	* 
	* @param records  ��������ģʽ���ļ�¼
	* @param item  �
	* @param F1 ����ͷ
	* @return  ����SecPackData����
	*/  
	public   SecPackData fpgrowth(LinkedList<LinkedList<String>> records,
			List<String> items, LinkedList<TreeNode2> F1) {
		// �����µ�����ģʽ���ĸ�����¼�������¹���FP-tree
		LinkedList<LinkedList<String>> newrecords = new LinkedList<LinkedList<String>>();
		// ������ͷ
		LinkedList<TreeNode2> header = buildHeaderLink(records);
		// ����FP-Tree
		TreeNode2 fptree = builderFpTree(records, header);
		int countNum = 0;
		// �����ݹ������
		if (header.size() <= 0 || fptree == null) {
			// System.out.println("-----------------");
			//return null;
			return spd;
		}
		// ��ӡ���,���Ƶ���
		if (items != null) {
			// Ѱ������ģʽ��,����β��ʼ
			for (int i = header.size() - 1; i >= 0; i--) {
				TreeNode2 head = header.get(i);
				String itemname = head.getName();
				Double count = 0.0;
				while (head.getNextHomonym() != null) {
					head = head.getNextHomonym();
					// Ҷ��count���ڶ��٣������������¼
					count = count + head.getCount();
				}
				LinkedList<String> list = new LinkedList<String>();
				list.add(head.getName());
				list.addAll(items);

				// System.out.println(list);
				List<String> list1 = itemsort(list, F1);
				// System.out.println(list1);
				ArrayList list111 = new ArrayList();
				for (int j = 0; j < list1.size() - 1; j++) {
					String item2 = list1.get(j);
					// System.out.print(item2 + ",");
					list111.add(item2);
				}
				String item3 = list1.get(list1.size() - 1);

				list111.add(item3);

				// System.out.println("1111111"+list111.toString());
				Collections.sort(list111);
				// System.out.println("2222222"+list111.toString());
				// fr.setItem(list111);
				// fr.setCount(count);
				fr=new frequent(list111, count);
				spd.addItem(fr);
		
				//System.out.println(list111.toString() + "	" + count + "	"	+ "12");

			
			}
		}
		// Ѱ������ģʽ��,����β��ʼ
		for (int i = header.size() - 1; i >= 0; i--) {
			TreeNode2 head = header.get(i);
			List<String> newitems = new LinkedList<String>();
			newitems.add(head.getName());
			// �����
			if (items != null) {
				newitems.addAll(items);
			}

			while (head.getNextHomonym() != null) {
				head = head.getNextHomonym();
				// Ҷ��count���ڶ��٣������������¼
				Double count = head.getCount();
				for (int n = 0; n < count; n++) {
					LinkedList<String> record = new LinkedList<String>();
					toroot(head.getParent(), record);
					newrecords.add(record);
				}
			}
			// System.out.println("-----------------");
			// �ݹ�֮,������FP-Tree
			fpgrowth(newrecords, newitems, F1);
		}
		return spd;
	}
	/**
	* ������򣬴˲�Ҳ����ʡ�ԣ�Ϊ�˼����ټӹ�������鷳����
	* @param orderheader  ����ͷ
	* 
	*/ 
	public void orderF1(LinkedList<TreeNode2> orderheader) {
		for (int i = 0; i < orderheader.size(); i++) {
			TreeNode2 node = orderheader.get(i);
			ordermap.put(node.getName(), i);
		}

	}
	/**
	* ����������Ԫ����ӽ�Ƶ���
	* @param header  ����ͷ
	* 
	*/ 
	 public void printF1(LinkedList<TreeNode2> header) { 
//	        System.out.println("F-1 set: ");
//	        String str="";
//	        for (TreeNode2 item : header) { 	    
//	            str=item.getName() + "	" + item.getCount() + "	"+time;
//	            this.collector.emit(new Values(str));
//	            str="";
//	        } 
		 for (TreeNode2 item : header) {
				ArrayList list111 = new ArrayList();
				list111.clear();
				list111.add(item.getName());

				fr=new frequent(list111, item.getCount());
				
				spd.addItem(fr);
					
			}
	    }
	

}

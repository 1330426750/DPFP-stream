package PatternTree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import FPGrowth.frequent;

public class ModifyPatternTree extends PatternTree {
	// static FileInputStream fis;
	// static InputStreamReader isr;
	// static BufferedReader br;
	static PatternTreeNode node;
	/**
	 * 
	 * 合并中间结果，对模式树进行更新
	 * @param afr  候选频繁项集
	 * @param count 频繁项集的count值
	 * @param time	时间戳
	 * @param tree  模式树
	 */
	public static void modify(ArrayList<frequent> afr, double count,String time, PatternTree tree) {
		int i;
		// String str = "";

	//	tree.setChangeTag(1);// 时间窗口发生改变
		
		// List delList =new ArrayList();
		for (frequent fr : afr) 
		{
			// System.out.println(fr);
			ArrayList<String> item = fr.getItem();
			double value = fr.getCount();
			// System.out.println("value"+value);
			String item1[] = new String[item.size()];
			node = tree.getRoot();
			for (i = 0; i < item.size(); i++) 
			{
				item1[i] = item.get(i);
			}
			for (i = 0; i < item.size() - 1; i++)//如果没有这些节点则插入
			{
				if (hasChild(node, item1[i], value) == 0)
				{
					insertNode(tree, item1[i], node, -1, Integer.parseInt(time));
				}
				node = change(node, item1[i]);
			}
			if (compare(node, item1[item.size() - 1]) == 0)
			{
				insertNode(tree, item1[i], node, value, Integer.parseInt(time));
			} else {
				node = change(node, item1[i]);
				// System.out.println("value+"+value);
				modifyNode(node, value, Integer.parseInt(time), tree);
				// String str1= tree.iteratorTree(tree.getRoot(), tree);
				// System.out.println("str1.."+str1);
			}

			// for(i = 0;i < item.size()-1; i ++)
			// {
			// System.out.println(item.get(i));
			// if (hasChild(node,item.get(i),value)==0)//所指节点的孩子结点中,没有名字为item的节点
			// {
			// insertNode(tree, item.get(i), node, -1, Integer.parseInt(time));
			// }
			// node=change(node,item.get(i));//返回所指节点node的孩子节点中，名字为item.get(i)的节点
			// }
			// if(compare(node,item.get(item.size()-1))==0)//节点node的孩子节点中，若没有名字为item的节点
			// {
			// insertNode(tree, item.get(item.size()-1), node,
			// value,Integer.parseInt(time));
			// }
			// else
			// {
			// node=change(node,item.get(item.size()-1));
			// modifyNode(node, value,Integer.parseInt(time), tree);
			// }

		}

		tree.setPresentTimeWindow(Integer.parseInt(time));
//		tree.insert1secTupleNum(0, count);
//	 	modifyTree(tree,count, Integer.parseInt(time)+1);
		
		
		//修改count的滑动窗口
		System.out.println("count"+count);
//		modifyCount(count, Integer.parseInt(time), tree);
		
		// String []s = first.split(",");
		// StringTokenizer token2 = new StringTokenizer(first,",");
		// String item[]= new String[s.length];
		// node=tree.getRoot();
		// double value=Double.parseDouble(second);
		// for(i=0;i<s.length;i++)
		// item[i]= token2.nextToken();
		// for(i=0;i<s.length-1;i++)
		// {
		// if (hasChild(node,item[i],value)==0)
		// {
		// insertNode(tree, item[i], node, -1, Integer.parseInt(third));
		// }
		// node=change(node,item[i]);
		// }
		// if(compare(node,item[s.length-1])==0){
		// insertNode(tree, item[i], node, value,Integer.parseInt(third));
		// }
		// else
		// {
		// node=change(node,item[i]);
		// modifyNode(node, value,Integer.parseInt(third), tree);
		// }

		// setZero(tree.getRoot(),tree.getPresentTimeWindow());
		// 设置最小支持度与最大支持度误差
		// deleteNode(tree.getRoot(),tree);
		// System.out.println(tree.iteratorTree(tree.getRoot(),tree));
	}

	/**
	 * 
	 * 1s记录总数count滑动时间窗口
	 * @param value传入的频繁项集的count
	 * @param timeWindow时间窗口
	 * @param tree树
	 */
	public static void modifyCount(double value,int timeWindow, PatternTree tree) {
		double temp1, temp2, temp3;
		// if (tree.getPresentTimeWindow()==timeWindow &&
		// tree.getChangeTag()==1)
		if (tree.getChangeTag() == 1)// 返回时间窗口是否发生改变
//		 if (tree.getPresentTimeWindow()!=timeWindow)
		{
			// System.out.println("11111111111");
		
		
		//	tree.setPresentTimeWindow(timeWindow);
		
			
			
			if (tree.num1secTupleNum() == 5) //1s的数组满
			{
				temp1 = 0;
				
				for (int i = 0; i < 5; i++) {
					temp1 = temp1 + tree.get1secTupleNum(i);//1s的数组数据相加（1*5S）

					if (i < 4) {//将最近4s传值到mid数组，用于查询（缓存数组）
						tree.insert1secMidTupleNum(i, tree.get1secTupleNum(i));
						// TODO
					}
				
				}
				tree.init1secTupleNum();
				tree.insert1secTupleNum(0, value);//给1*5s的窗口的第一个位置传入频繁项集的个数
				if (tree.num5secTupleNum() == 6) { //5s的数组满
					temp2 = 0;
					for (int i = 0; i < 6; i++) {
						temp2 = temp2 + tree.get5secTupleNum(i);
						if (i < 5)
							tree.insert5secMidTupleNum(i, tree.get5minTupleNum(i));
					}
					tree.init5secTupleNum();
					tree.insert5secTupleNum(0, temp1);//将1*5s数据之和插入到5s窗口的第一个位置
					
					if (tree.num30secTupleNum() == 10) { //30s的数组满
						temp3 = 0;
						for (int i = 0; i < 10; i++) {
							temp3 = temp3 + tree.get30secTupleNum(i);
							if (i < 9)
								tree.insert30secMidTupleNum(i, tree.get30secTupleNum(i));
						}
						tree.init30secTupleNum();
						tree.insert30secTupleNum(0, temp2);
						
						if (tree.num5minTupleNum() == 6)//5min的数组满，多余数据抛弃，不存在往下一个传递值的过程
						{
							for (int i = 4; i > 0; i--) {//缓存窗口滑动
								tree.insert5minMidTupleNum(i, tree.get5minMidTupleNum(i-1));
							}
							tree.insert5minMidTupleNum(0, tree.get5minTupleNum(5));
							for (int i = 5; i > 0; i--) {//6*5min窗口滑动
								tree.insert5minTupleNum(i, tree.get5minTupleNum(i-1));
							}
							tree.insert5minTupleNum(0, temp3);
						} 
						else {//6*5min窗口未满，新数据传入，窗口滑动
							for (int i = tree.num5minTupleNum(); i > 0; i--)
								tree.insert5minTupleNum(i, tree.get5minTupleNum(i-1));
							tree.insert5minTupleNum(0, temp3);
						}
					} 
					else {//10*30s窗口未满，新数据传入，窗口滑动
						for (int i = tree.num30secTupleNum(); i > 0; i--)
							tree.insert30secTupleNum(i, tree.get30secTupleNum(i-1));
						tree.insert30secTupleNum(0, temp2);
					}
				} else {//6*5s窗口填满的过程
					for (int i = tree.num5secTupleNum(); i > 0; i--)
						tree.insert5secTupleNum(i, tree.get5secTupleNum(i-1));
					tree.insert5secTupleNum(0, temp1);
				}
			} else {//1*5s未满，窗口已有数据往后滑动，刚开始插入新数据
				int j = tree.num1secTupleNum();
				for (int i = j; i > 0; i--) {
					tree.insert1secTupleNum(i, tree.get1secTupleNum(i - 1));
				}
				
				tree.insert1secTupleNum(0, value);
			//	System.out.println("count!!!!"+tree.get1secTupleNum(0));  100
				//System.out.println("count!!!!"+tree.get1secTupleNum(1));  200(第二秒数据插入，窗口滑动，数据出错)
				
			}
			
			
			
			tree.setPresentTimeWindow(timeWindow);		
		} 
		else if (tree.getPresentTimeWindow() == timeWindow)
			{
				if (tree.get1secTupleNum(0) == -1)
					tree.insert1secTupleNum(0, value);
				else
					tree.insert1secTupleNum(0, tree.get1secTupleNum(0) + value);
				
			}
		else if ((tree.getPresentTimeWindow() - timeWindow) % 100 > 0&& (tree.getPresentTimeWindow() - timeWindow) % 100 < 5) 
			{
				int loc;
				loc = (tree.getPresentTimeWindow() - timeWindow) % 100;
				if (loc < tree.num1secTupleNum())
					tree.insert1secTupleNum(loc, tree.get1secTupleNum(loc) + value);
				else 
				{
					tree.insert1secMidTupleNum(loc - 1, tree.get1secMidTupleNum(loc - 1)+ value);
					tree.insert5secTupleNum(0, tree.get5secTupleNum(0) + value);
				}
		}
	}
	
	
	/**
	 * 获取指定节点的孩子节点中，名字为item的节点
	 * @param node1  模式树节点
	 * @param item  项名称
	 * @return 返回所指节点node1的孩子节点中，名字为item的节点
	 */
	public static PatternTreeNode change(PatternTreeNode node1, String item) {
		PatternTreeNode node2 = null;
		for (PatternTreeNode index : node1.getChildList()) {
			if (index.getNodeId().equals(item)) {
				node2 = index;
			}
		}
		return node2;
	}

	/**
	 * 在指定节点的孩子节点中，若有名字为item的节点则返回1，否则返回0
	 * @param node1  模式树节点
	 * @param item   项名称
	 * @return 所指节点node1的孩子节点中，若有名字为item的节点则返回1，否则返回0
	 */
	public static int compare(PatternTreeNode node1, String item) {
		for (PatternTreeNode index : node1.getChildList()) {
			if (index.getNodeId().equals(item)) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 修改节点，插入新的时间窗口值
	 * 滑动窗口
	 * @param ptnode 节点
	 * @param value （频繁项集）节点的个数
	 * @param timeWindow  时间窗口
	 * @param tree  模式树
	 */
	public static void modifyNode(PatternTreeNode ptnode, double value,int timeWindow, PatternTree tree) {
		double temp1, temp2, temp3;
		// if (tree.getPresentTimeWindow()==timeWindow &&
		// tree.getChangeTag()==1)
		if (tree.getChangeTag() == 1)// 返回时间窗口是否发生改变
//		if (tree.getPresentTimeWindow()!=timeWindow)//时间窗口发生变换
		{
			// System.out.println("11111111111");
			ptnode.setCreateTag(1);
			ptnode.setPresentTimeWindow(timeWindow);
			ptnode.setRemoveTag(0);
			
			
			
			if (ptnode.num1secWindow() == 5) //1s的数组满
			{
				temp1 = 0;
				for (int i = 0; i < 5; i++) {
					temp1 = temp1 + ptnode.get1secWindow(i);//1s的数组数据相加（1*5S）

					if (i < 4) {//将最近4s传值到mid数组，用于查询（缓存数组）
						ptnode.insert1secMidWindow(i, ptnode.get1secWindow(i));
						// TODO
					}
				}
				ptnode.init1secWindow();
				ptnode.insert1secWindow(0, value);//给1*5s的窗口的第一个位置传入频繁项集的个数
				if (ptnode.num5secWindow() == 6) { //5s的数组满
					temp2 = 0;
					for (int i = 0; i < 6; i++) {
						temp2 = temp2 + ptnode.get5secWindow(i);
						if (i < 5)
							ptnode.insert5secMidWindow(i, ptnode.get5secWindow(i));
					}
					ptnode.init5secWindow();
					ptnode.insert5secWindow(0, temp1);//将1*5s数据之和插入到5s窗口的第一个位置
					
					if (ptnode.num30secWindow() == 10) { //30s的数组满
						temp3 = 0;
						for (int i = 0; i < 10; i++) {
							temp3 = temp3 + ptnode.get30secWindow(i);
							if (i < 9)
								ptnode.insert30secMidWindow(i, ptnode.get30secWindow(i));
						}
						ptnode.init30secWindow();
						ptnode.insert30secWindow(0, temp2);
						
						if (ptnode.num5minWindow() == 6)//5min的数组满，多余数据抛弃，不存在往下一个传递值的过程
						{
							for (int i = 4; i > 0; i--) {//缓存窗口滑动
								ptnode.insert5minMidWindow(i, ptnode.get5minMidWindow(i - 1));
							}
							ptnode.insert5minMidWindow(0, ptnode.get5minWindow(5));
							for (int i = 5; i > 0; i--) {//6*5min窗口滑动
								ptnode.insert5minWindow(i, ptnode.get5minWindow(i - 1));
							}
							ptnode.insert5minWindow(0, temp3);
						} 
						else {//6*5min窗口未满，新数据传入，窗口滑动
							for (int i = ptnode.num5minWindow(); i > 0; i--)
								ptnode.insert5minWindow(i, ptnode
										.get5minWindow(i - 1));
							ptnode.insert5minWindow(0, temp3);
						}
					} 
					else {//10*30s窗口未满，新数据传入，窗口滑动
						for (int i = ptnode.num30secWindow(); i > 0; i--)
							ptnode.insert30secWindow(i, ptnode
									.get30secWindow(i - 1));
						ptnode.insert30secWindow(0, temp2);
					}
				} else {//6*5s窗口填满的过程
					for (int i = ptnode.num5secWindow(); i > 0; i--)
						ptnode.insert5secWindow(i, ptnode.get5secWindow(i - 1));
					ptnode.insert5secWindow(0, temp1);
				}
			} else {//1*5s未满，窗口已有数据往后滑动，刚开始插入新数据
				int j = ptnode.num1secWindow();
				for (int i = j; i > 0; i--) {
					ptnode.insert1secWindow(i, ptnode.get1secWindow(i - 1));
				}
				ptnode.insert1secWindow(0, value);
			}
			
			
			
		tree.setPresentTimeWindow(timeWindow);	
		} 
		else if (tree.getPresentTimeWindow() == timeWindow)
			{
				if (ptnode.get1secWindow(0) == -1)
					ptnode.insert1secWindow(0, value);
				else
					ptnode.insert1secWindow(0, ptnode.get1secWindow(0) + value);
			}
		else if ((tree.getPresentTimeWindow() - timeWindow) % 100 > 0&& (tree.getPresentTimeWindow() - timeWindow) % 100 < 5) 
			{
				int loc;
				loc = (tree.getPresentTimeWindow() - timeWindow) % 100;
				if (loc < ptnode.num1secWindow())
					ptnode.insert1secWindow(loc, ptnode.get1secWindow(loc) + value);
				else 
				{
				ptnode.insert1secMidWindow(loc - 1, ptnode
						.get1secMidWindow(loc - 1)
						+ value);
				ptnode.insert5secWindow(0, ptnode.get5secWindow(0) + value);
				}
		}
	}

	/**
	 * 修改PatternTree的时间窗口
	 * 
	 * @param tree  模式树
	 * @param value  频繁项集节点个数
	 * @param timeWindow  时间窗口
	 */
	public static void modifyTree(PatternTree tree, double value, int timeWindow) {
		double temp1, temp2, temp3;
		if (tree.getPresentTimeWindow() - timeWindow == -1) {
			tree.setChangeTag(1);
			tree.setPresentTimeWindow(timeWindow);
			if (tree.num1secTupleNum() == 5) {
				temp1 = 0;
				for (int i = 0; i < 5; i++) {
					temp1 = temp1 + tree.get1secTupleNum(i);
					// System.out.println(tree.get1secTupleNum(i));
					if (i < 4)
						tree.insert1secMidTupleNum(i, tree.get1secTupleNum(i));
				}
				// System.out.println(tree.get1secMidTupleNum(0));
				tree.init1secTupleNum();
				tree.insert1secTupleNum(0, value);
				if (tree.num5secTupleNum() == 6) {
					temp2 = 0;
					for (int i = 0; i < 6; i++) {
						temp2 = temp2 + tree.get5secTupleNum(i);
						if (i < 5)
							tree.insert5secMidTupleNum(i, tree
									.get5secTupleNum(i));
					}
					tree.init5secTupleNum();
					tree.insert5secTupleNum(0, temp1);
					if (tree.num30secTupleNum() == 10) {
						temp3 = 0;
						for (int i = 0; i < 10; i++) {
							temp3 = temp3 + tree.get30secTupleNum(i);
							if (i < 9)
								tree.insert30secMidTupleNum(i - 1, tree
										.get30secTupleNum(i));
						}
						tree.init30secTupleNum();
						tree.insert30secTupleNum(0, temp2);
						
						if (tree.num5minTupleNum() == 6) {//5min满了
							for (int i = 4; i > 0; i--) {
								tree.insert5minMidTupleNum(i, tree
										.get5minMidTupleNum(i - 1));
							}
							tree.insert5minMidTupleNum(0, tree
									.get5minTupleNum(5));
							for (int i = 5; i > 0; i--) {
								tree.insert5minTupleNum(i, tree
										.get5minTupleNum(i - 1));
							}
							tree.insert5minTupleNum(0, temp3);
						} else {//滑动
							for (int i = tree.num5minTupleNum(); i > 0; i--)
								tree.insert5minTupleNum(i, tree
										.get5minTupleNum(i - 1));
							tree.insert5minTupleNum(0, temp3);
						}
						
					} else {
						for (int i = tree.num30secTupleNum(); i > 0; i--)
							tree.insert30secTupleNum(i, tree
									.get30secTupleNum(i - 1));
						tree.insert30secTupleNum(0, temp2);
					}
				} else {
					for (int i = tree.num5secTupleNum(); i > 0; i--)
						tree.insert5secTupleNum(i, tree.get5secTupleNum(i - 1));
					tree.insert5secTupleNum(0, temp1);
				}
			} else {
				for (int i = tree.num1secTupleNum(); i > 0; i--) {
					tree.insert1secTupleNum(i, tree.get1secTupleNum(i - 1));
				}
				tree.insert1secTupleNum(0, value);
			}
		} 
		
//		else if ((tree.getPresentTimeWindow() - timeWindow) % 100 >= 0&& (tree.getPresentTimeWindow() - timeWindow) % 100 < 5)
//		{
//			tree.setChangeTag(0);
//			int loc = (tree.getPresentTimeWindow() - timeWindow) % 100;
//			if (loc < tree.num1secTupleNum())
//				tree.insert1secTupleNum(loc, tree.get1secTupleNum(loc) + value);
//			else 
//			{
//				tree.insert1secMidTupleNum(loc - 1, tree
//						.get1secMidTupleNum(loc - 1)
//						+ value);
//				tree.insert5secTupleNum(0, tree.get5secTupleNum(0) + value);
//			}
//		}

	}

	/**
	 * 对没进行修改的节点插入0，并修改其当前时间窗口与PatternTree一致
	 * 
	 * @param node  将要修改的模式树节点
	 * @param timeWindow  当前时间窗口
	 * @param  tree 模式树
	 */
	public static void setZero(PatternTreeNode node, int timeWindow,
			PatternTree tree) {
		node.setRemoveTag(0);
		if (node.getPresentTimeWindow() != timeWindow) {
			modifyNode(node, 0, timeWindow, tree);
		}
		if (node.getChildList() != null) {
			for (PatternTreeNode index : node.getChildList())
				setZero(index, timeWindow, tree);
		}
	}

	/**
	 * 删除不符合PatternTree要求的节点（PatternTree的剪枝）
	 * 
	 * @param node  将要修改的模式树节点
	 * @param tree  模式树
	 */
	public static void deleteNode(PatternTreeNode node, PatternTree tree) {
		node.setRemoveTag(1);
		if (node.getChildList() != null) {
			for (int i = 0; i < node.getChildList().size(); i++) {
				node.getChildList().get(i);
				if (lessThanSupport(node.getChildList().get(i), tree) == 0) {
					if (node.getChildList().get(i).getRemoveTag() == 0)
						deleteNode(node.getChildList().get(i), tree);
				}

				else {
					node.getChildList().remove(node.getChildList().get(i));
					deleteNode(node, tree);
				}
			}

		}
	}

	/**
	 * 在指定节点的孩子结点中,若有名字为str的节点，返回1；否则返回0
	 * @param ptnode  指定的模式树节点
	 * @param str  节点的名字
	 * @param value 所指节点的孩子结点中,若有名字为str的节点，对其第1s的时间窗口插入value
	 * @return 所指节点的孩子结点中,是否有名字为str的节点，若有，返回1；没有返回0.
	 */
	public static int hasChild(PatternTreeNode ptnode, String str, double value) {
		if (ptnode.getChildList() != null) {
			for (PatternTreeNode index : ptnode.getChildList()) {
				if (index.getNodeId().equals(str)) {
					// TODO
					// if (index.getCreateTag()!=1)
					// {
					// index.insert1secWindow(0,value);
					// index.setCreateTag(1);
					// }
					return 1;
				}
			}
		}
		return 0;
	}
	/**
	 * 判断指定的节点和模式树，假如满足设定的最小支持度和最大误差度时，返回1，否则返回0
	 * @param node  指定的模式树节点
	 * @param tree  模式树
	 * 
	 * @return 满足最小支持度和最大误差度时，返回1，否则返回0
	 */
	public static int lessThanSupport(PatternTreeNode node, PatternTree tree) {
		double num1, num2;
		num1 = 0;
		num2 = 0;

		for (int i = 0; i < node.num1secWindow(); i++) {
			if (node.get1secWindow(i) >= (tree.getSupport() * tree
					.get1secTupleNum(i)))
				return 0;
			num1 = num1 + node.get1secWindow(i);
			num2 = num2 + tree.get1secTupleNum(i);
		}

		if (num1 >= (tree.getMistakeRate() * num2))
			return 0;

		return 1;
	}
	/*
	 * public static void main(String[] args) { PatternTree tree = new
	 * PatternTree(); modify(tree); }
	 */

}
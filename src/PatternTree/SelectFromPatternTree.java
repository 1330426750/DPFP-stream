package PatternTree;

public class SelectFromPatternTree extends PatternTree{
	/**
	 * ��������Ĳ�ѯʱ�䣬���ò�ͬ�ĺ������õ���ѯ�Ľ�� 
	 * 
	 * @param tree   ģʽ��
	 * @param time_sec  ��ѯʱ��
	 * @return  ����ָ���ڵ㼰���ӽڵ�����Ƶ���
	 */
	public static String selectFromPatternTree(PatternTree tree, int time_sec)
	{
		String str;
		str=null;
		tree.setSelectTag(tree.getSelectTag()+1);
		if (time_sec<=5)
		{
			//����ʱ��Ϊ5�����ڣ�����Ӧ�ĺ���
			str=selectNode_Minus5sec(tree.getRoot(),tree,time_sec);  
		}
		
		else if (time_sec<=30)
		{
			 //����ʱ��Ϊ5~30�����ڣ�5��ı�����������Ӧ�ĺ���
			str=selectNode_Minus30sec(tree.getRoot(),tree,time_sec); 
		}
		
		else if (time_sec<=300)
		{
			//����ʱ��Ϊ30��~5�����ڣ�30��ı�����������Ӧ�ĺ���
			str=selectNode_Minus5min(tree.getRoot(),tree,time_sec);   
		}
		
		else if (time_sec<=1800)
		{
			//����ʱ��Ϊ5~30�����ڣ�5�ֵı�����������Ӧ�ĺ���
			str=selectNode_Minus30min(tree.getRoot(),tree,time_sec);  
		}
		
		else
			//��ѯʱ�䲻���϶���
			System.out.println("Wrong select time!");             
		return str;
    }
	/**
	 * 
	 * �����5s��ָ���ڵ��������Ƿ�������С֧�ֶȣ��������㣬��ô����Ƶ���
	 * ������Ƶ���
	 * @param ptnode   ָ���ڵ�
	 * @param tree   ģʽ��
	 * @param time_sec  ��ѯʱ��
	 * @return  ������С֧�ֶȾͷ���1�����򷵻�0
	 */
	public static int selectMinus5sec(PatternTreeNode ptnode, PatternTree tree, int time_sec)
	{
		int i;
		double num1,num2;
		num1=0;
		num2=0;
		//����Ҫ��ѯ��time_secС�ڻ��ߵ��ڵ�ǰ�ڵ���бʱ�䴰���в�Ϊnull����Ŀ
		//��ô�ʹӵ�ǰ�ڵ����бʱ�䴰����ȡֵ
		if(ptnode.num1secWindow()>=time_sec)
		{
			for(i=0;i<time_sec;i++)
			{
				num1=num1+ptnode.get1secWindow(i);
				num2=num2+tree.get1secTupleNum(i);
			}
		}
		else  //Ҫ��ѯ��time_sec���ڵ�ǰ�ڵ���бʱ�䴰���в�Ϊnull����Ŀ
		{
			//�ӵ�ǰ��бʱ�䴰����ȡ��ȫ����null��ֵ
			for(i=0;i<ptnode.num1secWindow();i++)
			{
				num1=num1+ptnode.get1secWindow(i);
				num2=num2+tree.get1secTupleNum(i);
			}
			//Ȼ���ٴ���бʱ�䴰�ڻ�����ȡ��ȱ�ٵĲ���
			for(i=0;i<time_sec-ptnode.num1secWindow();i++)
			{
				num1=num1+ptnode.get1secMidWindow(i);
				num2=num2+tree.get1secMidTupleNum(i);
			}
		}
		//������С֧�ֶ�ʱ������1
		if(num1>=(tree.getSupport()*num2))
			return 1;
		return 0;
	}
	/**
	 * 
	 * �����30s��ָ���ڵ��������Ƿ�������С֧�ֶȣ��������㣬��ô����Ƶ���
	 * ������Ƶ���
	 * @param ptnode   ָ���ڵ�
	 * @param tree   ģʽ��
	 * @param time_sec  ��ѯʱ��
	 * @return  ������С֧�ֶȾͷ���1�����򷵻�0
	 */
	public static int selectMinus30sec(PatternTreeNode ptnode, PatternTree tree, int time_sec)
	{
		int i;
		double num1,num2;
		num1=0;
		num2=0;
		time_sec=time_sec/5;
		//����Ҫ��ѯ��time_secС�ڻ��ߵ��ڵ�ǰ�ڵ���бʱ�䴰���в�Ϊnull����Ŀ
		//��ô�ʹӵ�ǰ�ڵ����бʱ�䴰����ȡֵ
		if(ptnode.num5secWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec;i++)
	    	{
	    		num1=num1+ptnode.get5secWindow(i);
	     		num2=num2+tree.get5secTupleNum(i);
	    	}
    	}
    	else   //Ҫ��ѯ��time_sec���ڵ�ǰ�ڵ���бʱ�䴰���в�Ϊnull����Ŀ
    	{
			//�ӵ�ǰ��бʱ�䴰����ȡ��ȫ����null��ֵ
	    	for(i=0;i<ptnode.num5secWindow();i++)
	    	{
		    	num1=num1+ptnode.get5secWindow(i);
		    	num2=num2+tree.get5secTupleNum(i);
	    	}
			//Ȼ���ٴ���бʱ�䴰�ڻ�����ȡ��ȱ�ٵĲ���
	    	for(i=0;i<time_sec-ptnode.num5secWindow();i++)
		    {
	    		num1=num1+ptnode.get5secMidWindow(i);
		    	num2=num2+tree.get5secMidTupleNum(i);
	    	}
    	}
/*				
		if(ptnode.num1secWindow()<3)
		{
			if(ptnode.num5secWindow()<time_sec)
			{
		        num1=num1+ptnode.get5secMidWindow(time_sec-1-ptnode.num5secWindow());
		        num2=num2+tree.get5secMidTupleNum(time_sec-1-ptnode.num5secWindow());
			}
			else
			{
				num1=num1+ptnode.get5secWindow(time_sec-1);
				num2=num2+tree.get5secTupleNum(time_sec-1);
			}
		}		
		else
		{
			num1=num1+ptnode.get5secSupport();
			num2=num2+tree.get5secTupleNum();			
		}
		*/
		//������С֧�ֶ�ʱ������1
		if(num1>=(tree.getSupport()*num2))
			return 1;
		return 0;
	}
	/**
	 * 
	 * �����300s��ָ���ڵ��������Ƿ�������С֧�ֶȣ��������㣬��ô����Ƶ���
	 * ������Ƶ���
	 * @param ptnode   ָ���ڵ�
	 * @param tree   ģʽ��
	 * @param time_sec  ��ѯʱ��
	 * @return  ������С֧�ֶȾͷ���1�����򷵻�0
	 */
	public static int selectMinus5min(PatternTreeNode ptnode, PatternTree tree, int time_sec)
	{
		int i;
		double num1,num2;
		num1=0;
		num2=0;
		time_sec=time_sec/30;
		//����Ҫ��ѯ��time_secС�ڻ��ߵ��ڵ�ǰ�ڵ���бʱ�䴰���в�Ϊnull����Ŀ
		//��ô�ʹӵ�ǰ�ڵ����бʱ�䴰����ȡֵ
		if(ptnode.num30secWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec-1;i++)
	    	{
	    		num1=num1+ptnode.get30secWindow(i);
	     		num2=num2+tree.get30secTupleNum(i);
	    	}
    	}
    	else//Ҫ��ѯ��time_sec���ڵ�ǰ�ڵ���бʱ�䴰���в�Ϊnull����Ŀ
    	{
			//�ӵ�ǰ��бʱ�䴰����ȡ��ȫ����null��ֵ
	    	for(i=0;i<ptnode.num30secWindow();i++)
	    	{
		    	num1=num1+ptnode.get30secWindow(i);
		    	num2=num2+tree.get30secTupleNum(i);
	    	}
			//Ȼ���ٴ���бʱ�䴰�ڻ�����ȡ��ȱ�ٵĲ���
	    	for(i=0;i<time_sec-ptnode.num30secWindow();i++)
		    {
	    		num1=num1+ptnode.get30secMidWindow(i);
		    	num2=num2+tree.get30secMidTupleNum(i);
	    	}
    	}
				
		if(ptnode.num5secWindow()<3)
		{
			if(ptnode.num30secWindow()<time_sec)
			{
		        num1=num1+ptnode.get30secMidWindow(time_sec-1-ptnode.num30secWindow());
		        num2=num2+tree.get30secMidTupleNum(time_sec-1-ptnode.num30secWindow());
			}
			else
			{
				num1=num1+ptnode.get30secWindow(time_sec-1);
				num2=num2+tree.get30secTupleNum(time_sec-1);
			}
		}		
		else
		{
			num1=num1+ptnode.get30secSupport();
			num2=num2+tree.get30secTupleNum();			
		}
		//������С֧�ֶ�ʱ������1
		if(num1>=(tree.getSupport()*num2))
			return 1;
		return 0;
	}
	/**
	 * 
	 * �����1800s��ָ���ڵ��������Ƿ�������С֧�ֶȣ��������㣬��ô����Ƶ���
	 * ������Ƶ���
	 * @param ptnode   ָ���ڵ�
	 * @param tree   ģʽ��
	 * @param time_sec  ��ѯʱ��
	 * @return  ������С֧�ֶȾͷ���1�����򷵻�0
	 */
	public static int selectMinus30min(PatternTreeNode ptnode, PatternTree tree, int time_sec)
	{
		int i;
		double num1,num2;
		num1=0;
		num2=0;
		time_sec=time_sec/300;
		//����Ҫ��ѯ��time_secС�ڻ��ߵ��ڵ�ǰ�ڵ���бʱ�䴰���в�Ϊnull����Ŀ
		//��ô�ʹӵ�ǰ�ڵ����бʱ�䴰����ȡֵ
		if(ptnode.num5minWindow()>=time_sec)
    	{
	    	for(i=0;i<time_sec-1;i++)
	    	{
	    		num1=num1+ptnode.get5minWindow(i);
	     		num2=num2+tree.get5minTupleNum(i);
	    	}
    	}
    	else//Ҫ��ѯ��time_sec���ڵ�ǰ�ڵ���бʱ�䴰���в�Ϊnull����Ŀ
    	{
			//�ӵ�ǰ��бʱ�䴰����ȡ��ȫ����null��ֵ
	    	for(i=0;i<ptnode.num5minWindow();i++)
	    	{
		    	num1=num1+ptnode.get5minWindow(i);
		    	num2=num2+tree.get5minTupleNum(i);
	    	}
			//Ȼ���ٴ���бʱ�䴰�ڻ�����ȡ��ȱ�ٵĲ���
	    	for(i=0;i<time_sec-ptnode.num5minWindow();i++)
		    {
	    		num1=num1+ptnode.get5minMidWindow(i);
		    	num2=num2+tree.get5minMidTupleNum(i);
	    	}
    	}
				
		if(ptnode.num30secWindow()<3)
		{
			if(ptnode.num5minWindow()<time_sec)
			{
		        num1=num1+ptnode.get5minMidWindow(time_sec-1-ptnode.num5minWindow());
		        num2=num2+tree.get5minMidTupleNum(time_sec-1-ptnode.num5minWindow());
			}
			else
			{
				num1=num1+ptnode.get5minWindow(time_sec-1);
				num2=num2+tree.get5minTupleNum(time_sec-1);
			}
		}		
		else
		{
			num1=num1+ptnode.get5minSupport();
			num2=num2+tree.get5minTupleNum();			
		}
		//������С֧�ֶ�ʱ������1
		if(num1>=(tree.getSupport()*num2))
			return 1;
		return 0;
	}
	/** 
	 * ��ѯʱ����5���ڣ�ָ���ڵ㼰���ӽڵ�����Ƶ���
	 * @param node ָ���Ľڵ�
	 * @param tree ģʽ��
	 * @param time_sec ��ѯ��ʱ��
	 * @return  ����ָ����node�ڵ㼰���ӽڵ�����Ƶ���
	 */
	public static String selectNode_Minus5sec(PatternTreeNode node, PatternTree tree, int time_sec)
	{
		StringBuilder buffer = new StringBuilder();
		node.setSelectTag(tree.getSelectTag());
		if(node.getChildList()!=null)
		{
			for (int i=0;i<node.getChildList().size();i++)
			{
				node.getChildList().get(i);
				if (node.getChildList().get(i).getSelectTag()!=tree.getSelectTag())
				{
					//������С֧�ֶ�ʱ
					if(selectMinus5sec(node.getChildList().get(i),tree,time_sec)==1)
					{
						//����PatternTree.java�еľ�̬����outPutItemSet,�书����
						//����ڵ�����Ƶ���
						buffer.append(outPutItemSet(node.getChildList().get(i), tree.getRoot())+"\t");
						//������ѯnode�ĺ��ӽڵ��Ƶ���
						buffer.append(selectNode_Minus5sec(node.getChildList().get(i), tree, time_sec));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return buffer.toString();
	}
	/** 
	 * ��ѯʱ����30���ڣ�ָ���ڵ㼰���ӽڵ�����Ƶ���
	 * @param node ָ���Ľڵ�
	 * @param tree ģʽ��
	 * @param time_sec ��ѯ��ʱ��
	 * @return  ����ָ����node�ڵ㼰���ӽڵ�����Ƶ���
	 */
	public static String selectNode_Minus30sec(PatternTreeNode node, PatternTree tree, int time_sec)
	{
		StringBuilder buffer = new StringBuilder();
		node.setSelectTag(tree.getSelectTag());
		if(node.getChildList()!=null)
		{
			for (int i=0;i<node.getChildList().size();i++)
			{
				node.getChildList().get(i);
				if (node.getChildList().get(i).getSelectTag()!=tree.getSelectTag())
				{
					//������С֧�ֶ�ʱ
					if(selectMinus30sec(node.getChildList().get(i),tree,time_sec)==1)
					{
						//����PatternTree.java�еľ�̬����outPutItemSet,�书����
						//����ڵ�����Ƶ���
						buffer.append(outPutItemSet(node.getChildList().get(i), tree.getRoot())+"\t");
						//������ѯnode�ĺ��ӽڵ��Ƶ���
						buffer.append(selectNode_Minus30sec(node.getChildList().get(i), tree, time_sec));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return buffer.toString();
	}
	/** 
	 * ��ѯʱ����300���ڣ�ָ���ڵ㼰���ӽڵ�����Ƶ���
	 * @param node ָ���Ľڵ�
	 * @param tree ģʽ��
	 * @param time_sec ��ѯ��ʱ��
	 * @return  ����ָ����node�ڵ㼰���ӽڵ�����Ƶ���
	 */
	public static String selectNode_Minus5min(PatternTreeNode node, PatternTree tree, int time_sec)
	{
		StringBuilder buffer = new StringBuilder();
		node.setSelectTag(tree.getSelectTag());
		if(node.getChildList()!=null)
		{
			for (int i=0;i<node.getChildList().size();i++)
			{
				node.getChildList().get(i);
				if (node.getChildList().get(i).getSelectTag()!=tree.getSelectTag())
				{
					//������С֧�ֶ�ʱ
					if(selectMinus5min(node.getChildList().get(i),tree,time_sec)==1)
					{
						
						//����PatternTree.java�еľ�̬����outPutItemSet,�书����
						//����ڵ�����Ƶ���
						buffer.append(outPutItemSet(node.getChildList().get(i), tree.getRoot())+"\t");
						//������ѯnode�ĺ��ӽڵ��Ƶ���
						buffer.append(selectNode_Minus5min(node.getChildList().get(i), tree, time_sec));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return buffer.toString();
	}
	/** 
	 * ��ѯʱ����1800���ڣ�ָ���ڵ㼰���ӽڵ�����Ƶ���
	 * @param node ָ���Ľڵ�
	 * @param tree ģʽ��
	 * @param time_sec ��ѯ��ʱ��
	 * @return  ����ָ����node�ڵ㼰���ӽڵ�����Ƶ���
	 */
	public static String selectNode_Minus30min(PatternTreeNode node, PatternTree tree, int time_sec)
	{
		StringBuilder buffer = new StringBuilder();
		node.setSelectTag(tree.getSelectTag());
		if(node.getChildList()!=null)
		{
			for (int i=0;i<node.getChildList().size();i++)
			{
				node.getChildList().get(i);
				if (node.getChildList().get(i).getSelectTag()!=tree.getSelectTag())
				{
					//������С֧�ֶ�ʱ
					if(selectMinus30min(node.getChildList().get(i),tree,time_sec)==1)
					{
						//����PatternTree.java�еľ�̬����outPutItemSet,�书����
						//����ڵ�����Ƶ���
						buffer.append(outPutItemSet(node.getChildList().get(i), tree.getRoot())+"\t");
						//������ѯnode�ĺ��ӽڵ��Ƶ���
						buffer.append(selectNode_Minus30min(node.getChildList().get(i), tree, time_sec));
					}
					else
						node.getChildList().get(i).setSelectTag(tree.getSelectTag());
			    }
			}			
		}
		return buffer.toString();
	}
	
}

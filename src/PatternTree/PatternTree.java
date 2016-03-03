package PatternTree;

import java.io.Serializable;

//import java.util.ArrayList;
  
//import java.util.List;
//import redis.clients.jedis.Jedis;

public class PatternTree implements Serializable{
	
	// redis服务器主机

    static String host = "192.168.172.128";

    // 端口号

    static int port = 6379;
     
//    Jedis jedis = new Jedis(host, port);
       
      
	/** 树根*/
	private PatternTreeNode root;  
	/** 树的倾斜时间窗口，存储该段时间片的记录总数 */
	private double[] tupleNum_1sec= new double[5];
	private double[] tupleNum_5sec= new double[6];
	private double[] tupleNum_30sec= new double[10];
	private double[] tupleNum_5min= new double[6];
	/** 倾斜时间窗口缓存*/
	private double[] tupleNum_1sec_mid= new double[4];
	private double[] tupleNum_5sec_mid= new double[5];
	private double[] tupleNum_30sec_mid= new double[9];
	private double[] tupleNum_5min_mid= new double[5];
	
	private int presentTimeWindow;   //当前时间窗口号， 与各Node的当前时间窗口做比较以判断后续操作
	private double support;          //FP-Stream算法最小支持度
	private double mistake_rate;     //FP-Stream算法最大误差度
	private int selectTag;           //查询标记，在patternTree进行查询操作时与各节点的查询标记比较，以得出是否查询过该节点
	private int changeTag;           //改变标记，返回时间窗口是否发生改变
	      
	/** 
	 * 构造函数 
	 */  
    public PatternTree()  
	{  
	    root = new PatternTreeNode("root");
	    initTupleNum();                         //初始化
	    setSelectTag(0);
	    setPresentTimeWindow(-1);
	    setSupport(0.5);
	    setMistakeRate(0.3);
	    setChangeTag(0);
	}  
	/** 
	 * 构造函数 
	 * @param support  FP-Stream算法最小支持度
	 */  
    public PatternTree(double support)  
	{  
	    root = new PatternTreeNode("root");
	    initTupleNum();                         //初始化
	    setSelectTag(0);
	    setPresentTimeWindow(-1);
	    setSupport(support);
	    setMistakeRate(0.03);
	    setChangeTag(0);
	}
	/** 
	 * 获取树根 
	 * @return 返回树根
	 */
	public PatternTreeNode getRoot() {  
	    return root;  
	}  
	 /** 
	 * 设置树根 
	 * @param root 树根
	 */ 	  
    public void setRoot(PatternTreeNode root) {  
	    this.root = root;  
	}
    /**
     * 深度优先遍历PatternTree
     * 
     * @param patternTreeNode  遍历的节点
     * @param tree  遍历的PatternTree
     * @return  返回节点的名字、父亲节点名字，以及节点的时间窗口表
     */
    public String iteratorTree(PatternTreeNode patternTreeNode, PatternTree tree)  
    {  
        StringBuilder buffer = new StringBuilder();  
        String str1,str2; 
        if(patternTreeNode != null)   
        {     
            for (PatternTreeNode index : patternTreeNode.getChildList())   
            {  
            	str1=outPutItemSet(index, tree.getRoot());
            	str2=index.getTimeWindow()+"\t"+index.getMidTimeWindow();
                buffer.append(str1+"\t"+str2+"\t"+index.getPresentTimeWindow()+"\n");
//                jedis.set(str1,str2);
                if (index.getChildList() != null && index.getChildList().size() > 0 )   
                {                     	
                    buffer.append(iteratorTree(index, tree));  
                } 
            }  
        }  
             
        return buffer.toString();  
    } 
    /**
     * 遍历patternTreeNode节点，重置创建标记
     * 
     * @param patternTreeNode  遍历的节点
     * 
     * 
     */  
    public void resetCreateTag(PatternTreeNode patternTreeNode)  
    {  
        patternTreeNode.setCreateTag(0);  
        if(patternTreeNode != null)   
        {     
            for (PatternTreeNode index : patternTreeNode.getChildList())   
            {  
                if (index.getChildList() != null && index.getChildList().size() > 0 )   
                {                     	
                   resetCreateTag(index);
                } 
            }  
        }  
    } 

    /**
     * 向树中插入新节点
     *  
     * @param tree  插入的树
     * @param nodeId  新节点名
     * @param parent  节点的父亲
     * @param value   节点的第一个时间窗口的值
     * @param timeWindow  节点的当前时间窗口
     * @return  插入的新节点
     */
    public static PatternTreeNode insertNode(PatternTree tree, String nodeId, PatternTreeNode parent, double value,int timeWindow)
    {
    	PatternTreeNode a1=new PatternTreeNode(nodeId, parent);
    	a1.setPresentTimeWindow(timeWindow);
    	a1.setRemoveTag(0);
    	a1.setSelectTag(0);
    	a1.insert1secWindow(0,value);
    	if (value!=-1)
    		a1.setCreateTag(1);
    	return a1;  	
    }
    /**
     * 初始化存储各时间段记录数的时间窗口
     * 初始化各时间段记录数的倾斜时间窗口缓存
     */
    public void initTupleNum(){
		init1secTupleNum();
		init5secTupleNum();
		init30secTupleNum();
		init5minTupleNum();
		init1secMidTupleNum();
		init5secMidTupleNum();
		init30secMidTupleNum();
		init5minMidTupleNum();
	}
    /**
     * 初始化1s倾斜时间窗口的记录数
	 * 
     */
    public void init1secTupleNum(){
		for(int i=0;i<5;i++)
			this.tupleNum_1sec[i]=-1;
	}
	/**
     * 初始化5s倾斜时间窗口的记录数
	 * 
     */
	public void init5secTupleNum(){
		for(int i=0;i<6;i++)
			this.tupleNum_5sec[i]=-1;
	}
	/**
     * 初始化30s倾斜时间窗口的记录数
	 * 
     */
	public void init30secTupleNum(){
		for(int i=0;i<10;i++)
			this.tupleNum_30sec[i]=-1;
	}
	/**
     * 初始化300s倾斜时间窗口的记录数
	 * 
     */
	public void init5minTupleNum(){
		for(int i=0;i<6;i++)
			this.tupleNum_5min[i]=-1;
	}
	/**
     * 初始化1s倾斜时间窗口缓存的记录数
	 * 
     */
	public void init1secMidTupleNum(){
		for(int i=0;i<4;i++)
			this.tupleNum_1sec_mid[i]=-1;
	}
	/**
     * 初始化5s倾斜时间窗口缓存的记录数
	 * 
     */
	public void init5secMidTupleNum(){
		for(int i=0;i<5;i++)
			this.tupleNum_5sec_mid[i]=-1;
	}
	/**
     * 初始化30s倾斜时间窗口缓存的记录数
	 * 
     */
	public void init30secMidTupleNum(){
		for(int i=0;i<9;i++)
			this.tupleNum_30sec_mid[i]=-1;
	}
	/**
     * 初始化300s倾斜时间窗口缓存的记录数
	 * 
     */
	public void init5minMidTupleNum(){
		for(int i=0;i<5;i++)
			this.tupleNum_5min_mid[i]=-1;
	}
	
	/**
	 * 向时间窗口中插入数据
	 * @param i  插入的位置
	 * @param value  插入的值
	 */
	public void insert1secTupleNum(int i, double value){
		this.tupleNum_1sec[i]=value;
	}
	/**
	 * 向5s倾斜时间窗口中插入数据
	 * @param i  插入的位置
	 * @param value  插入的值
	 */
	public void insert5secTupleNum(int i, double value){
		this.tupleNum_5sec[i]=value;
	}
	/**
	 * 向30s倾斜时间窗口中插入数据
	 * @param i  插入的位置
	 * @param value  插入的值
	 */
	public void insert30secTupleNum(int i, double value){
		this.tupleNum_30sec[i]=value;
	}
	/**
	 * 向300s倾斜时间窗口中插入数据
	 * @param i  插入的位置
	 * @param value  插入的值
	 */
	public void insert5minTupleNum(int i, double value){
		this.tupleNum_5min[i]=value;
	}
	/**
	 * 向1s倾斜时间窗口缓存中插入数据
	 * @param i  插入的位置
	 * @param value  插入的值
	 */
	public void insert1secMidTupleNum(int i, double value){
		this.tupleNum_1sec_mid[i]=value;
	}
	/**
	 * 向5s倾斜时间窗口缓存中插入数据
	 * @param i  插入的位置
	 * @param value  插入的值
	 */
	public void insert5secMidTupleNum(int i, double value){
		this.tupleNum_5sec_mid[i]=value;
	}
	/**
	 * 向30s倾斜时间窗口缓存中插入数据
	 * @param i  插入的位置
	 * @param value  插入的值
	 */
	public void insert30secMidTupleNum(int i, double value){
		this.tupleNum_30sec_mid[i]=value;
	}
	/**
	 * 向300s倾斜时间窗口缓存中插入数据
	 * @param i  插入的位置
	 * @param value  插入的值
	 */
	public void insert5minMidTupleNum(int i, double value){
		this.tupleNum_5min_mid[i]=value;
	}
	
	/** 5个1s
	 * @return 返回倾斜时间窗口非Null的数目 
	 */
	public int num1secTupleNum(){
		int num=0;
		for(int i=0;i<5;i++){
			if (this.tupleNum_1sec[i]>=0)
				num++;
		}
		return num;
	}
	/**
	 * 5s窗口中有数据的个数
	 * @return返回5s窗口中有数据的个数（6*5s）
	 */
	public int num5secTupleNum(){
		int num=0;
		for(int i=0;i<6;i++){
			if (this.tupleNum_5sec[i]>=0)
				num++;
		}
		return num;
	}
	/**
	 * 30s窗口中有数据的个数（10*30s）
	 * @return返回30s窗口中有数据的个数（10*30s）--5min
	 */
	public int num30secTupleNum(){
		int num=0;
		for(int i=0;i<10;i++){
			if (this.tupleNum_30sec[i]>=0)
				num++;
		}
		return num;
	}
	/**
	 * 5min*6计算数组中已插入元素个数
	 * @return 5min*6计算数组中已插入多少元素
	 */
	public int num5minTupleNum(){
		int num=0;
		for(int i=0;i<6;i++){
			if (this.tupleNum_5min[i]>=0)
				num++;
		}
		return num;
	}
	
	/**
	 * 返回1s倾斜时间窗口值
	 * 
	 * @param i 倾斜时间窗口位置
	 * @return  返回指定倾斜时间窗口的值
	 */
	public double get1secTupleNum(int i){
		return this.tupleNum_1sec[i];
	}
	/**
	 * 返回5s时间窗口值
	 * 
	 * @param i 倾斜时间窗口位置
	 * @return  返回指定倾斜时间窗口的值
	 */
	public double get5secTupleNum(int i){
		return this.tupleNum_5sec[i];
	}
	/**
	 * 返回30s倾斜时间窗口值
	 * 
	 * @param i 倾斜时间窗口位置
	 * @return  返回指定倾斜时间窗口的值
	 */
	public double get30secTupleNum(int i){
		return this.tupleNum_30sec[i];
	}
	/**
	 * 返回300s倾斜时间窗口值
	 * 
	 * @param i 倾斜时间窗口位置
	 * @return  返回指定倾斜时间窗口的值
	 */
	public double get5minTupleNum(int i){
		return this.tupleNum_5min[i];
	}
	/**
	 * 返回1s倾斜时间窗口缓存值
	 * 
	 * @param i 倾斜时间窗口缓存位置
	 * @return  返回指定倾斜时间窗口缓存的值
	 */
	public double get1secMidTupleNum(int i){
		return this.tupleNum_1sec_mid[i];
	}
	/**
	 * 返回5s倾斜时间窗口缓存值
	 * 
	 * @param i 倾斜时间窗口缓存位置
	 * @return  返回指定倾斜时间窗口缓存的值
	 */
	public double get5secMidTupleNum(int i){
		return this.tupleNum_5sec_mid[i];
	}
	/**
	 * 返回30s倾斜时间窗口缓存值
	 * 
	 * @param i 倾斜时间窗口缓存位置
	 * @return  返回指定倾斜时间窗口缓存的值
	 */
	public double get30secMidTupleNum(int i){
		return this.tupleNum_30sec_mid[i];
	}
	/**
	 * 返回300s倾斜时间窗口缓存值
	 * 
	 * @param i 倾斜时间窗口缓存位置
	 * @return  返回指定倾斜时间窗口缓存的值
	 */
	public double get5minMidTupleNum(int i){
		return this.tupleNum_5min_mid[i];
	}
	/**
	 * 设置当前时间窗口
	 * 
	 * @param presentTimeWindow 当前时间窗口
	 * 
	 */
	public void setPresentTimeWindow(int presentTimeWindow){
		this.presentTimeWindow=presentTimeWindow;
	}
	/**
	 * 获取当前时间窗口
	 * @return  返回当前时间窗口
	 */
	public int getPresentTimeWindow(){
		return this.presentTimeWindow;
	}
	
	/**
	 * @param node 指定的节点
	 * @param root 根节点
	 * @return  返回节点代表的频繁项集
	 */
	public static String outPutItemSet(PatternTreeNode node, PatternTreeNode root){
		StringBuilder buffer =new StringBuilder();
		
		buffer.append(node.getNodeId());
		if (!node.getParent().equals(root))
		{
			buffer.append(",");
			buffer.append(outPutItemSet(node.getParent(),root));
		}		
		return buffer.toString();		
	}
	/** 
	 * 设置最小支持度
	 * @param support FP-Stream算法最小支持度
	 */	
	public void setSupport(double support){
		this.support=support;
	}
	/** 
	 * 获取最小支持度
	 * @return 获取FP-Stream算法最小支持度
	 */
	public double getSupport(){
		return this.support;
	}
	/** 
	 * 设置FP-Stream算法
	 * @param mistake_rate 最大误差度
	 */
	public void setMistakeRate(double mistake_rate){
		this.mistake_rate=mistake_rate;
	}
	/** 
	 * 获取最大误差度
	 * @return 获取FP-Stream算法最大误差度
	 */
	public double getMistakeRate(){
		return this.mistake_rate;
	}
	/** 
	 * 获取查询标记
	 * @return 返回查询标记
	 */
	public int getSelectTag(){  
	    return this.selectTag;  
	}  
	/** 
	 * 设置查询标记
	 * @param selectTag 查询标记
	 */  
	public void setSelectTag(int selectTag){  
	    this.selectTag = selectTag;  
	}
	/** 
	 * 获取改变标记
	 * @return 返回改变标记
	 */
	public int getChangeTag(){  
	    return this.changeTag;  
	}  
	/** 
	 * 设置改变标记
	 * @param changeTag 改变标记
	 */     
	public void setChangeTag(int changeTag){  
	    this.changeTag = changeTag;  
	}
	
	/**
	 * 获取最近5个1秒时间窗口的和
	 * @return 返回最近5个1秒时间窗口的和(5秒记录总数)
	 */
	public double get5secTupleNum()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num1secTupleNum();i++)
			num=num+this.get1secTupleNum(i);
		for (i=0;i<5-this.num1secTupleNum();i++)
			num=num+this.get1secMidTupleNum(i);
		return num;
	}
	
	/**
	 * 获取最近6个5秒时间窗口的和
	 * @return 最近6个5秒时间窗口的和(30秒记录总数)
	 */
	public double get30secTupleNum()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num5secTupleNum();i++)
			num=num+this.get5secTupleNum(i);
		for (i=0;i<6-this.num5secTupleNum();i++)
			num=num+this.get5secMidTupleNum(i);
		return num;
	}
	
	/**
	 * 获取最近10个30秒时间窗口的和
	 * @return 最近10个30秒时间窗口的和(5分钟记录总数)
	 */
	public double get5minTupleNum()
	{
		int i;
		double num;
		num=0;
		for (i=0;i<this.num30secTupleNum();i++)
			num=num+this.get30secTupleNum(i);
		for (i=0;i<10-this.num30secTupleNum();i++)
			num=num+this.get30secMidTupleNum(i);
		return num;
	}
     
/*
    
    public static void main(String[] args)  
    {  
        PatternTree tree = new PatternTree(); 
        PatternTreeNode p1=insertNode(tree, "a", tree.getRoot(), 100 ,2);
        PatternTreeNode p2=insertNode(tree, "b", p1, 120 ,3);
        System.out.println(tree.getRoot().getChildList());
             
        System.out.println(tree.iteratorTree(tree.getRoot()));  
    }  */
  
}

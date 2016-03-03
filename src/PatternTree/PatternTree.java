package PatternTree;

import java.io.Serializable;

//import java.util.ArrayList;
  
//import java.util.List;
//import redis.clients.jedis.Jedis;

public class PatternTree implements Serializable{
	
	// redis����������

    static String host = "192.168.172.128";

    // �˿ں�

    static int port = 6379;
     
//    Jedis jedis = new Jedis(host, port);
       
      
	/** ����*/
	private PatternTreeNode root;  
	/** ������бʱ�䴰�ڣ��洢�ö�ʱ��Ƭ�ļ�¼���� */
	private double[] tupleNum_1sec= new double[5];
	private double[] tupleNum_5sec= new double[6];
	private double[] tupleNum_30sec= new double[10];
	private double[] tupleNum_5min= new double[6];
	/** ��бʱ�䴰�ڻ���*/
	private double[] tupleNum_1sec_mid= new double[4];
	private double[] tupleNum_5sec_mid= new double[5];
	private double[] tupleNum_30sec_mid= new double[9];
	private double[] tupleNum_5min_mid= new double[5];
	
	private int presentTimeWindow;   //��ǰʱ�䴰�ںţ� ���Node�ĵ�ǰʱ�䴰�����Ƚ����жϺ�������
	private double support;          //FP-Stream�㷨��С֧�ֶ�
	private double mistake_rate;     //FP-Stream�㷨�������
	private int selectTag;           //��ѯ��ǣ���patternTree���в�ѯ����ʱ����ڵ�Ĳ�ѯ��ǱȽϣ��Եó��Ƿ��ѯ���ýڵ�
	private int changeTag;           //�ı��ǣ�����ʱ�䴰���Ƿ����ı�
	      
	/** 
	 * ���캯�� 
	 */  
    public PatternTree()  
	{  
	    root = new PatternTreeNode("root");
	    initTupleNum();                         //��ʼ��
	    setSelectTag(0);
	    setPresentTimeWindow(-1);
	    setSupport(0.5);
	    setMistakeRate(0.3);
	    setChangeTag(0);
	}  
	/** 
	 * ���캯�� 
	 * @param support  FP-Stream�㷨��С֧�ֶ�
	 */  
    public PatternTree(double support)  
	{  
	    root = new PatternTreeNode("root");
	    initTupleNum();                         //��ʼ��
	    setSelectTag(0);
	    setPresentTimeWindow(-1);
	    setSupport(support);
	    setMistakeRate(0.03);
	    setChangeTag(0);
	}
	/** 
	 * ��ȡ���� 
	 * @return ��������
	 */
	public PatternTreeNode getRoot() {  
	    return root;  
	}  
	 /** 
	 * �������� 
	 * @param root ����
	 */ 	  
    public void setRoot(PatternTreeNode root) {  
	    this.root = root;  
	}
    /**
     * ������ȱ���PatternTree
     * 
     * @param patternTreeNode  �����Ľڵ�
     * @param tree  ������PatternTree
     * @return  ���ؽڵ�����֡����׽ڵ����֣��Լ��ڵ��ʱ�䴰�ڱ�
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
     * ����patternTreeNode�ڵ㣬���ô������
     * 
     * @param patternTreeNode  �����Ľڵ�
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
     * �����в����½ڵ�
     *  
     * @param tree  �������
     * @param nodeId  �½ڵ���
     * @param parent  �ڵ�ĸ���
     * @param value   �ڵ�ĵ�һ��ʱ�䴰�ڵ�ֵ
     * @param timeWindow  �ڵ�ĵ�ǰʱ�䴰��
     * @return  ������½ڵ�
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
     * ��ʼ���洢��ʱ��μ�¼����ʱ�䴰��
     * ��ʼ����ʱ��μ�¼������бʱ�䴰�ڻ���
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
     * ��ʼ��1s��бʱ�䴰�ڵļ�¼��
	 * 
     */
    public void init1secTupleNum(){
		for(int i=0;i<5;i++)
			this.tupleNum_1sec[i]=-1;
	}
	/**
     * ��ʼ��5s��бʱ�䴰�ڵļ�¼��
	 * 
     */
	public void init5secTupleNum(){
		for(int i=0;i<6;i++)
			this.tupleNum_5sec[i]=-1;
	}
	/**
     * ��ʼ��30s��бʱ�䴰�ڵļ�¼��
	 * 
     */
	public void init30secTupleNum(){
		for(int i=0;i<10;i++)
			this.tupleNum_30sec[i]=-1;
	}
	/**
     * ��ʼ��300s��бʱ�䴰�ڵļ�¼��
	 * 
     */
	public void init5minTupleNum(){
		for(int i=0;i<6;i++)
			this.tupleNum_5min[i]=-1;
	}
	/**
     * ��ʼ��1s��бʱ�䴰�ڻ���ļ�¼��
	 * 
     */
	public void init1secMidTupleNum(){
		for(int i=0;i<4;i++)
			this.tupleNum_1sec_mid[i]=-1;
	}
	/**
     * ��ʼ��5s��бʱ�䴰�ڻ���ļ�¼��
	 * 
     */
	public void init5secMidTupleNum(){
		for(int i=0;i<5;i++)
			this.tupleNum_5sec_mid[i]=-1;
	}
	/**
     * ��ʼ��30s��бʱ�䴰�ڻ���ļ�¼��
	 * 
     */
	public void init30secMidTupleNum(){
		for(int i=0;i<9;i++)
			this.tupleNum_30sec_mid[i]=-1;
	}
	/**
     * ��ʼ��300s��бʱ�䴰�ڻ���ļ�¼��
	 * 
     */
	public void init5minMidTupleNum(){
		for(int i=0;i<5;i++)
			this.tupleNum_5min_mid[i]=-1;
	}
	
	/**
	 * ��ʱ�䴰���в�������
	 * @param i  �����λ��
	 * @param value  �����ֵ
	 */
	public void insert1secTupleNum(int i, double value){
		this.tupleNum_1sec[i]=value;
	}
	/**
	 * ��5s��бʱ�䴰���в�������
	 * @param i  �����λ��
	 * @param value  �����ֵ
	 */
	public void insert5secTupleNum(int i, double value){
		this.tupleNum_5sec[i]=value;
	}
	/**
	 * ��30s��бʱ�䴰���в�������
	 * @param i  �����λ��
	 * @param value  �����ֵ
	 */
	public void insert30secTupleNum(int i, double value){
		this.tupleNum_30sec[i]=value;
	}
	/**
	 * ��300s��бʱ�䴰���в�������
	 * @param i  �����λ��
	 * @param value  �����ֵ
	 */
	public void insert5minTupleNum(int i, double value){
		this.tupleNum_5min[i]=value;
	}
	/**
	 * ��1s��бʱ�䴰�ڻ����в�������
	 * @param i  �����λ��
	 * @param value  �����ֵ
	 */
	public void insert1secMidTupleNum(int i, double value){
		this.tupleNum_1sec_mid[i]=value;
	}
	/**
	 * ��5s��бʱ�䴰�ڻ����в�������
	 * @param i  �����λ��
	 * @param value  �����ֵ
	 */
	public void insert5secMidTupleNum(int i, double value){
		this.tupleNum_5sec_mid[i]=value;
	}
	/**
	 * ��30s��бʱ�䴰�ڻ����в�������
	 * @param i  �����λ��
	 * @param value  �����ֵ
	 */
	public void insert30secMidTupleNum(int i, double value){
		this.tupleNum_30sec_mid[i]=value;
	}
	/**
	 * ��300s��бʱ�䴰�ڻ����в�������
	 * @param i  �����λ��
	 * @param value  �����ֵ
	 */
	public void insert5minMidTupleNum(int i, double value){
		this.tupleNum_5min_mid[i]=value;
	}
	
	/** 5��1s
	 * @return ������бʱ�䴰�ڷ�Null����Ŀ 
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
	 * 5s�����������ݵĸ���
	 * @return����5s�����������ݵĸ�����6*5s��
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
	 * 30s�����������ݵĸ�����10*30s��
	 * @return����30s�����������ݵĸ�����10*30s��--5min
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
	 * 5min*6�����������Ѳ���Ԫ�ظ���
	 * @return 5min*6�����������Ѳ������Ԫ��
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
	 * ����1s��бʱ�䴰��ֵ
	 * 
	 * @param i ��бʱ�䴰��λ��
	 * @return  ����ָ����бʱ�䴰�ڵ�ֵ
	 */
	public double get1secTupleNum(int i){
		return this.tupleNum_1sec[i];
	}
	/**
	 * ����5sʱ�䴰��ֵ
	 * 
	 * @param i ��бʱ�䴰��λ��
	 * @return  ����ָ����бʱ�䴰�ڵ�ֵ
	 */
	public double get5secTupleNum(int i){
		return this.tupleNum_5sec[i];
	}
	/**
	 * ����30s��бʱ�䴰��ֵ
	 * 
	 * @param i ��бʱ�䴰��λ��
	 * @return  ����ָ����бʱ�䴰�ڵ�ֵ
	 */
	public double get30secTupleNum(int i){
		return this.tupleNum_30sec[i];
	}
	/**
	 * ����300s��бʱ�䴰��ֵ
	 * 
	 * @param i ��бʱ�䴰��λ��
	 * @return  ����ָ����бʱ�䴰�ڵ�ֵ
	 */
	public double get5minTupleNum(int i){
		return this.tupleNum_5min[i];
	}
	/**
	 * ����1s��бʱ�䴰�ڻ���ֵ
	 * 
	 * @param i ��бʱ�䴰�ڻ���λ��
	 * @return  ����ָ����бʱ�䴰�ڻ����ֵ
	 */
	public double get1secMidTupleNum(int i){
		return this.tupleNum_1sec_mid[i];
	}
	/**
	 * ����5s��бʱ�䴰�ڻ���ֵ
	 * 
	 * @param i ��бʱ�䴰�ڻ���λ��
	 * @return  ����ָ����бʱ�䴰�ڻ����ֵ
	 */
	public double get5secMidTupleNum(int i){
		return this.tupleNum_5sec_mid[i];
	}
	/**
	 * ����30s��бʱ�䴰�ڻ���ֵ
	 * 
	 * @param i ��бʱ�䴰�ڻ���λ��
	 * @return  ����ָ����бʱ�䴰�ڻ����ֵ
	 */
	public double get30secMidTupleNum(int i){
		return this.tupleNum_30sec_mid[i];
	}
	/**
	 * ����300s��бʱ�䴰�ڻ���ֵ
	 * 
	 * @param i ��бʱ�䴰�ڻ���λ��
	 * @return  ����ָ����бʱ�䴰�ڻ����ֵ
	 */
	public double get5minMidTupleNum(int i){
		return this.tupleNum_5min_mid[i];
	}
	/**
	 * ���õ�ǰʱ�䴰��
	 * 
	 * @param presentTimeWindow ��ǰʱ�䴰��
	 * 
	 */
	public void setPresentTimeWindow(int presentTimeWindow){
		this.presentTimeWindow=presentTimeWindow;
	}
	/**
	 * ��ȡ��ǰʱ�䴰��
	 * @return  ���ص�ǰʱ�䴰��
	 */
	public int getPresentTimeWindow(){
		return this.presentTimeWindow;
	}
	
	/**
	 * @param node ָ���Ľڵ�
	 * @param root ���ڵ�
	 * @return  ���ؽڵ�����Ƶ���
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
	 * ������С֧�ֶ�
	 * @param support FP-Stream�㷨��С֧�ֶ�
	 */	
	public void setSupport(double support){
		this.support=support;
	}
	/** 
	 * ��ȡ��С֧�ֶ�
	 * @return ��ȡFP-Stream�㷨��С֧�ֶ�
	 */
	public double getSupport(){
		return this.support;
	}
	/** 
	 * ����FP-Stream�㷨
	 * @param mistake_rate �������
	 */
	public void setMistakeRate(double mistake_rate){
		this.mistake_rate=mistake_rate;
	}
	/** 
	 * ��ȡ�������
	 * @return ��ȡFP-Stream�㷨�������
	 */
	public double getMistakeRate(){
		return this.mistake_rate;
	}
	/** 
	 * ��ȡ��ѯ���
	 * @return ���ز�ѯ���
	 */
	public int getSelectTag(){  
	    return this.selectTag;  
	}  
	/** 
	 * ���ò�ѯ���
	 * @param selectTag ��ѯ���
	 */  
	public void setSelectTag(int selectTag){  
	    this.selectTag = selectTag;  
	}
	/** 
	 * ��ȡ�ı���
	 * @return ���ظı���
	 */
	public int getChangeTag(){  
	    return this.changeTag;  
	}  
	/** 
	 * ���øı���
	 * @param changeTag �ı���
	 */     
	public void setChangeTag(int changeTag){  
	    this.changeTag = changeTag;  
	}
	
	/**
	 * ��ȡ���5��1��ʱ�䴰�ڵĺ�
	 * @return �������5��1��ʱ�䴰�ڵĺ�(5���¼����)
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
	 * ��ȡ���6��5��ʱ�䴰�ڵĺ�
	 * @return ���6��5��ʱ�䴰�ڵĺ�(30���¼����)
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
	 * ��ȡ���10��30��ʱ�䴰�ڵĺ�
	 * @return ���10��30��ʱ�䴰�ڵĺ�(5���Ӽ�¼����)
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

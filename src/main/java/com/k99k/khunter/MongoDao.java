package com.k99k.khunter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 基于mongoDB的基础DAO
 * @author keel
 *
 */
public class MongoDao implements DaoInterface{
	
	
	
	static final Logger log = Logger.getLogger(MongoDao.class);
	
	/**
	 * id生成器,默认初始值为1,增量为1
	 */
	final IDManager idm = new IDManager(1,1);
	
	/**
	 * 空BasicDBObject,备用
	 */
	static final BasicDBObject emptyBasicDBObject  = new BasicDBObject();
	
	/**
	 * 数据库的表名
	 */
	private String tableName;
	
	/**
	 * Dao的标识name
	 */
	private String name;
	
	/**
	 * 用于标识多个实例
	 */
	private int id;
	
	/**
	 * 数据源
	 */
	private MongoConn dataSource;
	
	/**
	 * 数据库类型
	 */
	private String dbType;
	
	/**
	 * 载入类型
	 */
	private String type;
	
	/**
	 * 创建建时初始化id生成器
	 * @param daoName Dao名称
	 */
	public MongoDao(String daoName,DataSourceInterface dataSource) {
		super();
		this.name = daoName;
		this.dataSource = (MongoConn) dataSource;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("clone failed.",e);
			return null;
		}
	}



	/* (non-Javadoc)
	 * @see com.k99k.khunter.DaoInterface#init()
	 */
	public boolean init(){
		try {
			idm.setId(initIDM(tableName));
		} catch (Exception e) {
			log.error("init error!", e);
			return false;
		}
		return true;
	}

	/**
	 * 查找对象
	 * @param id long
	 * @return 未找到返回null
	 */
	public KObject findOne(long id){
		Map<String, Object> m = this.findMap(id);
		if (m != null) {
			return  new  KObject(m);
		}
		return null;
		
	}
	
	/**
	 * 按名称查找KObject对象
	 * @param name KObject的name
	 * @return
	 */
	public KObject findOne(String name){
		Map<String, Object> m = this.findOneMap(new BasicDBObject("name", name),null);
		if (m != null) {
			return  new  KObject(m);
		}
		return null;
	}
	
	/**
	 * 查找单个对象
	 * @param id long
	 * @return 未找到返回null
	 */
	public Map<String,Object> findOneMap(BasicDBObject query){
		return this.findOneMap(query, null);
	}
	
	/**
	 * 查找单个对象
	 * @param query
	 * @param fields
	 * @return
	 */
	public Map<String,Object> findOneMap(BasicDBObject query,BasicDBObject fields){
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			DBCursor cur = coll.find(query,fields,0, 1);
	        if(cur.hasNext()) {
	        	return (Map) cur.next();
	        }
	        return null;
		} catch (MongoException e) {
			log.error("find error!", e);
			return null;
		}
	}
	
	/**
	 * 通用的查找过程
	 * @param query 必须有
	 * @param fields 全部则为null
	 * @param sortBy 无则为null
	 * @param skip 无则为0
	 * @param len 无则为0
	 * @param hint 无则为null
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> findByProps(BasicDBObject query,BasicDBObject fields,BasicDBObject sortBy,int skip,int len,BasicDBObject hint){
		int initSize = 20;
		if (len > 0) {
			initSize = len;
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(initSize);
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			DBCursor cur = null;
			if (sortBy != null) {
				cur = coll.find(query, fields, skip, len).sort(sortBy).hint(hint);
			} else {
				cur = coll.find(query, fields, skip, len).hint(hint);
			}
	        while(cur.hasNext()) {
	        	Map<String, Object> m = (Map<String, Object>) cur.next();
	        	list.add(m);
	        }
	        return list;
		} catch (MongoException e) {
			log.error("findByProps error!", e);
			return null;
		}
		
	}
	
	/**
	 * 按条件查询数量
	 * @param query 必须有
	 * @param hint 无则为null
	 * @return 数量
	 */
	public int count(BasicDBObject query,BasicDBObject hint){
		
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			return coll.find(query).hint(hint).count();
		} catch (MongoException e) {
			log.error("findByProps error!", e);
			return 0;
		}
		
	}
	
	/**
	 * 按条件查询数量
	 * @param query 必须有
	 * @return 数量
	 */
	public int count(BasicDBObject query){
		
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			return coll.find(query).count();
		} catch (MongoException e) {
			log.error("findByProps error!", e);
			return 0;
		}
		
	}
	
	/**
	 * 查找Map形式对象
	 * @param id long
	 * @return Map形式,未找到返回null
	 */
	public Map<String, Object> findMap(long id){
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			DBObject o = coll.findOne(id);
			if (o != null) {
				return (Map)o;
			}
			return null;
		} catch (MongoException e) {
			log.error("findMap error!", e);
			return null;
		}
	}
	
	
	/**
	 * 创建新对象,自动生成新ID
	 * @param kObj KObject
	 * @return 
	 */
	public boolean add(KObject kObj){
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			kObj.setId(idm.nextId());
			MongoWrapper w = new MongoWrapper(kObj);
			coll.insert(w);
		} catch (MongoException e) {
			log.error("add error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 创建或更新对象,注意此方法不自动生成ID
	 * @param kObj KObject
	 * @return
	 */
	public boolean save(KObject kObj){
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.save(new MongoWrapper(kObj));
		} catch (MongoException e) {
			log.error("save error!", e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 更新对象
	 * @param id long
	 * @param newObj KObject
	 * @return
	 */
	public boolean updateOne(long id,KObject newObj) {
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.update(new BasicDBObject("_id",id),new MongoWrapper(newObj));
		} catch (MongoException e) {
			log.error("update error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 更新单个对象
	 * @param query BasicDBObject
	 * @param newObj KObject
	 * @return
	 */
	public boolean updateOne(BasicDBObject query,KObject newObj) {
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.update(query,new MongoWrapper(newObj),false,false);
		} catch (MongoException e) {
			log.error("update error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 更新单个对象
	 * @param query BasicDBObject
	 * @param set BasicDBObject
	 * @return
	 */
	public boolean updateOne(BasicDBObject query,BasicDBObject set) {
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.update(query,set,false,false);
		} catch (MongoException e) {
			log.error("update error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 批量更新对象属性
	 * @param query BasicDBObject
	 * @param set BasicDBObject
	 * @return
	 */
	public boolean update(BasicDBObject query,BasicDBObject set) {
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.update(query,set,false,true);
		} catch (MongoException e) {
			log.error("update error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 标记删除,即将state置为-1
	 * @param id
	 * @return
	 */
	public boolean deleteOne(long id){
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.update(new BasicDBObject("_id",id),new BasicDBObject("$set",new BasicDBObject("state",-1)),false,false);
		} catch (MongoException e) {
			log.error("delete error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 标记删除,即将state置为-1
	 * @param query
	 * @param multi 是否批量
	 * @return
	 */
	public boolean delete(BasicDBObject query,boolean multi){
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.update(query,new BasicDBObject("$set",new BasicDBObject("state",-1)),false,multi);
		} catch (MongoException e) {
			log.error("delete error!", e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 从数据库中按条件批量彻底删除
	 * @param id
	 * @return
	 */
	public boolean deleteForever(BasicDBObject query){
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.remove(query);
		} catch (MongoException e) {
			log.error("deleteForever error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 按id从数据库中彻底删除
	 * @param id
	 * @return
	 */
	public boolean deleteForever(long id){
		try {
			//coll = checkColl(coll);
			DBCollection coll = this.dataSource.getColl(tableName);
			coll.remove(new BasicDBObject("_id",id));
		} catch (MongoException e) {
			log.error("deleteForever error!", e);
			return false;
		}
		return true;
	}
	/**
	 * 初始化id生成器,注意这里使用的_id
	 * @param tableName
	 * @return 当前最大id值  long
	 */
	long initIDM(String tableName){
		DBCollection coll = this.dataSource.getColl(tableName);
		DBCursor cur = coll.find(emptyBasicDBObject,new BasicDBObject("_id",1)).sort(new BasicDBObject("_id",-1)).limit(1);
		if (cur.hasNext()) {
			DBObject o = cur.next();
			return Long.parseLong(o.get("_id").toString());
		}else{
			return 1;
		}
	}
	
	/*
	private final DBCollection checkColl(DBCollection coll){
		if (coll == null || (!coll.getName().equals(tableName))) {
			return this.dataSource.getColl(tableName);
		}else{
			return coll;
		}
	}
	*/

	 /**
	 * @return the dataSource
	 */
	public final DataSourceInterface getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public final void setDataSource(DataSourceInterface dataSource) {
		this.dataSource = (MongoConn) dataSource;
	}

	public static void main(String[] args) throws Exception {

	        // connect to the local database server
	        Mongo m = new Mongo();

	        // get handle to "mydb"
	        DB db = m.getDB( "mydb" );
	    
	        // Authenticate - optional
	        // boolean auth = db.authenticate("foo", "bar");


	        // get a list of the collections in this database and print them out
	        Set<String> colls = db.getCollectionNames();
	        for (String s : colls) {
	            System.out.println(s);
	        }  

	        // get a collection object to work with
	        DBCollection coll = db.getCollection("testCollection");
	        
	        // drop all the data in it
	        coll.drop();


	        // make a document and insert it
	        BasicDBObject doc = new BasicDBObject();

	        doc.put("name", "MongoDB");
	        doc.put("type", "database");
	        doc.put("count", 1);

	        BasicDBObject info = new BasicDBObject();

	        info.put("x", 203);
	        info.put("y", 102);

	        doc.put("info", info);

	        coll.insert(doc);

	        // get it (since it's the only one in there since we dropped the rest earlier on)
	        DBObject myDoc = coll.findOne();
	        System.out.println(myDoc);

	        // now, lets add lots of little documents to the collection so we can explore queries and cursors
	        for (int i=0; i < 100; i++) {
	            coll.insert(new BasicDBObject().append("i", i));
	        }
	        System.out.println("total # of documents after inserting 100 small ones (should be 101) " + coll.getCount());

	        //  lets get all the documents in the collection and print them out
	        DBCursor cur = coll.find();
	        while(cur.hasNext()) {
	            System.out.println(cur.next());
	        }

	        //  now use a query to get 1 document out
	        BasicDBObject query = new BasicDBObject();
	        query.put("i", 71);
	        cur = coll.find(query);

	        while(cur.hasNext()) {
	            System.out.println(cur.next());
	        }

	        //  now use a range query to get a larger subset
	        query = new BasicDBObject();
	        query.put("i", new BasicDBObject("$gt", 50));  // i.e. find all where i > 50
	        cur = coll.find(query);

	        while(cur.hasNext()) {
	            System.out.println(cur.next());
	        }

	        // range query with multiple contstraings
	        query = new BasicDBObject();
	        query.put("i", new BasicDBObject("$gt", 20).append("$lte", 30));  // i.e.   20 < i <= 30
	        cur = coll.find(query);

	        while(cur.hasNext()) {
	            System.out.println(cur.next());
	        }

	        // create an index on the "i" field
	        coll.createIndex(new BasicDBObject("i", 1));  // create index on "i", ascending


	        //  list the indexes on the collection
	        List<DBObject> list = coll.getIndexInfo();
	        for (DBObject o : list) {
	            System.out.println(o);
	        }

	        // See if the last operation had an error
	        System.out.println("Last error : " + db.getLastError());

	        // see if any previous operation had an error
	        System.out.println("Previous error : " + db.getPreviousError());

	        // force an error
	        db.forceError();

	        // See if the last operation had an error
	        System.out.println("Last error : " + db.getLastError());

	        db.resetError();
	        m.close();
	    }

	@Override
	public String getName() {
		return this.name;
	}
	



	/**
	 * @return the tableName
	 */
	public final String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public final void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the dbType
	 */
	public final String getDbType() {
		return dbType;
	}

	/**
	 * @param dbType the dbType to set
	 */
	public final void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(String type) {
		this.type = type;
	}


	
}

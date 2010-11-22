package com.k99k.khunter;

import java.util.List;
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
	static final IDManager idm = new IDManager(1,1);
	
	/**
	 * 空BasicDBObject,备用
	 */
	static final BasicDBObject emptyBasicDBObject  = new BasicDBObject();
	
	/**
	 * dao名称，同时也是数据库的表名
	 */
	private final String daoName;
	
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
	 * @param daoName Dao名称，同时也是数据库的表名
	 */
	public MongoDao(String daoName,DataSourceInterface dataSource) {
		super();
		this.daoName = daoName;
		this.dataSource = (MongoConn) dataSource;
		idm.setId(initIDM(daoName));
	}

	/**
	 * 创建新对象
	 * @param kwObj KWObject
	 * @return
	 */
	public boolean add(MongoKObject kwObj){
		try {
			DBCollection coll = this.dataSource.getColl(daoName);
			kwObj.setId(idm.nextId());
			coll.insert(kwObj);
		} catch (MongoException e) {
			log.error("add error!", e);
			return false;
		}
		return true;
	}
	
	public boolean update(long id,MongoKObject newObj) {
		
		try {
			DBCollection coll = this.dataSource.getColl(daoName);
			coll.update(new BasicDBObject("id",id),newObj);
		} catch (MongoException e) {
			log.error("add error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 初始化id生成器
	 * @param tableName
	 * @return 当前最大id值  long
	 */
	long initIDM(String tableName){
		DBCollection coll = this.dataSource.getColl(tableName);
		DBCursor cur = coll.find(emptyBasicDBObject,new BasicDBObject("id",1)).sort(new BasicDBObject("id",-1)).limit(1);
		if (cur.hasNext()) {
			DBObject o = cur.next();
			return ((Long)o.get("id"));
		}else{
			return 1;
		}
	}
	
	

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
		return daoName;
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

package mongoDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Object that grants acces to the Mongo database specified in the 
 * AgentFileCollector.properties file. After stablishing the connection 
 * the object gives acces to the SyncedMetrics collection.
 * @author Emanuel Krivoy
 *
 */
public class MongoConnection {

	private static final String SYNCED_COLLECTION = "SyncedMetrics";
	
	private static final String DB_IP = "db_IP";
	private static final String DB_PORT = "db_port";
	private static final String DB_NAME = "db_name";
	private static final String DB_USER = "db_user";
	private static final String DB_PASSWORD = "db_password";
	private static final String DB_DBNAME = "db_dbname";
	
	protected String ip;
	protected int port;
	protected String name;
	protected String user;
	protected String password;
	
	protected MongoClient client;
	protected DB db;
	
	public MongoConnection() throws Exception{
		Properties prop = new Properties();
		InputStream inputStream = new FileInputStream(new File("AgentFileCollector.properties"));
		prop.load(inputStream);
		
		ip = prop.getProperty(DB_IP);
		port = Integer.parseInt(prop.getProperty(DB_PORT));
		name = prop.getProperty(DB_NAME);
		user = prop.getProperty(DB_USER);
		password = prop.getProperty(DB_PASSWORD);
		
		String dbName = prop.getProperty(DB_DBNAME); 
		
		client = getClient();
		
		db = client.getDB(dbName);
	}

	/**
	 * Gives acces to the SyncedMetrics collection in the connected database
	 * @return DBCollection object that represents the SyncedMetric collection
	 */
	public DBCollection getSyncedCollection() {
		return db.getCollection(SYNCED_COLLECTION);
	}
	
	/**
	 * Returns the Mongo client for the specified database
	 * @return MongoClient connected to the database
	 * @throws UnknownHostException
	 */
	protected MongoClient getClient() throws UnknownHostException {
    	MongoClient conexion ;      
        MongoCredential credential = MongoCredential.createCredential(user, name, password.toCharArray());
        ServerAddress address = new ServerAddress(ip, port);
        conexion = new MongoClient(address, Arrays.asList(credential));
        return conexion;
	}
	
	public void close(){
		client.close();
	}
	
}

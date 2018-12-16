package packMongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class mongoCON {
    private MongoClient mongoClient;
    public mongoCON(){

    }

    public MongoDatabase connect(String uri, String databaseName){
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        this.mongoClient = new MongoClient(mongoClientURI);
        return this.mongoClient.getDatabase(databaseName);
    }

    public MongoClient getMongoClient(){
        return this.mongoClient;
    }

    public boolean insertItem(Document document, MongoCollection<Document> mongoCollection){
        mongoCollection.insertOne(document);
        return true;
    }

    public boolean removeItem(String key, String value, MongoCollection<Document> mongoCollection){
        mongoCollection.deleteOne(eq(key, value));
        return true;
    }
}

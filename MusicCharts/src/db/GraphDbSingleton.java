package db;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GraphDbSingleton {

	private static final String NEO4J_PATH = "/home/izel/Downloads/neo4j-community-2.3.1/data/graph.db";
	public static GraphDatabaseService graphDB = null;

	public static synchronized GraphDatabaseService getGraphDB() {
		if (graphDB == null) {
			graphDB = new GraphDatabaseFactory()
					.newEmbeddedDatabase(new File(
							NEO4J_PATH));
		}
		return graphDB;
	}
}

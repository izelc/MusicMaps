package db;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Analyzer {

	private final Logger logger = Logger.getLogger(Analyzer.class);

	public ArrayList<Map> getTopArtists(String university) {
		String query = "MATCH(a:Artist)-[SINGS]->(t:Track)<-[LISTENS]-(s:User)-[STUDIED_AT]->(u:University) with count(distinct s) as ct,u,a WHERE u.universityName=\""
				+ university
				+ "\" AND ct>=2 RETURN a.name ORDER BY ct DESC LIMIT 15";
		return execute(query);

	}

	public ArrayList<Map> getTopGenres(String university) {
		String query = "MATCH (g:Genre)-[TAGS]->(a:Artist)-[SINGS]->(t:Track)<-[LISTENS]-(s:User)-[STUDIED_AT]->(u:University) with count(distinct s) as ct,u,a,g WHERE u.universityName=\""
				+ university
				+ "\"  AND ct>=2 RETURN distinct g.genre,ct ORDER BY ct DESC LIMIT 15";

		return execute(query);

	}

	public ArrayList<Map> getTopTurkishArtist(String university) {
		String query = "MATCH(n:Nationality)-[HAS_NATIONALITY_OF]->(a:Artist)-[SINGS]->(t:Track)<-[LISTENS]-(s:User)-[STUDIED_AT]->(u:University) with count(distinct s) as ct,u,a,n WHERE u.universityName=\""
				+ university
				+ "\" AND ct>=2 AND n.nationality=\"TR\" RETURN a.name,ct ORDER BY ct DESC";
		return execute(query);

	}

	public ArrayList<Map> execute(String query) throws QueryExecutionException {
		Result execute = GraphDbSingleton.getGraphDB().execute(query);

		ArrayList<Map> arrayList = new ArrayList<Map>();
		while (execute.hasNext()) {
			Map<String, Object> next = execute.next();
			System.out.println(next.toString());
			arrayList.add(next);
		}

		return arrayList;
	}

	private ArrayList<Map> getTopTurkishTracks(String university) {
		String query = "MATCH(n:Nationality)-[HAS_NATIONALITY_OF]->(a:Artist)-[SINGS]->(t:Track)<-[LISTENS]-(s:User)-[STUDIED_AT]->(u:University) with count(distinct s) as ct,u,a,t,n Where u.universityName=\""
				+ university
				+ "\" AND ct>=2 AND n.nationality=\"TR\"  RETURN a.name, t.name,ct ORDER BY ct DESC";

		return execute(query);
	}

	public int getTurkishArtistsRate(String university) {
		String numOfAllSongsQuery = "MATCH(n:Nationality)-[HAS_NATIONALITY_OF]->(a:Artist)-[SINGS]->(t:Track)<-[LISTENS]-(s:User)-[STUDIED_AT]->(u:University) Where u.universityName=\""
				+ university
				+ "\" AND NOT(n.nationality=\"unknown\") RETURN count(n)";

		String numOfTurkishSongsQuery = "MATCH(n:Nationality)-[HAS_NATIONALITY_OF]->(a:Artist)-[SINGS]->(t:Track)<-[LISTENS]-(s:User)-[STUDIED_AT]->(u:University) Where u.universityName=\""
				+ university + "\" AND n.nationality=\"TR\" RETURN count(n)";

		String numOfTurkish = GraphDbSingleton.getGraphDB()
				.execute(numOfTurkishSongsQuery).next().get("count(n)")
				.toString();
		logger.info("Number of Turkish songs: " + numOfTurkish);

		String numOfAll = GraphDbSingleton.getGraphDB()
				.execute(numOfAllSongsQuery).next().get("count(n)").toString();
		logger.info("Number of all songs: " + numOfAll);

		int rate = Integer.parseInt(numOfTurkish) * 100
				/ Integer.parseInt(numOfAll);
		logger.info("Percent of turkish songs at: " + university + " rate: "
				+ rate);
		return rate;

	}

	public ArrayList<Map> getTopTracks(String university) {
		String query = "MATCH(a:Artist)-[SINGS]->(t:Track)<-[LISTENS]-(s:User)-[STUDIED_AT]->(u:University) with count(distinct s) as ct,u,a,t Where u.universityName=\""
				+ university
				+ "\" AND ct>=2 RETURN a.name, t.name, t.trackId ORDER BY ct DESC LIMIT 15";
		return execute(query);
	}

	public  AudioFeatures getTopTracksAnalyze(String university) {
		String query = "MATCH(a:Artist)-[SINGS]->(t:Track)<-[LISTENS]-(s:User)-[STUDIED_AT]->(u:University) with count(distinct s) as ct,u,a,t Where u.universityName=\""
				+ university
				+ "\" AND ct>=2 RETURN a.name, t.name, t.trackId ORDER BY ct DESC";
		
		ArrayList<Map> topTracks = execute(query);
		String str = "";

		for (Map<String, Object> map : topTracks) {
			str += "," + map.get("t.trackId");
		}

		String idString = str.substring(1);
		AudioFeatures audioFeatures = new ChartAnalyzer()
				.getAudioFeatures(idString);

		return audioFeatures;

	}

}

package db;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import extractors.Track;

public class Inserter {
	private final Logger logger = Logger.getLogger(	Inserter.class);
	private GraphDatabaseService graphDb = null;

	public Inserter() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(
				"/home/izel/Downloads/neo4j-community-2.3.1/data/graph.db"));
	}

	

	public Logger getLogger() {
		return logger;
	}

	public void insertTrack(Node userNode, Transaction tx, String trackId,
			String trackName, String artistName, String artistId, String genre) {

		logger.info("trying to insert track: " + artistName + " - " + trackId);

		Node artistNode;
		artistNode = findOrCreateUniqueNode("artistId", artistId);
		artistNode.setProperty("name", artistName);
		artistNode.addLabel(new Label() {

			@Override
			public String name() {
				return "Artist";
			}
		});

		// creating track

		Node trackNode;
		trackNode = findOrCreateUniqueNode("trackId", trackId);

		trackNode.setProperty("name", trackName);
		trackNode.setProperty("trackId", trackId);
		trackNode.addLabel(new Label() {

			@Override
			public String name() {
				return "Track";
			}
		});

		if (!genre.equals("")) {
			Node genreNode;
			genreNode = findOrCreateUniqueNode("genre", genre);
			genreNode.setProperty("genre", genre);
			genreNode.addLabel(new Label() {

				@Override
				public String name() {
					return "Genre";
				}
			});
			if (!artistNode.hasRelationship(RelTypes.TAGS))
				InsertRelationship(tx, genreNode, artistNode, RelTypes.TAGS);
		}
		if (!trackNode.hasRelationship(RelTypes.SINGS))
			InsertRelationship(tx, artistNode, trackNode, RelTypes.SINGS);
		InsertRelationship(tx, userNode, trackNode, RelTypes.LISTENS);

	}

	public Node findOrCreateUniqueNode(String uniqueAttribute,
			String uniqueValue) throws QueryExecutionException {
		Node node;
		Result execute = graphDb.execute("match (n {" + uniqueAttribute + ": '"
				+ uniqueValue + "'}) return n");

		// creating artist

		if (!execute.hasNext()) {
			node = graphDb.createNode();
			node.setProperty(uniqueAttribute, uniqueValue);
		} else {
			node = (Node) execute.next().values().iterator().next();
		}
		return node;
	}

	public void InsertRelationship(Transaction tx, Node subjectNode,
			Node objectNode, RelTypes relation) {
		subjectNode.createRelationshipTo(objectNode, relation);
		tx.success();
	}

	public void insertUser(String userId, String userName,
			String university, ArrayList<Track> tracklist) {
		try {

			logger.info("Inserting user: " + userId + " , " + userName
					+ " , " + university);

			try (Transaction tx = graphDb.beginTx()) {
					
				Node universityNode = findOrCreateUniqueNode("universityName", university);

				universityNode.setProperty("title", university);
				universityNode.addLabel(new Label() {

					@Override
					public String name() {
						return "University";
					}
				});
				
				Node userNode = findOrCreateUniqueNode("userId", userId);
				userNode.setProperty("userId", userId);
				userNode.setProperty("userName", userName);
				userNode.setProperty("title", userName);
				userNode.addLabel(new Label() {

					@Override
					public String name() {
						return "User";
					}
				});
				InsertRelationship(tx, userNode, universityNode, RelTypes.STUDIED_AT);

				logger.info("user and his location succesfully created: "
						+ userName + " - " + university);

				for (Track track : tracklist) {
					insertTrack(userNode, tx, track.getTrackId(),
							track.getTrackName(), track.getArtist(),
							track.getArtistId(), track.getGenre());

				}

				// Database operations go here
				tx.success();
				logger.info("User succesfuly inserted"+userName);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			graphDb.shutdown();
		}

	}
}
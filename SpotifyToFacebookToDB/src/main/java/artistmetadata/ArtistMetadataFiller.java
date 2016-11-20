package artistmetadata;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.musicbrainz.controller.Artist;
import org.musicbrainz.model.TagWs2;
import org.musicbrainz.model.entity.ArtistWs2;
import org.musicbrainz.model.searchresult.ArtistResultWs2;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import db.RelTypes;

public class ArtistMetadataFiller {

	private final Logger logger = Logger.getLogger(ArtistMetadataFiller.class);
	private GraphDatabaseService graphDb;

	public ArtistMetadataFiller() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(
				"/home/izel/Downloads/neo4j-community-2.3.1/data/graph.db"));
	}

	public static void main(String[] args) {
		new ArtistMetadataFiller()
				.findArtistsWithoutAttribute(RelTypes.HAS_NATIONALITY_OF);
		new ArtistMetadataFiller().findArtistsWithoutAttribute(RelTypes.TAGS);
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

	/**
	 * @param relationship
	 *            for now relationship can only be TAGS or HAS_NATIONALITY_OF
	 *            Method is for finding artist on database without nationality
	 *            or genre. And try to fill out their attributes with values
	 *            found on MusicBrainz API.
	 */
	public void findArtistsWithoutAttribute(RelTypes relationship) {
		Result result = graphDb.execute("MATCH(a:Artist) WHERE NOT (a)<-[:"
				+ relationship + "]-() Return a");

		while (result.hasNext()) {
			Map<String, Object> record = result.next();
			try {
				Node artistNode = (Node) record.get("a");
				String artistName = artistNode.getProperty("name").toString();

				Artist artist = new Artist();

				artist.getSearchFilter().setLimit((long) 1);

				artist.search(artistName);
				List<ArtistResultWs2> results = artist
						.getFirstSearchResultPage();

				if (!results.isEmpty()) {
					for (ArtistResultWs2 artistResultWs2 : results) {
						ArtistWs2 artistFromMusicBrainz = artistResultWs2
								.getArtist();
						if (artistName.equals(artistFromMusicBrainz.getName()))
							updateAttributes(artistNode, artistFromMusicBrainz,
									relationship);

					}
				} else {
					logger.debug("Artist doesnt found on MusicBrainz"
							+ artistName);
				}
			} catch (Exception e) {
				logger.error("Found error trying to fill" + relationship);
				e.printStackTrace();
			}

		}
	}

	public void updateAttributes(Node artistNode,
			ArtistWs2 artistFromMusicBrainz, RelTypes relationship)
			throws QueryExecutionException {
		{
			String attributeFromMusicBrainz = "";
			Node attributeNode = null;

			if (relationship.equals(RelTypes.TAGS)) {
				List<TagWs2> tags = artistFromMusicBrainz.getTags();
				if (!tags.isEmpty()) {
					attributeFromMusicBrainz = tags.get(0).getName();
				}
			}

			if (relationship.equals(RelTypes.HAS_NATIONALITY_OF)) {
				attributeFromMusicBrainz = artistFromMusicBrainz.getCountry();
			}

			if (!(attributeFromMusicBrainz == null || attributeFromMusicBrainz
					.equals(""))) {

				logger.debug("Attribute is found for: "
						+ artistNode.getProperty("name").toString()
						+ " relationship: " + relationship + " value: "
						+ attributeFromMusicBrainz);

				if (relationship.equals(RelTypes.TAGS)) {

					attributeNode = findOrCreateUniqueNode("genre",
							attributeFromMusicBrainz);
					attributeNode
							.setProperty("genre", attributeFromMusicBrainz);
					attributeNode.addLabel(new Label() {

						@Override
						public String name() {
							return "Genre";
						}
					});

				} else if (relationship.equals(RelTypes.HAS_NATIONALITY_OF)) {
					attributeNode = findOrCreateUniqueNode("nationality",
							attributeFromMusicBrainz);
					attributeNode.setProperty("nationality",
							attributeFromMusicBrainz);
					attributeNode.addLabel(new Label() {

						@Override
						public String name() {
							return "Nationality";
						}
					});
				}

				attributeNode.createRelationshipTo(artistNode, relationship);

			} else {
				logger.debug("Attribute is unknown for the relationship: "
						+ relationship + " for the artist: "
						+ artistNode.getProperty("name").toString());

				if (relationship.equals(RelTypes.TAGS)) {
					attributeNode = findOrCreateUniqueNode("genre", "unknown");
					attributeNode.setProperty("genre", "unkown");
					attributeNode.addLabel(new Label() {

						@Override
						public String name() {
							return "Genre";
						}
					});

				} else if (relationship.equals(RelTypes.HAS_NATIONALITY_OF)) {
					attributeNode = findOrCreateUniqueNode("nationality",
							"unknown");
					attributeNode.setProperty("nationality", "unknown");
					attributeNode.addLabel(new Label() {

						@Override
						public String name() {
							return "Nationality";
						}
					});
				}
				attributeNode.createRelationshipTo(artistNode, relationship);
			}

		}
	}

}

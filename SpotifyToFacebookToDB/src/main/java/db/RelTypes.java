package db;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {
	LIVES,
	STUDIED_AT, SINGS, LISTENS,TAGS,  HAS_NATIONALITY_OF
}

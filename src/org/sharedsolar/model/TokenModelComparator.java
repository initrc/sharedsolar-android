package org.sharedsolar.model;

import java.util.Comparator;

public class TokenModelComparator implements Comparator<TokenModel>{
	public int compare(TokenModel object1, TokenModel object2) {
		return object2.getTimestamp().compareTo(object1.getTimestamp());
	}
}

package org.sharedsolar.model;

import java.util.Comparator;

public class AccountListModelComparator implements Comparator<AccountListModel>{
	public int compare(AccountListModel object1, AccountListModel object2) {
		return object1.getAid().compareTo(object2.getAid());
	}
}

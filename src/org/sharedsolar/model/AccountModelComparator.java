package org.sharedsolar.model;

import java.util.Comparator;

public class AccountModelComparator implements Comparator<AccountModel>{
	public int compare(AccountModel object1, AccountModel object2) {
		return object1.getAid().compareTo(object2.getAid());
	}
}

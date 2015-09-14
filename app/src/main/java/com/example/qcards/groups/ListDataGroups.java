package com.example.qcards.groups;

import java.util.HashMap;
import java.util.List;

import com.example.qcards.contactsqlite.Contact;

public class ListDataGroups {
	
	List<String> listDataHeader;
	HashMap<String, List<Contact>> listDataChild;
	
	public ListDataGroups() {
		// TODO Auto-generated constructor stub
	}
	
	public ListDataGroups(List<String> listDataHeader, HashMap<String, List<Contact>> listDataChild) {
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }

    public List<String> getListGroupsNames() {
        return listDataHeader;
    }

    public HashMap<String, List<Contact>> getListGroupsData() {
        return listDataChild;
    }

	

}

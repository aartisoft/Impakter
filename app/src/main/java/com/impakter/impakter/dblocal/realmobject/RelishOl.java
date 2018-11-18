package com.impakter.impakter.dblocal.realmobject;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RelishOl extends RealmObject {

	private String id, name;
	private double price;
	private RealmList<RelishOptionOl> arrOptions;
	private int selectedIndex = 0;
	private String selectedToppingOption = "";
	private boolean isChecked;

	public RelishOl(String id, String name, double price, RealmList<RelishOptionOl> arrOptions, int selectedIndex, String selectedToppingOption, boolean isChecked) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.arrOptions = arrOptions;
		this.selectedIndex = selectedIndex;
		this.selectedToppingOption = selectedToppingOption;
		this.isChecked = isChecked;
	}

	public RelishOl() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public RealmList<RelishOptionOl> getArrOptions() {
		return arrOptions;
	}

	public void setArrOptions(RealmList<RelishOptionOl> arrOptions) {
		this.arrOptions = arrOptions;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public String getSelectedToppingOption() {
		return selectedToppingOption;
	}

	public void setSelectedToppingOption(String selectedToppingOption) {
		this.selectedToppingOption = selectedToppingOption;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}
}

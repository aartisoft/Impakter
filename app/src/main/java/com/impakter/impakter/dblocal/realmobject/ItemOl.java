package com.impakter.impakter.dblocal.realmobject;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemOl extends RealmObject {

    // id: "1394421159",
    // name: "Coca-Cola",
    // desc: "We believe it&#39;s not just what you do but how you do it ",
    // thumb:
    // "http://fruitysolution.com/dwaine/site/image.html?id=1394421159&f=1397467940.jpg&t=products",
    // small_thumb:
    // "http://fruitysolution.com/dwaine/site/image.html?id=1394421159&f=S1397467940.jpg&t=products",
    // price: "2",
    // promotion: null,
    // promotion_desc: "",
    // urls_image: null,
    // urls_video: null,
    // menu: "1397446507"
    @PrimaryKey
    private String primarykeyItem;
    private String id;
    private String name, desc, thumb, menuId;

    private double price;
    //	private CookMethod selectedCookMethod;
    private String allergenic, recipe, diet;

    public String getAllergenic() {
        return allergenic;
    }

    public void setAllergenic(String allergenic) {
        this.allergenic = allergenic;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public CookMethodOl getSelectedCookMethod() {
        return selectedCookMethod;
    }

    public void setSelectedCookMethod(CookMethodOl selectedCookMethod) {
        this.selectedCookMethod = selectedCookMethod;
    }

    public ItemOl() {
    }

    public ItemOl(String primarykeyItem, String id, String name, String desc,
                  String thumb, String menuId, double price, String allergenic, String recipe,
                  String diet) {
        this.primarykeyItem = primarykeyItem;
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.thumb = thumb;
        this.menuId = menuId;
        this.price = price;
        this.allergenic = allergenic;
        this.recipe = recipe;
        this.diet = diet;
    }

    private CookMethodOl selectedCookMethod;

    public String getPrimarykeyItem() {
        return primarykeyItem;
    }

    public void setPrimarykeyItem(String primarykeyItem) {
        this.primarykeyItem = primarykeyItem;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CategoryOlv2 getCategory(List<CategoryOlv2> categoryOlv2s) {
        for (CategoryOlv2 category : categoryOlv2s) {
            if (menuId.equals(category.getId())) {
                return category;
            }
        }
        return null;
    }


//	public CookMethod getSelectedCookMethod() {
//
//		if (selectedCookMethod != null)
//			return selectedCookMethod;
//		if (getCategory().getArrCookMethods().size() > 0) {
//			selectedCookMethod = getCategory().getArrCookMethods().get(0);
//		}
//		return selectedCookMethod;
//	}
//
//	public void setSelectedCookMethod(CookMethod selectedCookMethod) {
//		this.selectedCookMethod = selectedCookMethod;
//	}

}

package com.segomezco.gestortiendas.Store;

public class StoreModel {
    public String name;
    public String description;
    public String category;
    public String openHours;
    public String address;
    public String owner;
    public String phone;
    public String imageUrl;

    public StoreModel() {
    }

    public StoreModel(String name, String description, String category, String openHours,
                      String address, String phone, String owner) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.openHours = openHours;
        this.address = address;
        this.owner = owner;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenHours() {
        return openHours;
    }
    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

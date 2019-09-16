package com.amitKundu.tourmate.Classes;

public class MemoryClass {

    private String memory_id;
    private String captionDescription;
    private String caption;
    private String postimages;
    private String  currentMemoryDate;
    private String currentMemoryTime;

    public MemoryClass() {
    }

    public MemoryClass(String captionDescription, String caption, String postimages, String currentMemoryDate, String currentMemoryTime) {
        this.captionDescription = captionDescription;
        this.caption = caption;
        this.postimages = postimages;
        this.currentMemoryDate = currentMemoryDate;
        this.currentMemoryTime = currentMemoryTime;
    }

    public String getMemory_id() {
        return memory_id;
    }

    public void setMemory_id(String memory_id) {
        this.memory_id = memory_id;
    }

    public String getCaptionDescription() {
        return captionDescription;
    }

    public void setCaptionDescription(String captionDescription) {
        this.captionDescription = captionDescription;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPostimages() {
        return postimages;
    }

    public void setPostimages(String postimages) {
        this.postimages = postimages;
    }

    public String getCurrentMemoryDate() {
        return currentMemoryDate;
    }

    public void setCurrentMemoryDate(String currentMemoryDate) {
        this.currentMemoryDate = currentMemoryDate;
    }

    public String getCurrentMemoryTime() {
        return currentMemoryTime;
    }

    public void setCurrentMemoryTime(String currentMemoryTime) {
        this.currentMemoryTime = currentMemoryTime;
    }
}

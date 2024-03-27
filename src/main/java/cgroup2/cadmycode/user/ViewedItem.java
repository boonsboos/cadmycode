package cgroup2.cadmycode.user;

/**
 * keeps track of the percentage of the viewed items of a user
 */
public class ViewedItem {

    private int contentItemID;
    private int userID;
    private int viewed;

    /**
     * creates a viewedItem for the user
     * @param contentItemID the ID of a viewed item
     * @param userID the ID of a user
     * @param viewed the percentage viewed
     */
    public ViewedItem(int contentItemID, int userID, int viewed) {
        this.contentItemID = contentItemID;
        this.userID = userID;
        this.viewed = viewed;
    }

    /**
     * gets the content item ID
     * @return the content item ID
     */
    public int getContentItemID() {
        return contentItemID;
    }

    /**
     * gets the user ID
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * gets the view
     * @return the view
     */
    public int getViewed() {
        return viewed;
    }
}

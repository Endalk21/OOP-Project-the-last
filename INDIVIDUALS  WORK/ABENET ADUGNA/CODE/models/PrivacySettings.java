package models;

public class PrivacySettings {
    private boolean isPrivate = false;
    private boolean allowComments = true;
    private boolean allowTags = true;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isAllowComments() {
        return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    public boolean isAllowTags() {
        return allowTags;
    }

    public void setAllowTags(boolean allowTags) {
        this.allowTags = allowTags;
    }
}

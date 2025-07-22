package com.example.securitylib;

public class SecurityConfig {

    private boolean blockScreenshots;
    private boolean blockCopyPaste;
    private boolean blockRecentAppsPreview;

    public SecurityConfig(boolean blockScreenshots, boolean blockCopyPaste, boolean blockRecentAppsPreview) {
        this.blockScreenshots = blockScreenshots;
        this.blockCopyPaste = blockCopyPaste;
        this.blockRecentAppsPreview = blockRecentAppsPreview;
    }

    public boolean isBlockScreenshots() {
        return blockScreenshots;
    }

    public boolean isBlockCopyPaste() {
        return blockCopyPaste;
    }

    public boolean isBlockRecentAppsPreview() {
        return blockRecentAppsPreview;
    }

    public static class Builder {
        private boolean blockScreenshots = true;
        private boolean blockCopyPaste = true;
        private boolean blockRecentAppsPreview = true;

        public Builder disableScreenshots(boolean value) {
            this.blockScreenshots = value;
            return this;
        }

        public Builder disableCopyPaste(boolean value) {
            this.blockCopyPaste = value;
            return this;
        }

        public Builder disableRecentAppsPreview(boolean value) {
            this.blockRecentAppsPreview = value;
            return this;
        }

        public SecurityConfig build() {
            return new SecurityConfig(blockScreenshots, blockCopyPaste, blockRecentAppsPreview);
        }
    }
}
package com.example.securitylib;

public class SecurityConfig {

    private boolean blockScreenshotsAndRecentApps;
    private boolean blockCopyPaste;

    public SecurityConfig(boolean blockScreenshotsAndRecentApps, boolean blockCopyPaste) {
        this.blockScreenshotsAndRecentApps = blockScreenshotsAndRecentApps;
        this.blockCopyPaste = blockCopyPaste;
    }

    public boolean isBlockScreenshotsAndRecentApps() {
        return blockScreenshotsAndRecentApps;
    }

    public boolean isBlockCopyPaste() {
        return blockCopyPaste;
    }

    public static class Builder {
        private boolean blockScreenshotsAndRecentApps = true;
        private boolean blockCopyPaste = true;

        public Builder disableScreenshotsAndRecentApps(boolean value) {
            this.blockScreenshotsAndRecentApps = value;
            return this;
        }

        public Builder disableCopyPaste(boolean value) {
            this.blockCopyPaste = value;
            return this;
        }

        public SecurityConfig build() {
            return new SecurityConfig(blockScreenshotsAndRecentApps, blockCopyPaste);
        }
    }
}
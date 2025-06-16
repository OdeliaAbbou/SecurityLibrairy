package com.example.securitylib;

public class SecurityConfig {

    private boolean blockScreenshots;
    private boolean blockCopyPaste;

    public SecurityConfig(boolean blockScreenshots, boolean blockCopyPaste) {
        this.blockScreenshots = blockScreenshots;
        this.blockCopyPaste = blockCopyPaste;
    }

    public boolean isBlockScreenshots() {
        return blockScreenshots;
    }

    public boolean isBlockCopyPaste() {
        return blockCopyPaste;
    }

    // Builder pattern pour rendre l'utilisation simple et propre
    public static class Builder {
        private boolean blockScreenshots = true;
        private boolean blockCopyPaste = true;

        public Builder disableScreenshots(boolean value) {
            this.blockScreenshots = value;
            return this;
        }

        public Builder disableCopyPaste(boolean value) {
            this.blockCopyPaste = value;
            return this;
        }

        public SecurityConfig build() {
            return new SecurityConfig(blockScreenshots, blockCopyPaste);
        }
    }
}

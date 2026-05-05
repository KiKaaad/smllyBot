package com.kika.smllybot.utils.localization;

public record I18nRequest(String lang, String category, String module, String name, String key) {

    public I18nRequest(String lang, String category, String name, String key) {
        this(lang, category, "", name, key);
    }

    public String getFilePath() {
        if (module == null || module.isEmpty()) {
            return String.format("/language/%s/%s/%s.json", lang, category, name);
        }
        return String.format("/language/%s/%s/%s/%s.json", lang, category, name, module);
    }

}

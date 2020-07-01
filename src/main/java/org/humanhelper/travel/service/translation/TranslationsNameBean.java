package org.humanhelper.travel.service.translation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.humanhelper.data.bean.name.ProxyNameIdBean;

import java.util.Map;

/**
 * Содержит id, name, и переводы name
 *
 * @author Андрей
 * @since 05.09.15
 */
public class TranslationsNameBean extends ProxyNameIdBean {

    @JsonUnwrapped(prefix = NAME_FIELD + "_")
    private Translations translations;//Перевод name

    public Translations getTranslations() {
        return translations;
    }

    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    public void createTranslations(Map<String, String> translations) {
        this.translations = new Translations();
        this.translations.set(translations);
    }

    public Translations createOrGetTranslations() {
        if (translations == null) {
            translations = new Translations();
        }
        return translations;
    }

    /**
     * Задать перевод для name
     */
    public void setName(Language language, String name) {
        if (language.isMain()) {
            setName(name);
        } else {
            if (!name.equals(getName())) {//Отсекаем дубликаты в переводе
                createOrGetTranslations().set(language, name);
            }
        }
    }

    public boolean equalsNameWithTranslations(String name) {
        return getName().equals(name) || (translations != null && translations.getTranslationLanguage(name) != null);
    }

    public boolean isSimilar(Translations translations) {
        return this.translations != null && this.translations.isTranslationsSimilar(translations);
    }

}

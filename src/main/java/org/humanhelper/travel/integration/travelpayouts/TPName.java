package org.humanhelper.travel.integration.travelpayouts;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Удаляем дубликаты в имени
 *
 * @author Андрей
 * @since 08.01.15
 */
public class TPName {

    private String code;
    private String name;
    private Map<String, String> name_translations;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getName_translations() {
        return name_translations;
    }

    public void setName_translations(Map<String, String> name_translations) {
        this.name_translations = name_translations;
    }

    public void deleteDuplicates() {
        Collection<String> keys = new HashSet<>(name_translations.keySet());
        for (String lang : keys) {
            String value = name_translations.get(lang);
            if (value != null) {
                value = value.trim();
                if (value.isEmpty()) {
                    name_translations.remove(lang);
                } else {
                    name_translations.put(lang, value);
                    if (name.equalsIgnoreCase(value)) {
                        name_translations.remove(lang);
                    }
                }
            } else {
                name_translations.remove(lang);
            }
        }
    }

    @Override
    public String toString() {
        return "TPName{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", name_translations=" + name_translations +
                '}';
    }
}

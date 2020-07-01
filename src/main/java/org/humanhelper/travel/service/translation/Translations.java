package org.humanhelper.travel.service.translation;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Андрей
 * @since 05.09.15
 */
public class Translations implements Serializable {

    private String ru;
    private String de;
    private String zh_CN;
    private String tr;
    private String fr;
    private String es;
    private String it;
    private String th;

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getZh_CN() {
        return zh_CN;
    }

    public void setZh_CN(String zh_CN) {
        this.zh_CN = zh_CN;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public Translations set(Map<String, String> values) {
        for (Language language : Language.values()) {
            if (!language.isMain()) {
                String value = values.get(language.getLocale().toString());
                set(language, value);
            }
        }
        return this;
    }

    public Translations set(Language language, String value) {
        if (value != null) {
            language.setFromMap(this, value);
        }
        return this;
    }

    /**
     * Если среди переводов есть значение равное value, то будет возвращен язык этого перевода, иначе возвращается null
     */
    public Language getTranslationLanguage(String value) {
        for (Language language : Language.values()) {
            if (!language.isMain()) {
                String trValue = language.getValue(this);
                if (value.equals(trValue)) {
                    return language;
                }
            }
        }
        return null;
    }

    /**
     * Если в переводах для какого-то языка заданы оба значения и различия в них меньше 3 букв, то возвращаем true
     */
    public boolean isTranslationsSimilar(Translations translations) {
        if (translations == null) {
            return false;
        }

        for (Language language : Language.values()) {
            if (!language.isMain()) {
                String sourceValue = language.getValue(this);
                String targetValue = language.getValue(translations);
                if (sourceValue != null && targetValue != null && StringUtils.getLevenshteinDistance(sourceValue, targetValue) < 3) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "{" +
                "ru='" + ru + '\'' +
                ", de='" + de + '\'' +
                ", zh_CN='" + zh_CN + '\'' +
                ", tr='" + tr + '\'' +
                ", fr='" + fr + '\'' +
                ", es='" + es + '\'' +
                ", it='" + it + '\'' +
                ", th='" + th + '\'' +
                '}';
    }
}

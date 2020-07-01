package org.humanhelper.travel.service.translation;

import java.util.Locale;

/**
 * @author Андрей
 * @since 05.09.15
 */
public enum Language {

    en(Locale.ENGLISH) {
        @Override
        public void setFromMap(Translations translations, String value) {
            throw new IllegalStateException("Can't set translation for main language");
        }

        @Override
        public boolean isMain() {
            return true;
        }

        @Override
        public String getValue(Translations translations) {
            throw new IllegalStateException("Can't get translation for main language");
        }
    },
    ru(new Locale("ru")) {
        @Override
        public void setFromMap(Translations translations, String value) {
            translations.setRu(value);
        }

        @Override
        public String getValue(Translations translations) {
            return translations.getRu();
        }
    },
    de(Locale.GERMAN) {
        @Override
        public void setFromMap(Translations translations, String value) {
            translations.setDe(value);
        }

        @Override
        public String getValue(Translations translations) {
            return translations.getDe();
        }
    },
    zh_CN(Locale.SIMPLIFIED_CHINESE) {
        @Override
        public void setFromMap(Translations translations, String value) {
            translations.setZh_CN(value);
        }

        @Override
        public String getValue(Translations translations) {
            return translations.getZh_CN();
        }
    },
    tr(new Locale("tr")) {
        @Override
        public void setFromMap(Translations translations, String value) {
            translations.setTr(value);
        }

        @Override
        public String getValue(Translations translations) {
            return translations.getTr();
        }
    },
    fr(Locale.FRENCH) {
        @Override
        public void setFromMap(Translations translations, String value) {
            translations.setFr(value);
        }

        @Override
        public String getValue(Translations translations) {
            return translations.getFr();
        }
    },
    es(new Locale("es")) {
        @Override
        public void setFromMap(Translations translations, String value) {
            translations.setEs(value);
        }

        @Override
        public String getValue(Translations translations) {
            return translations.getEs();
        }
    },
    it(Locale.ITALIAN) {
        @Override
        public void setFromMap(Translations translations, String value) {
            translations.setIt(value);
        }

        @Override
        public String getValue(Translations translations) {
            return translations.getIt();
        }
    },
    th(new Locale("th")) {
        @Override
        public void setFromMap(Translations translations, String value) {
            translations.setTh(value);
        }

        @Override
        public String getValue(Translations translations) {
            return translations.getTh();
        }
    };

    private Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    abstract public void setFromMap(Translations translations, String value);

    abstract public String getValue(Translations translations);

    public boolean isMain() {
        return false;
    }

    public String getLocaleName() {
        return getLocale().toLanguageTag().toLowerCase();
    }
}

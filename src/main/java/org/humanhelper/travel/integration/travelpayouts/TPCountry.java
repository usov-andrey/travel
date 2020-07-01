package org.humanhelper.travel.integration.travelpayouts;

/**
 * {
 * "code": "NC",
 * "name": "New Caledonia",
 * "currency": "XPF",
 * "name_translations": {
 * "de": "Neukaledonien",
 * "en": "New Caledonia",
 * "zh-CN": "新喀里多尼亚",
 * "tr": "Yeni Kaledonya",
 * "ru": "Новая Каледония",
 * "fr": "Nouvelle-Calédonie",
 * "es": "Nueva Caledonia",
 * "it": "Nuova Caledonia",
 * "th": "ประเทศนิวแคลิโดเนีย"
 * }
 * }
 *
 * @author Андрей
 * @since 08.01.15
 */
public class TPCountry extends TPName {

    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "TPCountry{" +
                "currency='" + currency + '\'' +
                super.toString() +
                '}';

    }
}

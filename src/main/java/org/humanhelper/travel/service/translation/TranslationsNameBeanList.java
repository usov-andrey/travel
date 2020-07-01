package org.humanhelper.travel.service.translation;

import org.humanhelper.data.bean.name.NameIdList;

/**
 * Ищем по имени еще и в переводах
 *
 * @author Андрей
 * @since 08.01.16
 */
public class TranslationsNameBeanList<T extends TranslationsNameBean> extends NameIdList<T, String> {

    @Override
    protected boolean equalsName(T object, String name) {
        return object.equalsNameWithTranslations(name);
    }

}

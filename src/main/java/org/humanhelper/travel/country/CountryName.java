package org.humanhelper.travel.country;

/**
 * @author Андрей
 * @since 18.12.14
 */
public class CountryName {

    private String common;
    private String official;

    public CountryName() {
    }

    public CountryName(String common, String official) {
        this.common = common;
        this.official = official;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getOfficial() {
        return official;
    }

    public void setOfficial(String official) {
        this.official = official;
    }
}

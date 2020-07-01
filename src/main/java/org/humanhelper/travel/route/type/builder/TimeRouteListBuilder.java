package org.humanhelper.travel.route.type.builder;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.price.PriceAgent;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.price.SimplePriceAgent;
import org.humanhelper.travel.route.discount.DiscountResolver;
import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.FixedTimeRoute;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.waydiscount.DiscountStrategy;
import org.humanhelper.travel.route.waydiscount.FixedPriceTwoWaysDiscountStrategy;
import org.humanhelper.travel.service.period.DatePeriod;
import org.humanhelper.travel.transport.Transport;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Создаем список TimeRoute
 *
 * @author Андрей
 * @since 02.10.15
 */
public class TimeRouteListBuilder {

    private Currency currency;
    private PriceAgent agent;
    private List<Value> valueList = new ArrayList<>();
    private Value value;

    public TimeRouteListBuilder(Currency currency, PriceAgent agent) {
        this.currency = currency;
        this.agent = agent;
    }

    public static TimeRouteListBuilder build(Currency currency, PriceAgent agent) {
        return new TimeRouteListBuilder(currency, agent);
    }

    public static TimeRouteListBuilder build(Currency currency) {
        return build(currency, SimplePriceAgent.INSTANCE);
    }

    public static TimeRoute create(String value) {
        //Строка в формате
        //Moscow to Kualumpur at (31.10.2015 19:00:00, 01.11.2015 8:00:00)
        String source = StringHelper.getStringBefore(value, " to ");
        String target = StringHelper.getStringBetween(value, " to ", " at ");
        String startTime = StringHelper.getStringBetween(value, "(", ",");
        String endTime = StringHelper.getStringBetween(value, ", ", ")");
        return new FixedTimeRoute(Region.build(source), null, Region.build(target), null,
                DateHelper.getDateTime(startTime, "dd.MM.yyyy hh:mm:ss"),
                DateHelper.getDateTime(endTime, "dd.MM.yyyy hh:mm:ss"));
    }

    public TimeRouteListBuilder time(int startHour, int endHour) {
        if (value == null) {
            value = addValue();
        }
        value.startTime = day -> new DateHelper.FixedDate(DateHelper.setTime(day, startHour, 0));
        value.endTime = day -> new DateHelper.FixedDate(DateHelper.setTime(endHour > startHour ? day : DateHelper.incDays(day, 1), endHour, 0));
        return this;
    }

    public TimeRouteListBuilder timeDuration(int startHour, int durationHours) {
        if (value == null) {
            value = addValue();
        }
        value.startTime = day -> new DateHelper.FixedDate(DateHelper.setTime(day, startHour, 0));
        value.endTime = day -> new DateHelper.FixedDate(DateHelper.incHours(DateHelper.setTime(day, startHour, 0), durationHours));
        return this;
    }

    public TimeRouteListBuilder price(float price) {
        if (value == null) {
            value = addValue();
        }
        value.price = price;
        return this;
    }

    public TimeRouteListBuilder duration(int duration) {
        if (value == null) {
            value = addValue();
        }
        value.duration = duration;
        return this;
    }

    public TimeRouteListBuilder route(Place source, Place target) {
        value = addValue();
        value.source = source;
        value.target = target;
        return this;
    }

    /**
     * use instead route
     */
    @Deprecated
    public TimeRouteListBuilder way(Place source, Place target, int sourceToTargetHours, int targetToSourceHours, int hours, Float priceSourceToTarget, Float priceTargetToSource, Float priceTogether) {
        return route(source, target).time(sourceToTargetHours, getHours(sourceToTargetHours, hours)).price(priceSourceToTarget)
                .backRoute(priceTogether).time(targetToSourceHours, getHours(targetToSourceHours, hours)).price(priceTargetToSource);
    }

    /**
     * use instead route
     */
    @Deprecated
    public TimeRouteListBuilder way(Place source, Place target, int sourceToTargetHours, int targetToSourceHours, int hours, Float priceSourceToTarget, Float priceTargetToSource) {
        return route(source, target).time(sourceToTargetHours, getHours(sourceToTargetHours, hours)).price(priceSourceToTarget)
                .backRoute().time(targetToSourceHours, getHours(targetToSourceHours, hours)).price(priceTargetToSource);
    }

    private int getHours(int hours, int durationInHours) {
        int result = hours + durationInHours;
        if (result >= 24) {
            result -= 24;
        }
        return result;
    }

    private Value addValue() {
        Value value = new Value();
        valueList.add(value);
        return value;
    }

    public List<FixedTimeRoute> buildDaily(DatePeriod period) {
        List<FixedTimeRoute> result = new ArrayList<>();
        for (Value value : valueList) {
            for (Date day : period.dayIterator()) {
                result.add(value.createTime(day, currency, agent));
            }
            //Необходимо пересоздать скидки
        }
        return result;
    }


    public Set<TimeRoute> buildDay(Date day) {
        return buildDay(new HashSet<>(), day);
    }

    private <T extends Collection<TimeRoute>> T buildDay(T result, Date day) {
        result.addAll(valueList.stream().map(
                value -> value.createTime(day, currency, agent)
        ).collect(Collectors.toList()));
        return result;
    }

    public List<AnyTimeRoute> buildAnyTime() {
        List<AnyTimeRoute> result = new ArrayList<>();
        for (Value value : valueList) {
            result.add(value.createAnyTime(currency, agent));
        }
        return result;
    }

    /**
     * Цена за одно перемещение при скидке
     */
    public TimeRouteListBuilder backRoute(float discountPrice) {
        value.discountStrategy = new FixedPriceTwoWaysDiscountStrategy(discountPrice);
        value.discount = new Discount(discountPrice * 2);
        return backRoute();
    }

    public TimeRouteListBuilder backRoute() {
        Value oldValue = value;
        value = addValue();
        value.back(oldValue);
        return this;
    }

    public PriceResolver getPriceResolver() {
        return value.getPriceResolver(currency, agent);
    }

    public TimeRouteListBuilder price(String price) {
        return price(Float.parseFloat(price));
    }

    public TimeRouteListBuilder company(String company) {
        value.company = company;
        return this;
    }

    public TimeRouteListBuilder transport(String transportName) {
        value.transportName = transportName;
        return this;
    }

    /**
     * @param time формат "HH:mm"
     */
    public TimeRouteListBuilder startHHmm(String time) {
        value.startTime = dateHHmm(time);
        return this;
    }

    /**
     * @param time формат "HH:mm"
     */
    public TimeRouteListBuilder endHHmm(String time) {
        value.endTime = dateHHmm(time);
        return this;
    }

    private Function<Date, Date> dateHHmm(String time) {
        return day -> new DateHelper.FixedDate(DateHelper.getDate(day, fixTime(time), "HH:mm"));//24 формат
    }

    private String fixTime(String time) {
        //08:35 (+1 Day)
        if (time.length() == 5) {
            return time;
        }
        return StringHelper.getStringBefore(time, " ");
    }

    /**
     * @param time "2015-12-18 14:45:00"
     */
    public TimeRouteListBuilder startDateTime(String time) {
        value.startTime = dateTime(time);
        return this;
    }

    /**
     * @param time "2015-12-18 14:45:00"
     */
    public TimeRouteListBuilder endDateTime(String time) {
        value.endTime = dateTime(time);
        return this;
    }

    private Function<Date, Date> dateTime(String time) {
        return day -> new DateHelper.FixedDate(DateHelper.getDate(day, time, "yyyy-MM-dd hh:mm:ss"));
    }

    private class Discount {

        private float priceTogether;
        private DiscountResolver discountResolver;

        public Discount(float priceTogether) {
            this.priceTogether = priceTogether;
            recreate();
        }

        public void recreate() {
            this.discountResolver = new DiscountResolver();
        }

        public void addRoute(FixedTimeRoute route) {
            //Стоимость другого маршрута, при использовании перед ним текущего route будет равна
            //route + route2 = priceTogether
            discountResolver.addDiscountPrice(route, priceTogether / 2);
            route.setDiscountResolver(discountResolver);
        }
    }

    private class Value {
        private Place source;
        private Place target;
        private String company;
        private String transportName;
        private Function<Date, Date> startTime;
        private Function<Date, Date> endTime;
        private float price;
        private int duration;
        private DiscountStrategy discountStrategy;
        private Discount discount;

        public FixedTimeRoute createTime(Date day, Currency currency, PriceAgent agent) {
            FixedTimeRoute route = new FixedTimeRoute()
                    .price(getPriceResolver(currency, agent)).discount(discountStrategy);
            if (startTime != null && endTime != null) {
                route.time(startTime.apply(day), endTime.apply(day));
            }
            route.sourceTarget(source, target).transport(getTransport());
            if (discount != null) {
                discount.addRoute(route);
            }
            return route;
        }

        public AnyTimeRoute createAnyTime(Currency currency, PriceAgent agent) {
            AnyTimeRoute route = new AnyTimeRoute().duration(duration).price(getPriceResolver(currency, agent));
            route.sourceTarget(source, target).transport(getTransport());
            return route;
        }

        public PriceResolver getPriceResolver(Currency currency, PriceAgent agent) {
            return new PriceResolver(price, currency, agent);
        }

        private Transport getTransport() {
            return company == null && transportName == null ? null :
                    new Transport(transportName, company);
        }

        public void back(Value value) {
            source = value.target;
            target = value.source;
            startTime = value.startTime;
            endTime = value.endTime;
            price = value.price;
            duration = value.duration;
            discountStrategy = value.discountStrategy;
            discount = value.discount;
        }
    }
}

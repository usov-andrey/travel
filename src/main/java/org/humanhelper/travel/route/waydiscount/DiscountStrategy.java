package org.humanhelper.travel.route.waydiscount;

import org.humanhelper.travel.route.type.Route;

/**
 * Скидки бывают,
 * 1) когда берешь билет туда обратно
 * 2) или когда берешь два билета, в который исходная точка равна конечной
 * 3) либо когда летишьпоследовательно двумя перелетами одной компании
 * 4) либо когда исходная точка в первом перемещении равна конечной во втором
 * Т.е. скидка возможна для одной и той же транспортной компании и в ней определяется как расчитывается скидка.
 * 1-TwoWaysDiscountStrategy
 * Если билет туда обратно: то N1.startPlace=N2.endPlace и N1.endPlace=N2.startPlace
 * 2.
 * Если билет c дубликатом исходной: N1.startPlace=N2.endPlace
 * 3.
 * Если последовательность билетов: N1.endPlace=N2.startPlace
 * <p>
 * Для каждого найденного маршрута в зависимости от стратегии проставляем скидки начала и скидки конца:
 * <p>
 * У скидки начала храним код конца:
 * 1) startPlace + endPlace + transport + discount1
 * 2) startPlace + transport + discount2
 * 3) endPlace + transport + discount3
 * Когда мы придем в маршрут из endPlace(3), когда мы придем в маршрут в startPlace(2) и когда мы придем в маршрут из endPlace в startPlace(1)
 * <p>
 * У скидки конец храним его код
 * 1) endPlace + startPlace + transport + discount1
 * 2) endPlace + transport + discount2
 * 3) startPlace + transport + discount3
 * <p>
 * Для каждого перелета мы создаем в зависимости от типа скидки для transport две новые вершины в графе.
 *
 * @author Андрей
 * @since 22.12.14
 */
public interface DiscountStrategy {

    public int getEndCodeForStartWay(Route way);

    public int getEndCodeForEndWay(Route way);

    public Float getPriceForStartWay(Route way);

    public Float getPriceForEndWay(Route way);
}

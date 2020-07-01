package org.humanhelper.travel.price;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Андрей
 * @since 26.12.14
 */
@Service
public class PriceAgentDao {

    private Map<String, PriceAgent> ticketAgentMap = new HashMap<>();

    public PriceAgent getPriceAgent(String name) {
        return ticketAgentMap.get(name);
    }

    public void addPriceAgent(PriceAgent ticketAgent) {
        ticketAgentMap.put(ticketAgent.getName(), ticketAgent);
    }
}

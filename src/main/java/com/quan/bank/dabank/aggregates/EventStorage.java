package com.quan.bank.dabank.aggregates;

import com.google.common.collect.ImmutableList;
import com.quan.bank.dabank.aggregates.AccountAggregate;
import com.quan.bank.dabank.aggregates.events.RootEvent;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static io.vavr.collection.List.ofAll;

public class EventStorage {
    private final Map<UUID, List<RootEvent>> events = new ConcurrentHashMap<>();

    public static AccountAggregate reconstruct(Collection<RootEvent> events) {
        return ofAll(events).foldLeft(new AccountAggregate(events), (AccountAggregate::apply));
    }

    public ImmutableList<AccountAggregate> findAll() {
        return events.values().stream().map(EventStorage::reconstruct).collect(ImmutableList.toImmutableList());
    }

    public AccountAggregate get(UUID uuid) {
        if (events.containsKey(uuid)) {
            return reconstruct(events.get(uuid));
        }
        return null;
    }

    public boolean exists(UUID uuid) {return events.containsKey(uuid);}

    public void save(RootEvent rootEvent) {
        events.compute(rootEvent.getAggregateUUID(), (id, events) -> (events == null) ? ImmutableList.of(rootEvent)
                : new ImmutableList.Builder<RootEvent>().addAll(events).add(rootEvent).build());
    }
}

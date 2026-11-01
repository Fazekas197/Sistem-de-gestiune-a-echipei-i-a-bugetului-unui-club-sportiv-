package org.sportsclub.dam.sports_club_manager.service.audit;

import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class InMemoryAuditService implements AuditService {
    private final List<AuditRecord> records = new CopyOnWriteArrayList<>();
    @Override public void record(User actor, AuditAction action, String entityType, UUID entityId, String details) {
        records.add(new AuditRecord(UUID.randomUUID(), Instant.now(),
                actor == null ? null : actor.getId(), action, entityType, entityId,
                details == null ? "" : details));
    }
    @Override public List<AuditRecord> findAll() { return List.copyOf(records); }
}

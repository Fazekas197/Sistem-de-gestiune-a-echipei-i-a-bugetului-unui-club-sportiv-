package org.sportsclub.dam.sports_club_manager.service.audit;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import java.util.List;
public interface AuditService {
    void record(User actor, AuditAction action, String entityType, java.util.UUID entityId, String details);
    List<AuditRecord> findAll();
}

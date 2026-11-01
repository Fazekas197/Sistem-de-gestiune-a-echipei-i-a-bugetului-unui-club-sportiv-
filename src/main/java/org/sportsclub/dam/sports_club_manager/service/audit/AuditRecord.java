package org.sportsclub.dam.sports_club_manager.service.audit;
import java.time.Instant;
import java.util.UUID;
public record AuditRecord(UUID id, Instant timestamp, UUID actorId, AuditAction action,
                          String entityType, UUID entityId, String details) { }

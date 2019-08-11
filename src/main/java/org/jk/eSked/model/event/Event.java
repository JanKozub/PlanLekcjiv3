package org.jk.eSked.model.event;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

public class Event {
    private final UUID id;
    private final LocalDate date;
    private final long dateTimestamp;
    private final int hour;
    private final EventType eventType;
    private final String topic;
    private final LocalDate createdDate;
    private final long createdDateTimestamp;

    public Event(UUID id, long date, int hour, EventType eventType, String topic, long createdDate) {
        this.id = id;
        this.date = Instant.ofEpochMilli(date).atZone(ZoneOffset.systemDefault()).toLocalDate();
        this.dateTimestamp = date;
        this.hour = hour;
        this.eventType = eventType;
        this.topic = topic;
        this.createdDate = Instant.ofEpochMilli(createdDate).atZone(ZoneOffset.systemDefault()).toLocalDate();
        this.createdDateTimestamp = createdDate;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getTopic() {
        return topic;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public long getDateTimestamp() {
        return dateTimestamp;
    }

    public long getCreatedDateTimestamp() {
        return createdDateTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return hour == event.hour &&
                Objects.equals(id, event.id) &&
                Objects.equals(date, event.date) &&
                eventType == event.eventType &&
                Objects.equals(topic, event.topic) &&
                Objects.equals(createdDate, event.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, hour, eventType, topic, createdDate);
    }
}
package net.datasa.finders.domain.entity;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatParticipantId implements Serializable {
    private int chatroomId;
    private String participantId;
}


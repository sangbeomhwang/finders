package net.datasa.finders.domain.entity;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamEntityId implements Serializable {
    private Integer projectNum;
    private String memberId;
}
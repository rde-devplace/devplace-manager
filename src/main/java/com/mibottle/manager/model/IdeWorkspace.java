package com.mibottle.manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ide_workspace")
public class IdeWorkspace {

    @Id
    private String name;
    private String projectName;

    @Column(columnDefinition = "TEXT")
    private String description;
}

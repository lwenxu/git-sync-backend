package com.lwen.gitsync.entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JobSchedule {
    @Id
    @GeneratedValue
    private int id;
    private String syncRule;
    private String repository;
    private String syncPath;
    private int status;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aid")
    private AccountInfo accountInfo;
}

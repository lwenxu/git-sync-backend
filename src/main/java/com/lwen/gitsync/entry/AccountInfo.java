package com.lwen.gitsync.entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class AccountInfo {
    @Id
    @GeneratedValue
    private int id;
    private String type;
    private String username;
    private String password;
    // @OneToMany(cascade = CascadeType.ALL)
    // @JoinColumn(name = "aid")
    // private List<JobSchedule> jobSchedule;

}

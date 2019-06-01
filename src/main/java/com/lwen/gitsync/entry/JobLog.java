package com.lwen.gitsync.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class JobLog {
    @Id@GeneratedValue
    private Integer id;
    private LocalDateTime time;
    private String errLog;
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name = "jobid")
    private JobSchedule jobSchedule;
}

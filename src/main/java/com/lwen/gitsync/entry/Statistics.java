package com.lwen.gitsync.entry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class Statistics {
    @Id
    @GeneratedValue
    private int id;
    private int successCount;
    private int failedCount;
}

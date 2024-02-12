package org.serverModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class serverData {
    private int confirmationID;
    private String remarks;
    private String description;
}

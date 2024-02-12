package org.clientModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class clientData {

    private int clientID;
    private String clientCode;
    private String model;
}
